package org.happyuc.webuj.protocol.scenarios;

import org.happyuc.webuj.abi.FunctionEncoder;
import org.happyuc.webuj.abi.FunctionReturnDecoder;
import org.happyuc.webuj.abi.TypeReference;
import org.happyuc.webuj.abi.datatypes.Function;
import org.happyuc.webuj.abi.datatypes.Type;
import org.happyuc.webuj.abi.datatypes.Utf8String;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.methods.request.ReqTransaction;
import org.happyuc.webuj.protocol.core.methods.response.HucCall;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Integration test demonstrating integration with Greeter contract taken from the
 * <a href="https://github.com/ethereum/go-ethereum/wiki/Contract-Tutorial">Contract Tutorial</a>
 * on the Go Happyuc Wiki.
 */
public class GreeterContractIT extends Scenario {

    private static final String VALUE = "Greetings!";

    @Test
    public void testGreeterContract() throws Exception {
        boolean accountUnlocked = unlockAccount();
        assertTrue(accountUnlocked);

        // create our smart contract
        String createTransactionHash = sendCreateContractTransaction();
        assertFalse(createTransactionHash.isEmpty());

        RepTransactionReceipt createRepTransactionReceipt = waitForTransactionReceipt(createTransactionHash);

        assertThat(createRepTransactionReceipt.getTransactionHash(), is(createTransactionHash));

        assertFalse("Contract execution ran out of gas", createRepTransactionReceipt.getGasUsed().equals(GAS_LIMIT));

        String contractAddress = createRepTransactionReceipt.getContractAddress();

        assertNotNull(contractAddress);

        // call our getter
        Function getFunction = createGreetFunction();
        String responseValue = callSmartContractFunction(getFunction, contractAddress);
        assertFalse(responseValue.isEmpty());

        List<Type> response = FunctionReturnDecoder.decode(responseValue, getFunction.getOutputParameters());
        assertThat(response.size(), is(1));
        assertThat((String) response.get(0).getValue(), is(VALUE));
    }

    private String sendCreateContractTransaction() throws Exception {
        BigInteger nonce = getNonce(ALICE.getAddress());

        String encodedConstructor = FunctionEncoder.encodeConstructor(Collections.<Type>singletonList(new Utf8String(VALUE)));

        ReqTransaction reqTransaction = ReqTransaction.createContractTransaction(ALICE.getAddress(), nonce, GAS_PRICE, GAS_LIMIT, BigInteger.ZERO, getGreeterSolidityBinary() + encodedConstructor);

        HucSendRepTransaction transactionResponse = webuj.hucSendTransaction(reqTransaction).sendAsync().get();

        return transactionResponse.getTransactionHash();
    }

    private String callSmartContractFunction(Function function, String contractAddress) throws Exception {

        String encodedFunction = FunctionEncoder.encode(function);

        HucCall response = webuj.hucCall(ReqTransaction.createHucCallTransaction(ALICE.getAddress(), contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).sendAsync().get();

        return response.getValue();
    }

    private static String getGreeterSolidityBinary() throws Exception {
        return load("/solidity/greeter/build/Greeter.bin");
    }

    Function createGreetFunction() {
        return new Function("greet", Collections.<Type>emptyList(), Collections.<TypeReference<?>>singletonList(new TypeReference<Utf8String>() {}));
    }
}
