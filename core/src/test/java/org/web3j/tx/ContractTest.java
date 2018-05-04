package org.happyuc.webuj.tx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;

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
import org.happyuc.webuj.protocol.webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.Response;
import org.happyuc.webuj.protocol.core.methods.request.Transaction;
import org.happyuc.webuj.protocol.core.methods.response.HucCall;
import org.happyuc.webuj.protocol.core.methods.response.HucGetCode;
import org.happyuc.webuj.protocol.core.methods.response.HucGetTransactionReceipt;
import org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction;
import org.happyuc.webuj.protocol.core.methods.response.Log;
import org.happyuc.webuj.protocol.core.methods.response.TransactionReceipt;
import org.happyuc.webuj.protocol.exceptions.TransactionException;
import org.happyuc.webuj.utils.Async;
import org.happyuc.webuj.utils.Numeric;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContractTest extends ManagedTransactionTester {

    private static final String TEST_CONTRACT_BINARY = "12345";

    private TestContract contract;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        super.setUp();

        contract = new TestContract(
                ADDRESS, webuj, SampleKeys.CREDENTIALS,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);
    }

    @Test
    public void testGetContractAddress() {
        assertThat(contract.getContractAddress(), is(ADDRESS));
    }

    @Test
    public void testGetContractTransactionReceipt() {
        assertFalse(contract.getTransactionReceipt().isPresent());
    }

    @Test
    public void testDeploy() throws Exception {
        TransactionReceipt transactionReceipt = createTransactionReceipt();
        Contract deployedContract = deployContract(transactionReceipt);

        assertThat(deployedContract.getContractAddress(), is(ADDRESS));
        assertTrue(deployedContract.getTransactionReceipt().isPresent());
        assertThat(deployedContract.getTransactionReceipt().get(), equalTo(transactionReceipt));
    }

    @Test
    public void testContractDeployFails() throws Exception {
        thrown.expect(TransactionException.class);
        thrown.expectMessage(
                "Transaction has failed with status: 0x0. Gas used: 1. (not-enough gas?)");
        TransactionReceipt transactionReceipt = createFailedTransactionReceipt();
        deployContract(transactionReceipt);
    }

    @Test
    public void testContractDeployWithNullStatusSucceeds() throws Exception {
        TransactionReceipt transactionReceipt = createTransactionReceiptWithStatus(null);
        Contract deployedContract = deployContract(transactionReceipt);

        assertThat(deployedContract.getContractAddress(), is(ADDRESS));
        assertTrue(deployedContract.getTransactionReceipt().isPresent());
        assertThat(deployedContract.getTransactionReceipt().get(), equalTo(transactionReceipt));
    }

    @Test
    public void testIsValid() throws Exception {
        prepareHucGetCode(TEST_CONTRACT_BINARY);

        Contract contract = deployContract(createTransactionReceipt());
        assertTrue(contract.isValid());
    }

    @Test
    public void testIsValidDifferentCode() throws Exception {
        prepareHucGetCode(TEST_CONTRACT_BINARY + "0");

        Contract contract = deployContract(createTransactionReceipt());
        assertFalse(contract.isValid());
    }

    @Test
    public void testIsValidEmptyCode() throws Exception {
        prepareHucGetCode("");

        Contract contract = deployContract(createTransactionReceipt());
        assertFalse(contract.isValid());
    }

    @Test(expected = RuntimeException.class)
    public void testDeployInvalidContractAddress() throws Throwable {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);

        prepareTransaction(transactionReceipt);

        String encodedConstructor = FunctionEncoder.encodeConstructor(
                Arrays.<Type>asList(new Uint256(BigInteger.TEN)));

        try {
            TestContract.deployRemoteCall(
                    TestContract.class, webuj, SampleKeys.CREDENTIALS,
                    ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,
                    "0xcafed00d", encodedConstructor, BigInteger.ZERO).send();
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
        hucCall.setResult("0x0000000000000000000000000000000000000000000000000000000000000020"
                + "0000000000000000000000000000000000000000000000000000000000000000");
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
        hucCall.setResult("0x0000000000000000000000000000000000000000000000000000000000000037"
                + "0000000000000000000000000000000000000000000000000000000000000007");
        prepareCall(hucCall);

        assertThat(contract.callMultipleValue().send(),
                equalTo(Arrays.asList(
                        new Uint256(BigInteger.valueOf(55)),
                        new Uint256(BigInteger.valueOf(7)))));
    }

    @Test
    public void testCallMultipleValueEmpty() throws Exception {
        HucCall hucCall = new HucCall();
        hucCall.setResult("0x");
        prepareCall(hucCall);

        assertThat(contract.callMultipleValue().send(),
                equalTo(emptyList()));
    }

    @SuppressWarnings("unchecked")
    private void prepareCall(HucCall hucCall) throws IOException {
        Request<?, HucCall> request = mock(Request.class);
        when(request.send()).thenReturn(hucCall);

        when(webuj.hucCall(any(Transaction.class), eq(DefaultBlockParameterName.LATEST)))
                .thenReturn((Request) request);
    }

    @Test
    public void testTransaction() throws Exception {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);
        transactionReceipt.setStatus("0x1");

        prepareTransaction(transactionReceipt);

        assertThat(contract.performTransaction(
                new Address(BigInteger.TEN), new Uint256(BigInteger.ONE)).send(),
                is(transactionReceipt));
    }

    @Test
    public void testTransactionFailed() throws Exception {
        thrown.expect(TransactionException.class);
        thrown.expectMessage(
                "Transaction has failed with status: 0x0. Gas used: 1. (not-enough gas?)");

        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);
        transactionReceipt.setStatus("0x0");
        transactionReceipt.setGasUsed("0x1");

        prepareTransaction(transactionReceipt);
        contract.performTransaction(
                new Address(BigInteger.TEN), new Uint256(BigInteger.ONE)).send();
    }

    @Test
    public void testProcessEvent() {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        Log log = new Log();
        log.setTopics(Arrays.asList(
                // encoded function
                "0xfceb437c298f40d64702ac26411b2316e79f3c28ffa60edfc891ad4fc8ab82ca",
                // indexed value
                "0000000000000000000000003d6cb163f7c72d20b0fcd6baae5889329d138a4a"));
        // non-indexed value
        log.setData("0000000000000000000000000000000000000000000000000000000000000001");

        transactionReceipt.setLogs(Arrays.asList(log));

        EventValues eventValues = contract.processEvent(transactionReceipt).get(0);

        assertThat(eventValues.getIndexedValues(),
                equalTo(singletonList(
                        new Address("0x3d6cb163f7c72d20b0fcd6baae5889329d138a4a"))));
        assertThat(eventValues.getNonIndexedValues(),
                equalTo(singletonList(new Uint256(BigInteger.ONE))));
    }

    @Test(expected = TransactionException.class)
    public void testTimeout() throws Throwable {
        prepareTransaction(null);

        TransactionManager transactionManager = new RawTransactionManager(
                webuj, SampleKeys.CREDENTIALS, 1, 1);

        contract = new TestContract(
                ADDRESS, webuj, transactionManager,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT);

        testErrorScenario();
    }

    @Test(expected = RuntimeException.class)
    @SuppressWarnings("unchecked")
    public void testInvalidTransactionResponse() throws Throwable {
        prepareNonceRequest();

        HucSendTransaction hucSendTransaction = new HucSendTransaction();
        hucSendTransaction.setError(new Response.Error(1, "Invalid transaction"));

        Request<?, HucSendTransaction> rawTransactionRequest = mock(Request.class);
        when(rawTransactionRequest.sendAsync()).thenReturn(Async.run(() -> hucSendTransaction));
        when(webuj.hucSendRawTransaction(any(String.class)))
                .thenReturn((Request) rawTransactionRequest);

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
        assertEquals(ManagedTransaction.GAS_PRICE, contract.getGasPrice());
        BigInteger newPrice = ManagedTransaction.GAS_PRICE.multiply(BigInteger.valueOf(2));
        contract.setGasPrice(newPrice);
        assertEquals(newPrice, contract.getGasPrice());
    }

    @Test(expected = RuntimeException.class)
    @SuppressWarnings("unchecked")
    public void testInvalidTransactionReceipt() throws Throwable {
        prepareNonceRequest();
        prepareTransactionRequest();

        HucGetTransactionReceipt hucGetTransactionReceipt = new HucGetTransactionReceipt();
        hucGetTransactionReceipt.setError(new Response.Error(1, "Invalid transaction receipt"));

        Request<?, HucGetTransactionReceipt> getTransactionReceiptRequest = mock(Request.class);
        when(getTransactionReceiptRequest.sendAsync())
                .thenReturn(Async.run(() -> hucGetTransactionReceipt));
        when(webuj.hucGetTransactionReceipt(TRANSACTION_HASH))
                .thenReturn((Request) getTransactionReceiptRequest);

        testErrorScenario();
    }

    @Test
    public void testExtractEventParametersWithLogGivenATransactionReceipt() {

        final java.util.function.Function<String, Event> eventFactory = name ->
                new Event(name, emptyList(), emptyList());

        final BiFunction<Integer, Event, Log> logFactory = (logIndex, event) ->
                new Log(false, "" + logIndex, "0", "0x0", "0x0", "0", "0x" + logIndex, "", "",
                        singletonList(EventEncoder.encode(event)));

        final Event testEvent1 = eventFactory.apply("TestEvent1");
        final Event testEvent2 = eventFactory.apply("TestEvent2");

        final List<Log> logs = Arrays.asList(
                logFactory.apply(0, testEvent1),
                logFactory.apply(1, testEvent2)
        );

        final TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setLogs(logs);

        final List<Contract.EventValuesWithLog> eventValuesWithLogs1 =
                contract.extractEventParametersWithLog(testEvent1, transactionReceipt);

        assertEquals(eventValuesWithLogs1.size(), 1);
        assertEquals(eventValuesWithLogs1.get(0).getLog(), logs.get(0));

        final List<Contract.EventValuesWithLog> eventValuesWithLogs2 =
                contract.extractEventParametersWithLog(testEvent2, transactionReceipt);

        assertEquals(eventValuesWithLogs2.size(), 1);
        assertEquals(eventValuesWithLogs2.get(0).getLog(), logs.get(1));
    }

    void testErrorScenario() throws Throwable {
        try {
            contract.performTransaction(
                    new Address(BigInteger.TEN), new Uint256(BigInteger.ONE)).send();
        } catch (InterruptedException e) {
            throw e;
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

    private TransactionReceipt createTransactionReceipt() {
        return createTransactionReceiptWithStatus("0x1");
    }

    private TransactionReceipt createFailedTransactionReceipt() {
        return createTransactionReceiptWithStatus("0x0");
    }

    private TransactionReceipt createTransactionReceiptWithStatus(String status) {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);
        transactionReceipt.setContractAddress(ADDRESS);
        transactionReceipt.setStatus(status);
        transactionReceipt.setGasUsed("0x1");
        return transactionReceipt;
    }

    private Contract deployContract(TransactionReceipt transactionReceipt)
            throws Exception {

        prepareTransaction(transactionReceipt);

        String encodedConstructor = FunctionEncoder.encodeConstructor(
                Arrays.<Type>asList(new Uint256(BigInteger.TEN)));

        return TestContract.deployRemoteCall(
                TestContract.class, webuj, SampleKeys.CREDENTIALS,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,
                "0xcafed00d", encodedConstructor, BigInteger.ZERO).send();
    }

    @SuppressWarnings("unchecked")
    private void prepareHucGetCode(String binary) throws IOException {
        HucGetCode hucGetCode = new HucGetCode();
        hucGetCode.setResult(Numeric.prependHexPrefix(binary));

        Request<?, HucGetCode> hucGetCodeRequest = mock(Request.class);
        when(hucGetCodeRequest.send())
                .thenReturn(hucGetCode);
        when(webuj.hucGetCode(ADDRESS, DefaultBlockParameterName.LATEST))
                .thenReturn((Request) hucGetCodeRequest);
    }

    private static class TestContract extends Contract {
        public TestContract(
                String contractAddress, webuj webuj, Credentials credentials,
                BigInteger gasPrice, BigInteger gasLimit) {
            super(TEST_CONTRACT_BINARY, contractAddress, webuj, credentials, gasPrice, gasLimit);
        }

        public TestContract(
                String contractAddress,
                webuj webuj, TransactionManager transactionManager,
                BigInteger gasPrice, BigInteger gasLimit) {
            super(TEST_CONTRACT_BINARY, contractAddress, webuj, transactionManager, gasPrice,
                    gasLimit);
        }

        public RemoteCall<Utf8String> callSingleValue() {
            Function function = new Function("call",
                    Arrays.<Type>asList(),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                    }));
            return executeRemoteCallSingleValueReturn(function);
        }

        public RemoteCall<List<Type>> callMultipleValue()
                throws ExecutionException, InterruptedException {
            Function function = new Function("call",
                    Arrays.<Type>asList(),
                    Arrays.<TypeReference<?>>asList(
                            new TypeReference<Uint256>() { },
                            new TypeReference<Uint256>() { }));
            return executeRemoteCallMultipleValueReturn(function);
        }

        public RemoteCall<TransactionReceipt> performTransaction(
                Address address, Uint256 amount) {
            Function function = new Function("approve",
                    Arrays.<Type>asList(address, amount),
                    Collections.<TypeReference<?>>emptyList());
            return executeRemoteCallTransaction(function);
        }

        public List<EventValues> processEvent(TransactionReceipt transactionReceipt) {
            Event event = new Event("Event",
                    Arrays.<TypeReference<?>>asList(new TypeReference<Address>() { }),
                    Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() { }));
            return extractEventParameters(event, transactionReceipt);
        }
    }
}
