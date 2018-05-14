package org.happyuc.webuj.tx;

import org.happyuc.webuj.abi.EventEncoder;
import org.happyuc.webuj.abi.EventValues;
import org.happyuc.webuj.abi.FunctionEncoder;
import org.happyuc.webuj.abi.FunctionReturnDecoder;
import org.happyuc.webuj.abi.TypeReference;
import org.happyuc.webuj.abi.datatypes.Address;
import org.happyuc.webuj.abi.datatypes.Event;
import org.happyuc.webuj.abi.datatypes.Function;
import org.happyuc.webuj.abi.datatypes.Type;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.protocol.core.methods.request.ReqTransaction;
import org.happyuc.webuj.protocol.core.methods.response.HucCall;
import org.happyuc.webuj.protocol.core.methods.response.HucGetCode;
import org.happyuc.webuj.protocol.core.methods.response.Log;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.protocol.exceptions.TransactionException;
import org.happyuc.webuj.tx.exceptions.ContractCallException;
import org.happyuc.webuj.utils.Numeric;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


/**
 * Solidity contract type abstraction for interacting with smart contracts via native Java types.
 */
public abstract class Contract extends ManagedTransaction {

    // https://www.reddit.com/r/ethereum/comments/5g8ia6/attention_miners_we_recommend_raising_gas_limit/
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(4300000);

    protected final String contractBinary;
    protected String contractAddress;
    protected BigInteger gasPrice;
    protected BigInteger gasLimit;
    protected RepTransactionReceipt repTransactionReceipt;
    protected Map<String, String> deployedAddresses;

    protected Contract(String contractBinary, String contractAddress, Webuj webuj, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(webuj, transactionManager);

        this.contractAddress = ensResolver.resolve(contractAddress);

        this.contractBinary = contractBinary;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
    }

    protected Contract(String contractBinary, String contractAddress, Webuj web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        this(contractBinary, contractAddress, web3j, new RawTransactionManager(web3j, credentials), gasPrice, gasLimit);
    }

    @Deprecated
    protected Contract(String contractAddress, Webuj web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        this("", contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    @Deprecated
    protected Contract(String contractAddress, Webuj web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        this("", contractAddress, web3j, new RawTransactionManager(web3j, credentials), gasPrice, gasLimit);
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setRepTransactionReceipt(RepTransactionReceipt repTransactionReceipt) {
        this.repTransactionReceipt = repTransactionReceipt;
    }

    public String getContractBinary() {
        return contractBinary;
    }

    /**
     * Allow {@code gasPrice} to be set.
     *
     * @param newPrice gas price to use for subsequent transactions
     */
    public void setGasPrice(BigInteger newPrice) {
        this.gasPrice = newPrice;
    }

    /**
     * Get the current {@code gasPrice} value this contract uses when executing transactions.
     *
     * @return the gas price set on this contract
     */
    public BigInteger getGasPrice() {
        return gasPrice;
    }

    /**
     * Check that the contract deployed at the address associated with this smart contract wrapper
     * is in fact the contract you believe it is.
     *
     * <p>This method uses the
     * <a href="https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_getcode">huc_getCode</a> method
     * to get the contract byte code and validates it against the byte code stored in this smart
     * contract wrapper.
     *
     * @return true if the contract is valid
     * @throws IOException if unable to connect to webuj node
     */
    public boolean isValid() throws IOException {
        if (contractAddress.equals("")) {
            throw new UnsupportedOperationException("Contract binary not present, you will need to regenerate your smart " + "contract wrapper with webuj v2.2.0+");
        }

        HucGetCode hucGetCode = webuj.hucGetCode(contractAddress, DefaultBlockParameterName.LATEST).send();
        if (hucGetCode.hasError()) {
            return false;
        }

        String code = Numeric.cleanHexPrefix(hucGetCode.getCode());
        // There may be multiple contracts in the Solidity bytecode, hence we only check for a
        // match with a subset
        return !code.isEmpty() && contractBinary.contains(code);
    }

    /**
     * If this Contract instance was created at deployment, the RepTransactionReceipt associated
     * with the initial creation will be provided, e.g. via a <em>deploy</em> method. This will
     * not persist for Contracts instances constructed via a <em>load</em> method.
     *
     * @return the RepTransactionReceipt generated at contract deployment
     */
    public RepTransactionReceipt getRepTransactionReceipt() {
        return repTransactionReceipt;
    }

    /**
     * Execute constant function call - i.e. a call that does not change state of the contract
     *
     * @param function to call
     * @return {@link List} of values returned by function call
     */
    private List<Type> executeCall(Function function) throws IOException {
        String encodedFunction = FunctionEncoder.encode(function);
        HucCall hucCall = webuj.hucCall(ReqTransaction.createHucCallTransaction(transactionManager.getFromAddress(), contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).send();

        String value = hucCall.getValue();
        return FunctionReturnDecoder.decode(value, function.getOutputParameters());
    }

    @SuppressWarnings("unchecked")
    protected <T extends Type> T executeCallSingleValueReturn(Function function) throws IOException {
        List<Type> values = executeCall(function);
        if (!values.isEmpty()) {
            return (T) values.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends Type, R> R executeCallSingleValueReturn(Function function, Class<R> returnType) throws IOException {
        T result = executeCallSingleValueReturn(function);
        if (result == null) {
            throw new ContractCallException("Empty value (0x) returned from contract");
        }

        Object value = result.getValue();
        if (returnType.isAssignableFrom(value.getClass())) {
            return (R) value;
        } else if (result.getClass().equals(Address.class) && returnType.equals(String.class)) {
            return (R) result.toString();  // cast isn't necessary
        } else {
            throw new ContractCallException("Unable to convert response: " + value + " to expected type: " + returnType.getSimpleName());
        }
    }

    protected List<Type> executeCallMultipleValueReturn(Function function) throws IOException {
        return executeCall(function);
    }

    protected RepTransactionReceipt executeTransaction(Function function) throws IOException, TransactionException {
        return executeTransaction(function, BigInteger.ZERO);
    }

    private RepTransactionReceipt executeTransaction(Function function, BigInteger weiValue) throws IOException, TransactionException {
        return executeTransaction(FunctionEncoder.encode(function), weiValue);
    }

    /**
     * Given the duration required to execute a transaction.
     *
     * @param data     to send in transaction
     * @param weiValue in Wei to send in transaction
     * @return transaction receipt
     * @throws IOException          if the call to the node fails
     * @throws TransactionException if the transaction was not mined while waiting
     */
    RepTransactionReceipt executeTransaction(String data, BigInteger weiValue) throws TransactionException, IOException {

        return send(contractAddress, data, weiValue, gasPrice, gasLimit);
    }

    protected <T extends Type> RemoteCall<T> executeRemoteCallSingleValueReturn(final Function function) {
        return new RemoteCall<T>(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return Contract.this.executeCallSingleValueReturn(function);
            }
        });
    }

    protected <T> RemoteCall<T> executeRemoteCallSingleValueReturn(final Function function, final Class<T> returnType) {
        return new RemoteCall<T>(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return Contract.this.executeCallSingleValueReturn(function, returnType);
            }
        });
    }

    protected RemoteCall<List<Type>> executeRemoteCallMultipleValueReturn(final Function function) {
        return new RemoteCall<List<Type>>(new Callable<List<Type>>() {
            @Override
            public List<Type> call() throws Exception {
                return Contract.this.executeCallMultipleValueReturn(function);
            }
        });
    }

    protected RemoteCall<RepTransactionReceipt> executeRemoteCallTransaction(final Function function) {
        return new RemoteCall<RepTransactionReceipt>(new Callable<RepTransactionReceipt>() {
            @Override
            public RepTransactionReceipt call() throws Exception {
                return Contract.this.executeTransaction(function);
            }
        });
    }

    protected RemoteCall<RepTransactionReceipt> executeRemoteCallTransaction(final Function function, final BigInteger weiValue) {
        return new RemoteCall<RepTransactionReceipt>(new Callable<RepTransactionReceipt>() {
            @Override
            public RepTransactionReceipt call() throws Exception {
                return Contract.this.executeTransaction(function, weiValue);
            }
        });
    }

    private static <T extends Contract> T create(T contract, String binary, String encodedConstructor, BigInteger value) throws IOException, TransactionException {
        RepTransactionReceipt repTransactionReceipt = contract.executeTransaction(binary + encodedConstructor, value);

        String contractAddress = repTransactionReceipt.getContractAddress();
        if (contractAddress == null) {
            throw new RuntimeException("Empty contract address returned");
        }
        contract.setContractAddress(contractAddress);
        contract.setRepTransactionReceipt(repTransactionReceipt);

        return contract;
    }

    protected static <T extends Contract> T deploy(Class<T> type, Webuj web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String binary, String encodedConstructor, BigInteger value) throws IOException, TransactionException {

        try {
            Constructor<T> constructor = type.getDeclaredConstructor(String.class, Webuj.class, Credentials.class, BigInteger.class, BigInteger.class);
            constructor.setAccessible(true);

            // we want to use null here to ensure that "to" parameter on message is not populated
            T contract = constructor.newInstance(null, web3j, credentials, gasPrice, gasLimit);

            return create(contract, binary, encodedConstructor, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static <T extends Contract> T deploy(Class<T> type, Webuj web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String binary, String encodedConstructor, BigInteger value) throws IOException, TransactionException {

        try {
            Constructor<T> constructor = type.getDeclaredConstructor(String.class, Webuj.class, TransactionManager.class, BigInteger.class, BigInteger.class);
            constructor.setAccessible(true);

            // we want to use null here to ensure that "to" parameter on message is not populated
            T contract = constructor.newInstance(null, web3j, transactionManager, gasPrice, gasLimit);
            return create(contract, binary, encodedConstructor, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static <T extends Contract> RemoteCall<T> deployRemoteCall(final Class<T> type, final Webuj web3j, final Credentials credentials, final BigInteger gasPrice, final BigInteger gasLimit, final String binary, final String encodedConstructor, final BigInteger value) {
        return new RemoteCall<T>(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return deploy(type, web3j, credentials, gasPrice, gasLimit, binary, encodedConstructor, value);
            }
        });
    }

    protected static <T extends Contract> RemoteCall<T> deployRemoteCall(Class<T> type, Webuj web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String binary, String encodedConstructor) {
        return deployRemoteCall(type, web3j, credentials, gasPrice, gasLimit, binary, encodedConstructor, BigInteger.ZERO);
    }

    protected static <T extends Contract> RemoteCall<T> deployRemoteCall(final Class<T> type, final Webuj web3j, final TransactionManager transactionManager, final BigInteger gasPrice, final BigInteger gasLimit, final String binary, final String encodedConstructor, final BigInteger value) {
        return new RemoteCall<T>(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return deploy(type, web3j, transactionManager, gasPrice, gasLimit, binary, encodedConstructor, value);
            }
        });
    }

    protected static <T extends Contract> RemoteCall<T> deployRemoteCall(Class<T> type, Webuj web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String binary, String encodedConstructor) {
        return deployRemoteCall(type, web3j, transactionManager, gasPrice, gasLimit, binary, encodedConstructor, BigInteger.ZERO);
    }

    public static EventValues staticExtractEventParameters(Event event, Log log) {

        List<String> topics = log.getTopics();
        String encodedEventSignature = EventEncoder.encode(event);
        if (!topics.get(0).equals(encodedEventSignature)) {
            return null;
        }

        List<Type> indexedValues = new ArrayList<Type>();
        List<Type> nonIndexedValues = FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters());

        List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
        for (int i = 0; i < indexedParameters.size(); i++) {
            Type value = FunctionReturnDecoder.decodeIndexedValue(topics.get(i + 1), indexedParameters.get(i));
            indexedValues.add(value);
        }
        return new EventValues(indexedValues, nonIndexedValues);
    }

    protected EventValues extractEventParameters(Event event, Log log) {
        return staticExtractEventParameters(event, log);
    }

    protected List<EventValues> extractEventParameters(Event event, RepTransactionReceipt repTransactionReceipt) {

        List<Log> logs = repTransactionReceipt.getLogs();
        List<EventValues> values = new ArrayList<EventValues>();
        for (Log log : logs) {
            EventValues eventValues = extractEventParameters(event, log);
            if (eventValues != null) {
                values.add(eventValues);
            }
        }

        return values;
    }

    protected EventValuesWithLog extractEventParametersWithLog(Event event, Log log) {
        final EventValues eventValues = staticExtractEventParameters(event, log);
        return (eventValues == null) ? null : new EventValuesWithLog(eventValues, log);
    }

    protected List<EventValuesWithLog> extractEventParametersWithLog(Event event, RepTransactionReceipt repTransactionReceipt) {

        List<Log> logs = repTransactionReceipt.getLogs();
        List<EventValuesWithLog> values = new ArrayList<EventValuesWithLog>();
        for (Log log : logs) {
            EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
            if (eventValues != null) {
                values.add(eventValues);
            }
        }

        return values;
    }

    /**
     * Subclasses should implement this method to return pre-existing addresses for deployed
     * contracts.
     *
     * @param networkId the network id, for example "1" for the main-net, "3" for ropsten, etc.
     * @return the deployed address of the contract, if known, and null otherwise.
     */
    protected String getStaticDeployedAddress(String networkId) {
        return null;
    }

    public final void setDeployedAddress(String networkId, String address) {
        if (deployedAddresses == null) {
            deployedAddresses = new HashMap<>();
        }
        deployedAddresses.put(networkId, address);
    }

    public final String getDeployedAddress(String networkId) {
        String addr = null;
        if (deployedAddresses != null) {
            addr = deployedAddresses.get(networkId);
        }
        return addr == null ? getStaticDeployedAddress(networkId) : addr;
    }

    /**
     * Adds a log field to {@link EventValues}.
     */
    public static class EventValuesWithLog {
        private final EventValues eventValues;
        private final Log log;

        private EventValuesWithLog(EventValues eventValues, Log log) {
            this.eventValues = eventValues;
            this.log = log;
        }

        public List<Type> getIndexedValues() {
            return eventValues.getIndexedValues();
        }

        public List<Type> getNonIndexedValues() {
            return eventValues.getNonIndexedValues();
        }

        public Log getLog() {
            return log;
        }
    }

    @SuppressWarnings("unchecked")
    protected static <S extends Type, T> List<T> convertToNative(List<S> arr) {
        List<T> out = new ArrayList<T>();
        for (Iterator<S> it = arr.iterator(); it.hasNext(); ) {
            out.add((T) it.next().getValue());
        }
        return out;
    }
}
