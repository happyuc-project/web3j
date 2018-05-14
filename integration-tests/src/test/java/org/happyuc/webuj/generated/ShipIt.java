package org.happyuc.webuj.generated;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.happyuc.webuj.abi.TypeReference;
import org.happyuc.webuj.abi.datatypes.Address;
import org.happyuc.webuj.abi.datatypes.Function;
import org.happyuc.webuj.abi.datatypes.Type;
import org.happyuc.webuj.abi.datatypes.Utf8String;
import org.happyuc.webuj.abi.datatypes.generated.Bytes32;
import org.happyuc.webuj.abi.datatypes.generated.Uint256;
import org.happyuc.webuj.abi.datatypes.generated.Uint8;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.tuples.generated.Tuple8;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.tx.Contract;
import org.happyuc.webuj.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">webuj command line tools</a>,
 * or the org.happyuc.webuj.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with webuj version 3.3.0.
 */
public class ShipIt extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6101e48061001e6000396000f3006060604052600436106100405763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663d51cd4ac8114610045575b600080fd5b341561005057600080fd5b61007173ffffffffffffffffffffffffffffffffffffffff60043516610159565b60405173ffffffffffffffffffffffffffffffffffffffff808a168252881660208201526040810187905260608101869052608081018560008111156100b357fe5b60ff168152602081018590526060810183905260808282038101604083019081528554600260001960018316156101000201909116049183018290529160a00190859080156101435780601f1061011857610100808354040283529160200191610143565b820191906000526020600020905b81548152906001019060200180831161012657829003601f168201915b5050995050505050505050505060405180910390f35b6000602081905290815260409020805460018201546002830154600384015460048501546005860154600787015473ffffffffffffffffffffffffffffffffffffffff9687169796909516959394929360ff90921692909160060190885600a165627a7a72305820f4d4bf1c9ea57666a0a0e3a810321535fee9dd5f0809158d2f27a18e0a2df2560029";

    protected ShipIt(String contractAddress, Webuj web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ShipIt(String contractAddress, Webuj web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<Tuple8<String, String, BigInteger, BigInteger, BigInteger, BigInteger, String, byte[]>> shipments(String param0) {
        final Function function = new Function("shipments", Arrays.<Type>asList(new Address(param0)), Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bytes32>() {}));
        return new RemoteCall<Tuple8<String, String, BigInteger, BigInteger, BigInteger, BigInteger, String, byte[]>>(new Callable<Tuple8<String, String, BigInteger, BigInteger, BigInteger, BigInteger, String, byte[]>>() {
            @Override
            public Tuple8<String, String, BigInteger, BigInteger, BigInteger, BigInteger, String, byte[]> call() throws Exception {
                List<Type> results = executeCallMultipleValueReturn(function);
                return new Tuple8<String, String, BigInteger, BigInteger, BigInteger, BigInteger, String, byte[]>((String) results.get(0).getValue(), (String) results.get(1).getValue(), (BigInteger) results.get(2).getValue(), (BigInteger) results.get(3).getValue(), (BigInteger) results.get(4).getValue(), (BigInteger) results.get(5).getValue(), (String) results.get(6).getValue(), (byte[]) results.get(7).getValue());
            }
        });
    }

    public static RemoteCall<ShipIt> deploy(Webuj web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ShipIt.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ShipIt> deploy(Webuj web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ShipIt.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static ShipIt load(String contractAddress, Webuj web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ShipIt(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static ShipIt load(String contractAddress, Webuj web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ShipIt(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
