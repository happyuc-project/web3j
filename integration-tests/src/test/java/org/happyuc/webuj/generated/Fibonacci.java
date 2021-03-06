package org.happyuc.webuj.generated;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.happyuc.webuj.abi.EventEncoder;
import org.happyuc.webuj.abi.TypeReference;
import org.happyuc.webuj.abi.datatypes.Event;
import org.happyuc.webuj.abi.datatypes.Function;
import org.happyuc.webuj.abi.datatypes.generated.Uint256;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameter;
import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.protocol.core.methods.request.HucReqFilter;
import org.happyuc.webuj.protocol.core.methods.response.Log;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.tx.Contract;
import org.happyuc.webuj.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.webuj.io/command_line.html">webuj command line tools</a>,
 * or the org.happyuc.webuj.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/happyuc-project/webu.java/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with webuj version 3.3.1.
 */
public class Fibonacci extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6101498061001e6000396000f30060606040526004361061004b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416633c7fdc70811461005057806361047ff414610078575b600080fd5b341561005b57600080fd5b61006660043561008e565b60405190815260200160405180910390f35b341561008357600080fd5b6100666004356100da565b6000610099826100da565b90507f71e71a8458267085d5ab16980fd5f114d2d37f232479c245d523ce8d23ca40ed828260405191825260208201526040908101905180910390a1919050565b60008115156100eb57506000610118565b81600114156100fc57506001610118565b610108600283036100da565b610114600184036100da565b0190505b9190505600a165627a7a72305820b79593e85095f5c09ecad21be0e9501a5528960f09cf22e8543ff6221976ea8e0029";

    public static final Event NOTIFY_EVENT = new Event("Notify", Arrays.asList(), Arrays.asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));

    protected Fibonacci(String contractAddress, Webuj webuj, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, webuj, credentials, gasPrice, gasLimit);
    }

    protected Fibonacci(String contractAddress, Webuj webuj, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, webuj, transactionManager, gasPrice, gasLimit);
    }

    public List<NotifyEventResponse> getNotifyEvents(RepTransactionReceipt repTransactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NOTIFY_EVENT, repTransactionReceipt);
        ArrayList<NotifyEventResponse> responses = new ArrayList<NotifyEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NotifyEventResponse typedResponse = new NotifyEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.input = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.result = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<NotifyEventResponse> notifyEventObservable(HucReqFilter filter) {
        return webuj.hucLogObservable(filter).map(new Func1<Log, NotifyEventResponse>() {
            @Override
            public NotifyEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NOTIFY_EVENT, log);
                NotifyEventResponse typedResponse = new NotifyEventResponse();
                typedResponse.log = log;
                typedResponse.input = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.result = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<NotifyEventResponse> notifyEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        HucReqFilter filter = new HucReqFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NOTIFY_EVENT));
        return notifyEventObservable(filter);
    }

    public RemoteCall<RepTransactionReceipt> fibonacciNotify(BigInteger number) {
        final Function function = new Function("fibonacciNotify", Arrays.asList(new org.happyuc.webuj.abi.datatypes.generated.Uint256(number)), Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> fibonacci(BigInteger number) {
        final Function function = new Function("fibonacci", Arrays.asList(new org.happyuc.webuj.abi.datatypes.generated.Uint256(number)), Arrays.asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<Fibonacci> deploy(Webuj webuj, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Fibonacci.class, webuj, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Fibonacci> deploy(Webuj webuj, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Fibonacci.class, webuj, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Fibonacci load(String contractAddress, Webuj webuj, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Fibonacci(contractAddress, webuj, credentials, gasPrice, gasLimit);
    }

    public static Fibonacci load(String contractAddress, Webuj webuj, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Fibonacci(contractAddress, webuj, transactionManager, gasPrice, gasLimit);
    }

    public static class NotifyEventResponse {
        public Log log;

        public BigInteger input;

        public BigInteger result;
    }
}
