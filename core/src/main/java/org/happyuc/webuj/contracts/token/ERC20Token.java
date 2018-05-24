package org.happyuc.webuj.contracts.token;

import org.happyuc.webuj.abi.EventEncoder;
import org.happyuc.webuj.abi.FunctionEncoder;
import org.happyuc.webuj.abi.TypeReference;
import org.happyuc.webuj.abi.datatypes.Address;
import org.happyuc.webuj.abi.datatypes.DynamicBytes;
import org.happyuc.webuj.abi.datatypes.Event;
import org.happyuc.webuj.abi.datatypes.Function;
import org.happyuc.webuj.abi.datatypes.Type;
import org.happyuc.webuj.abi.datatypes.Utf8String;
import org.happyuc.webuj.abi.datatypes.generated.Uint256;
import org.happyuc.webuj.abi.datatypes.generated.Uint8;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameter;
import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.protocol.core.methods.request.HucReqFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.tx.Contract;
import org.happyuc.webuj.tx.RawTransactionManager;
import org.happyuc.webuj.tx.TransactionData;
import org.happyuc.webuj.tx.TransactionManager;
import org.happyuc.webuj.utils.Convert;
import rx.Observable;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ERC20Token extends Contract implements ERC20Interface {

    public static final List<TypeReference<?>> EMPTY_REFERENCES = Arrays.asList(
            new TypeReference<Address>() {},
            new TypeReference<Address>() {});

    public static final Event TRANSFER_EVENT = new Event(
            "Transfer",
            EMPTY_REFERENCES,
            Collections.singletonList(new TypeReference<Uint256>() {
            }));

    public static final Event APPROVAL_EVENT = new Event(
            "Approval",
            EMPTY_REFERENCES,
            Collections.singletonList(new TypeReference<Uint256>() {
            }));

    public ERC20Token(String contractAddress, Webuj webuj, TransactionManager transactionManager, BigInteger price, BigInteger limit) {
        super(null, contractAddress, webuj, transactionManager, price, limit);
    }


    /**
     * Obtain the balance of whos _owner in which Token
     *
     * @param _owner an Account or Wallet address
     * @return BigInteger
     */
    @Override
    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final List<Type> inputParam = Collections.singletonList(new Address(_owner));
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Uint256>() {});
        final Function function = new Function("balanceOf", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    /**
     * Transfer balance
     *
     * @param _to    the recipient _to rep whos you want to transfer
     * @param _value transfer balance value
     * @return RepTransactionReceipt
     */
    @Override
    public RemoteCall<RepTransactionReceipt> transfer(String _to, String _value, Convert.Unit unit, String _remark) {
        BigInteger weiValue = Convert.toWei(_value, unit).toBigInteger();
        final Function function = new Function("transfer", Arrays.asList(new Address(_to), new Uint256(weiValue)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public HucSendRepTransaction transfer2(String _to, String _value, Convert.Unit unit, String _remark) throws IOException {
        BigInteger weiValue = Convert.toWei(_value, unit).toBigInteger();
        final Function function = new Function("transfer", Arrays.asList(new Address(_to), new Uint256(weiValue)), Collections.emptyList());
        TransactionData txData = new TransactionData(contractAddress, FunctionEncoder.encode(function), BigInteger.ZERO, gasPrice, gasLimit);
        return transactionManager.makeReqTransaction(txData).send();
    }

    public List<RepTransactionReceipt> mulTransfer(List<RemoteCall<RepTransactionReceipt>> remotes) {
        return remotes
                .parallelStream()
                .map(remote -> {
                    try {
                        return remote.send();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * todo ???
     *
     * @param repTransactionReceipt the transaction result, like method `transfer`
     * @return List EventResponse.TransferEr
     */
    public List<EventResponse.TransferEr> simpleGetTxEvents(RepTransactionReceipt repTransactionReceipt) {
        return getTransferEvents(repTransactionReceipt, eventValues -> {
            EventResponse.TransferEr tEr = new EventResponse.TransferEr();
            tEr.log = eventValues.getLog();
            tEr._from = (String) eventValues.getIndexedValues().get(0).getValue();
            tEr._to = (String) eventValues.getIndexedValues().get(1).getValue();
            tEr._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            return tEr;
        });
    }

    @Override
    public <T> List<T> getTransferEvents(RepTransactionReceipt repTransactionReceipt, EventResponse.Rec<T> rec) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, repTransactionReceipt);
        ArrayList<T> responses = new ArrayList<>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            T t = rec.doLog(eventValues);
            responses.add(t);
        }
        return responses;
    }

    /**
     * todo ???
     *
     * @param startBlock todo ???
     * @param endBlock   todo ???
     * @return Observable EventResponse.TransferEr
     */
    public Observable<EventResponse.TransferEr> simpleTsEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return transferEventObservable(startBlock, endBlock, eventValues -> {
            EventResponse.TransferEr tEr = new EventResponse.TransferEr();
            tEr.log = eventValues.getLog();
            tEr._from = (String) eventValues.getIndexedValues().get(0).getValue();
            tEr._to = (String) eventValues.getIndexedValues().get(1).getValue();
            tEr._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            return tEr;
        });
    }

    @Override
    public <T> Observable<T> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, EventResponse.Rec<T> rec) {
        HucReqFilter filter = new HucReqFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventObservable(filter, rec);
    }

    private <T> Observable<T> transferEventObservable(HucReqFilter filter, EventResponse.Rec<T> rec) {
        return webuj.hucLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
            return rec.doLog(eventValues);
        });
    }

    @Override
    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        final List<Type> inputParam = Arrays.asList(new Address(_owner), new Address(_spender));
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Uint256>() {
        });
        final Function function = new Function("allowance", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Override
    public RemoteCall<RepTransactionReceipt> approve(String _spender, BigInteger _value) {
        final List<Type> inputParam = Arrays.asList(new Address(_spender), new Uint256(_value));
        final List<TypeReference<?>> outputParam = Collections.emptyList();
        final Function function = new Function("approve", inputParam, outputParam);
        return executeRemoteCallTransaction(function);
    }

    @Override
    public RemoteCall<RepTransactionReceipt> transferFrom(String _from, String _to, String _value, Convert.Unit unit, String remark) {
        BigInteger weiValue = Convert.toWei(_value, unit).toBigInteger();
        final List<Type> inputParam = Arrays.asList(new Address(_from), new Address(_to), new Uint256(weiValue));
        final List<TypeReference<?>> outputParam = Collections.emptyList();
        final Function function = new Function("transferFrom", inputParam, outputParam);
        return executeRemoteCallTransaction(function);
    }

    /**
     * todo ???
     *
     * @param repTransactionReceipt the transaction result, like method `transfer`
     * @return List EventResponse.ApprovalEr
     */
    public List<EventResponse.ApprovalEr> simpleGetapprEvents(RepTransactionReceipt repTransactionReceipt) {
        return getTransferEvents(repTransactionReceipt, eventValues -> {
            EventResponse.ApprovalEr aEr = new EventResponse.ApprovalEr();
            aEr.log = eventValues.getLog();
            aEr._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            aEr._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            aEr._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            return aEr;
        });
    }

    @Override
    public <T> List<T> getApprovalEvents(RepTransactionReceipt repTransactionReceipt, EventResponse.Rec<T> rec) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, repTransactionReceipt);
        ArrayList<T> responses = new ArrayList<>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            T t = rec.doLog(eventValues);
            responses.add(t);
        }
        return responses;
    }

    /**
     * todo ???
     *
     * @param startBlock todo ???
     * @param endBlock   todo ???
     * @return Observable EventResponse.ApprovalEr
     */
    public Observable<EventResponse.ApprovalEr> simpleAppEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return approvalEventObservable(startBlock, endBlock, eventValues -> {
            EventResponse.ApprovalEr aEr = new EventResponse.ApprovalEr();
            aEr.log = eventValues.getLog();
            aEr._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            aEr._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            aEr._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            return aEr;
        });
    }

    @Override
    public <T> Observable<T> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, EventResponse.Rec<T> rec) {
        HucReqFilter filter = new HucReqFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventObservable(filter, rec);
    }

    private <T> Observable<T> approvalEventObservable(HucReqFilter filter, EventResponse.Rec<T> rec) {
        return webuj.hucLogObservable(filter).map(log -> {
            EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
            return rec.doLog(eventValues);
        });
    }

    public RemoteCall<String> name() {
        final List<Type> inputParam = Collections.emptyList();
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Utf8String>() {
        });
        final Function function = new Function("name", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> symbol() {
        final List<Type> inputParam = Collections.emptyList();
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Utf8String>() {
        });
        final Function function = new Function("symbol", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> decimals() {
        final List<Type> inputParam = Collections.emptyList();
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Uint8>() {
        });
        final Function function = new Function("decimals", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    /**
     * Obtain the total circulation which Token
     *
     * @return BigInteger
     */
    @Override
    public RemoteCall<BigInteger> totalSupply() {
        final List<Type> inputParam = Collections.emptyList();
        final List<TypeReference<?>> outputParam = Collections.singletonList(new TypeReference<Uint256>() {
        });
        final Function function = new Function("totalSupply", inputParam, outputParam);
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<RepTransactionReceipt> approveAndCall(String _spender, BigInteger _value, byte[] _extraData) {
        final List<Type> inputParam = Arrays.asList(new Address(_spender), new Uint256(_value), new DynamicBytes(_extraData));
        final List<TypeReference<?>> outputParam = Collections.emptyList();
        final Function function = new Function("approveAndCall", inputParam, outputParam);
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<ERC20Token> deploy(Webuj webuj, Credentials credentials, BigInteger price, BigInteger limit, BigInteger amount, String name, String symbol) {
        List<Type> param = Arrays.asList(new Uint256(amount), new Utf8String(name), new Utf8String(symbol));
        String encodedConstructor = FunctionEncoder.encodeConstructor(param);
        return deployRemoteCall(ERC20Token.class, webuj, credentials, price, limit, null, encodedConstructor);
    }

    public static RemoteCall<ERC20Token> deploy(Webuj webuj, TransactionManager manager, BigInteger price, BigInteger limit, BigInteger amount, String name, String symbol) {
        List<Type> param = Arrays.asList(new Uint256(amount), new Utf8String(name), new Utf8String(symbol));
        String encodedConstructor = FunctionEncoder.encodeConstructor(param);
        return deployRemoteCall(ERC20Token.class, webuj, manager, price, limit, null, encodedConstructor);
    }

    public static ERC20Token load(String contractAddress, Webuj webuj, Credentials credentials) {
        return new ERC20Token(contractAddress, webuj, new RawTransactionManager(webuj, credentials), GAS_PRICE, GAS_LIMIT);
    }

    public static ERC20Token load(String contractAddress, Webuj webuj, TransactionManager transactionManager) {
        return new ERC20Token(contractAddress, webuj, transactionManager, GAS_PRICE, GAS_LIMIT);
    }

}
