package org.happyuc.webuj.tx;

import org.hamcrest.CoreMatchers;
import org.happyuc.webuj.abi.EventEncoder;
import org.happyuc.webuj.abi.EventValues;
import org.happyuc.webuj.abi.FunctionEncoder;
import org.happyuc.webuj.abi.TypeReference;
import org.happyuc.webuj.abi.datatypes.Address;
import org.happyuc.webuj.abi.datatypes.Event;
import org.happyuc.webuj.abi.datatypes.Function;
import org.happyuc.webuj.abi.datatypes.Type;
import org.happyuc.webuj.abi.datatypes.Utf8String;
import org.happyuc.webuj.abi.datatypes.generated.Uint256;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.crypto.SampleKeys;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.Response;
import org.happyuc.webuj.protocol.core.methods.request.ReqTransaction;
import org.happyuc.webuj.protocol.core.methods.response.EthCall;
import org.happyuc.webuj.protocol.core.methods.response.HucGetCode;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionReceipt;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;
import org.happyuc.webuj.protocol.core.methods.response.HucCall;
import org.happyuc.webuj.protocol.core.methods.response.Log;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.protocol.exceptions.TransactionException;
import org.happyuc.webuj.utils.Async;
import org.happyuc.webuj.utils.Numeric;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContractTest extends ManagedReqRepTransactionTester {

    private static final String TEST_CONTRACT_BINARY = "12345";

    private TestContract contract;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        contract = new TestContract(ADDRESS, webuj, SampleKeys.CREDENTIALS, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
    }

    @Test
    public void testGetContractAddress() {
        assertThat(contract.getContractAddress(), is(ADDRESS));
    }

    @Test
    public void testGetContractTransactionReceipt() {
        assertNull(contract.getRepTransactionReceipt());
    }

    @Test
    public void testDeploy() throws Exception {
        RepTransactionReceipt repTransactionReceipt = createTransactionReceipt();
        Contract deployedContract = deployContract(repTransactionReceipt);

        assertThat(deployedContract.getContractAddress(), is(ADDRESS));
        assertNotNull(deployedContract.getRepTransactionReceipt());
        assertThat(deployedContract.getRepTransactionReceipt(), equalTo(repTransactionReceipt));
    }

    private RepTransactionReceipt createTransactionReceipt() {
        RepTransactionReceipt repTransactionReceipt = new RepTransactionReceipt();
        repTransactionReceipt.setTransactionHash(TRANSACTION_HASH);
        repTransactionReceipt.setContractAddress(ADDRESS);
        return repTransactionReceipt;
    }

    @Test
    public void testIsValid() throws Exception {
        prepareEthGetCode(TEST_CONTRACT_BINARY);

        Contract contract = deployContract(createTransactionReceipt());
        assertTrue(contract.isValid());
    }

    @Test
    public void testIsValidDifferentCode() throws Exception {
        prepareEthGetCode(TEST_CONTRACT_BINARY + "0");

        Contract contract = deployContract(createTransactionReceipt());
        assertFalse(contract.isValid());
    }

    @Test
    public void testIsValidEmptyCode() throws Exception {
        prepareEthGetCode("");

        Contract contract = deployContract(createTransactionReceipt());
        assertFalse(contract.isValid());
    }

    @Test(expected = RuntimeException.class)
    public void testDeployInvalidContractAddress() throws Throwable {
        RepTransactionReceipt repTransactionReceipt = new RepTransactionReceipt();
        repTransactionReceipt.setTransactionHash(TRANSACTION_HASH);

        prepareTransaction(repTransactionReceipt);

        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(BigInteger.TEN)));

        try {
            TestContract.deployRemoteCall(TestContract.class, webuj, SampleKeys.CREDENTIALS, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT, "0xcafed00d", encodedConstructor, BigInteger.ZERO).send();
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    @Test
    public void testCallSingleValue() throws Exception {
        // Example taken from FunctionReturnDecoderTest

        HucCall hucCall = new HucCall();
        hucCall.setResult("0x0000000000000000000000000000000000000000000000000000000000000020" + "0000000000000000000000000000000000000000000000000000000000000000");
        prepareCall(hucCall);

        assertThat(contract.callSingleValue().send(), equalTo(new Utf8String("")));
    }

    @Test
    public void testCallSingleValueEmpty() throws Exception {
        // Example taken from FunctionReturnDecoderTest

        HucCall hucCall = new HucCall();
        hucCall.setResult("0x");
        prepareCall(hucCall);

        assertNull(contract.callSingleValue().send());
    }

    @Test
    public void testCallMultipleValue() throws Exception {
        HucCall hucCall = new HucCall();
        hucCall.setResult("0x0000000000000000000000000000000000000000000000000000000000000037" + "0000000000000000000000000000000000000000000000000000000000000007");
        prepareCall(hucCall);

        assertThat(contract.callMultipleValue().send(), equalTo(Arrays.<Type>asList(new Uint256(BigInteger.valueOf(55)), new Uint256(BigInteger.valueOf(7)))));
    }

    @Test
    public void testCallMultipleValueEmpty() throws Exception {
        HucCall hucCall = new HucCall();
        hucCall.setResult("0x");
        prepareCall(hucCall);

        assertThat(contract.callMultipleValue().send(), CoreMatchers.equalTo(Collections.<Type>emptyList()));
    }

    @SuppressWarnings("unchecked")
    private void prepareCall(HucCall hucCall) throws IOException {
        Request<?, HucCall> request = mock(Request.class);
        when(request.send()).thenReturn(hucCall);

        when(webuj.hucCall(any(ReqTransaction.class), eq(DefaultBlockParameterName.LATEST))).thenReturn((Request) request);
    }

    @Test
    public void testTransaction() throws Exception {
        RepTransactionReceipt repTransactionReceipt = new RepTransactionReceipt();
        repTransactionReceipt.setTransactionHash(TRANSACTION_HASH);

        prepareTransaction(repTransactionReceipt);

        assertThat(contract.performTransaction(new Address(BigInteger.TEN), new Uint256(BigInteger.ONE)).send(), is(repTransactionReceipt));
    }

    @Test
    public void testProcessEvent() {
        RepTransactionReceipt repTransactionReceipt = new RepTransactionReceipt();
        Log log = new Log();
        log.setTopics(Arrays.asList(
                // encoded function
                "0xfceb437c298f40d64702ac26411b2316e79f3c28ffa60edfc891ad4fc8ab82ca",
                // indexed value
                "0000000000000000000000003d6cb163f7c72d20b0fcd6baae5889329d138a4a"));
        // non-indexed value
        log.setData("0000000000000000000000000000000000000000000000000000000000000001");

        repTransactionReceipt.setLogs(Arrays.asList(log));

        EventValues eventValues = contract.processEvent(repTransactionReceipt).get(0);

        assertThat(eventValues.getIndexedValues(), equalTo(Collections.<Type>singletonList(new Address("0x3d6cb163f7c72d20b0fcd6baae5889329d138a4a"))));
        assertThat(eventValues.getNonIndexedValues(), equalTo(Collections.<Type>singletonList(new Uint256(BigInteger.ONE))));
    }

    @Test(expected = TransactionException.class)
    public void testTimeout() throws Throwable {
        prepareTransaction(null);

        TransactionManager transactionManager = new RawTransactionManager(webuj, SampleKeys.CREDENTIALS, 1, 1);

        contract = new TestContract(ADDRESS, webuj, transactionManager, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);

        testErrorScenario();
    }

    @Test(expected = RuntimeException.class)
    @SuppressWarnings("unchecked")
    public void testInvalidTransactionResponse() throws Throwable {
        prepareNonceRequest();

        final HucSendRepTransaction hucSendRepTransaction = new HucSendRepTransaction();
        hucSendRepTransaction.setError(new Response.Error(1, "Invalid transaction"));

        Request rawTransactionRequest = mock(Request.class);
        when(rawTransactionRequest.sendAsync()).thenReturn(Async.run(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return hucSendRepTransaction;
            }
        }));
        when(webuj.hucSendRawTransaction(any(String.class))).thenReturn((Request) rawTransactionRequest);

        testErrorScenario();
    }

    @Test
    public void testSetGetAddresses() throws Exception {
        assertNull(contract.getDeployedAddress("1"));
        contract.setDeployedAddress("1", "0x000000000000add0e00000000000");
        assertNotNull(contract.getDeployedAddress("1"));
        contract.setDeployedAddress("2", "0x000000000000add0e00000000000");
        assertNotNull(contract.getDeployedAddress("2"));
    }

    @Test
    public void testSetGetGasPrice() {
        assertThat(ManagedTransaction.GAS_PRICE, equalTo(contract.getGasPrice()));
        BigInteger newPrice = ManagedTransaction.GAS_PRICE.multiply(BigInteger.valueOf(2));
        contract.setGasPrice(newPrice);
        assertThat(newPrice, equalTo(contract.getGasPrice()));
    }

    @Test(expected = RuntimeException.class)
    @SuppressWarnings("unchecked")
    public void testInvalidTransactionReceipt() throws Throwable {
        prepareNonceRequest();
        prepareTransactionRequest();

        final HucGetRepTransactionReceipt hucGetRepTransactionReceipt = new HucGetRepTransactionReceipt();
        hucGetRepTransactionReceipt.setError(new Response.Error(1, "Invalid transaction receipt"));

        Request<?, HucGetRepTransactionReceipt> getTransactionReceiptRequest = mock(Request.class);
        when(getTransactionReceiptRequest.sendAsync()).thenReturn(Async.run(new Callable<HucGetRepTransactionReceipt>() {
            @Override
            public HucGetRepTransactionReceipt call() throws Exception {
                return hucGetRepTransactionReceipt;
            }
        }));
        when(webuj.hucGetTransactionReceipt(TRANSACTION_HASH)).thenReturn((Request) getTransactionReceiptRequest);

        testErrorScenario();
    }

    @Test
    public void testExtractEventParametersWithLogGivenATransactionReceipt() {

        final Event testEvent1 = new Event("TestEvent1", Collections.<TypeReference<?>>emptyList(), Collections.<TypeReference<?>>emptyList());

        final Event testEvent2 = new Event("TestEvent2", Collections.<TypeReference<?>>emptyList(), Collections.<TypeReference<?>>emptyList());

        final List<Log> logs = Arrays.asList(new Log(false, "" + 0, "0", "0x0", "0x0", "0", "0x1", "", "", singletonList(EventEncoder.encode(testEvent1))), new Log(false, "" + 0, "0", "0x0", "0x0", "0", "0x2", "", "", singletonList(EventEncoder.encode(testEvent2))));

        final RepTransactionReceipt repTransactionReceipt = new RepTransactionReceipt();
        repTransactionReceipt.setLogs(logs);

        final List<Contract.EventValuesWithLog> eventValuesWithLogs1 = contract.extractEventParametersWithLog(testEvent1, repTransactionReceipt);

        assertThat(eventValuesWithLogs1.size(), equalTo(1));
        assertThat(eventValuesWithLogs1.get(0).getLog(), equalTo(logs.get(0)));

        final List<Contract.EventValuesWithLog> eventValuesWithLogs2 = contract.extractEventParametersWithLog(testEvent2, repTransactionReceipt);

        assertThat(eventValuesWithLogs2.size(), equalTo(1));
        assertThat(eventValuesWithLogs2.get(0).getLog(), equalTo(logs.get(1)));
    }

    void testErrorScenario() throws Throwable {
        try {
            contract.performTransaction(new Address(BigInteger.TEN), new Uint256(BigInteger.ONE)).send();
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    private Contract deployContract(RepTransactionReceipt repTransactionReceipt) throws Exception {

        prepareTransaction(repTransactionReceipt);

        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(BigInteger.TEN)));

        return TestContract.deployRemoteCall(TestContract.class, webuj, SampleKeys.CREDENTIALS, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT, "0xcafed00d", encodedConstructor, BigInteger.ZERO).send();
    }

    @SuppressWarnings("unchecked")
    private void prepareEthGetCode(String binary) throws IOException {
        HucGetCode hucGetCode = new HucGetCode();
        hucGetCode.setResult(Numeric.prependHexPrefix(binary));

        Request<?, HucGetCode> ethGetCodeRequest = mock(Request.class);
        when(ethGetCodeRequest.send()).thenReturn(hucGetCode);
        when(webuj.hucGetCode(ADDRESS, DefaultBlockParameterName.LATEST)).thenReturn((Request) ethGetCodeRequest);
    }

    private static class TestContract extends Contract {
        public TestContract(String contractAddress, Webuj web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
            super(TEST_CONTRACT_BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
        }

        public TestContract(String contractAddress, Webuj web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
            super(TEST_CONTRACT_BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
        }

        public RemoteCall<Utf8String> callSingleValue() {
            Function function = new Function("call", Arrays.<Type>asList(), Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
            return executeRemoteCallSingleValueReturn(function);
        }

        public RemoteCall<List<Type>> callMultipleValue() throws ExecutionException, InterruptedException {
            Function function = new Function("call", Arrays.<Type>asList(), Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
            return executeRemoteCallMultipleValueReturn(function);
        }

        public RemoteCall<RepTransactionReceipt> performTransaction(Address address, Uint256 amount) {
            Function function = new Function("approve", Arrays.<Type>asList(address, amount), Collections.<TypeReference<?>>emptyList());
            return executeRemoteCallTransaction(function);
        }

        public List<EventValues> processEvent(RepTransactionReceipt repTransactionReceipt) {
            Event event = new Event("Event", Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}), Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
            return extractEventParameters(event, repTransactionReceipt);
        }
    }
}
