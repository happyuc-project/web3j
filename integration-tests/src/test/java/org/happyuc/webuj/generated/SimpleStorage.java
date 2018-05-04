package org.happyuc.webuj.generated;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.happyuc.webuj.abi.TypeReference;
import org.happyuc.webuj.abi.datatypes.Function;
import org.happyuc.webuj.abi.datatypes.Type;
import org.happyuc.webuj.abi.datatypes.generated.Uint256;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.protocol.webuj;
import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.protocol.core.methods.response.TransactionReceipt;
import org.happyuc.webuj.tx.Contract;
import org.happyuc.webuj.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.webuj.io/command_line.html">webuj command line tools</a>,
 * or the org.happyuc.webuj.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/happyuc-project/webu.java/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with webuj version 3.3.1.
 */
public class SimpleStorage extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b600560005560bb806100226000396000f30060606040526004361060485763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166360fe47b18114604d5780636d4ce63c146062575b600080fd5b3415605757600080fd5b60606004356084565b005b3415606c57600080fd5b60726089565b60405190815260200160405180910390f35b600055565b600054905600a165627a7a72305820430774b8a91f549acbaaace2b6ac7d17f15706f8ac365056ab9775a9d20e1be40029";

    protected SimpleStorage(String contractAddress, webuj webuj, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, webuj, credentials, gasPrice, gasLimit);
    }

    protected SimpleStorage(String contractAddress, webuj webuj, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, webuj, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> set(BigInteger x) {
        final Function function = new Function(
                "set", 
                Arrays.<Type>asList(new org.happyuc.webuj.abi.datatypes.generated.Uint256(x)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> get() {
        final Function function = new Function("get", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<SimpleStorage> deploy(webuj webuj, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SimpleStorage.class, webuj, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<SimpleStorage> deploy(webuj webuj, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SimpleStorage.class, webuj, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static SimpleStorage load(String contractAddress, webuj webuj, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimpleStorage(contractAddress, webuj, credentials, gasPrice, gasLimit);
    }

    public static SimpleStorage load(String contractAddress, webuj webuj, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimpleStorage(contractAddress, webuj, transactionManager, gasPrice, gasLimit);
    }
}
