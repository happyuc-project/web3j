package org.happyuc.webuj.protocol.scenarios;

import java.math.BigInteger;
import java.util.List;

import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;
import org.junit.Test;

import org.happyuc.webuj.abi.FunctionEncoder;
import org.happyuc.webuj.abi.FunctionReturnDecoder;
import org.happyuc.webuj.abi.datatypes.Function;
import org.happyuc.webuj.abi.datatypes.Type;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.methods.request.ReqTransaction;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Integration test demonstrating the full contract deployment workflow.
 */
public class DeployContractIT extends Scenario {

    @Test
    public void testContractCreation() throws Exception {
        boolean accountUnlocked = unlockAccount();
        assertTrue(accountUnlocked);

        String transactionHash = sendTransaction();
        assertFalse(transactionHash.isEmpty());

        RepTransactionReceipt repTransactionReceipt = waitForTransactionReceipt(transactionHash);

        assertThat(repTransactionReceipt.getTransactionHash(), is(transactionHash));

        assertFalse("Contract execution ran out of gas", repTransactionReceipt.getGasUsed().equals(GAS_LIMIT));

        String contractAddress = repTransactionReceipt.getContractAddress();

        assertNotNull(contractAddress);

        Function function = createFibonacciFunction();

        String responseValue = callSmartContractFunction(function, contractAddress);
        assertFalse(responseValue.isEmpty());

        List<Type> uint = FunctionReturnDecoder.decode(responseValue, function.getOutputParameters());
        assertThat(uint.size(), is(1));
        assertThat(uint.get(0).getValue(), equalTo(BigInteger.valueOf(13)));
    }

    private String sendTransaction() throws Exception {
        BigInteger nonce = getNonce(ALICE.getAddress());

        ReqTransaction reqTransaction = ReqTransaction.createContractTransaction(ALICE.getAddress(), nonce, GAS_PRICE, GAS_LIMIT, BigInteger.ZERO, getFibonacciSolidityBinary());

        HucSendRepTransaction transactionResponse = webuj.hucSendTransaction(reqTransaction).sendAsync().get();

        return transactionResponse.getTransactionHash();
    }

    private String callSmartContractFunction(Function function, String contractAddress) throws Exception {

        String encodedFunction = FunctionEncoder.encode(function);

        org.happyuc.webuj.protocol.core.methods.response.HucCall response = webuj.hucCall(ReqTransaction.createHucCallTransaction(ALICE.getAddress(), contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).sendAsync().get();

        return response.getValue();
    }
}
