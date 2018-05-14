package org.happyuc.webuj.protocol.scenarios;

import org.happyuc.webuj.crypto.RawTransaction;
import org.happyuc.webuj.crypto.TransactionEncoder;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionCount;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.utils.Convert;
import org.happyuc.webuj.utils.Numeric;
import org.junit.Test;

import java.math.BigInteger;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Create, sign and send a raw transaction.
 */
public class CreateRawReqRepTransactionIT extends Scenario {

    @Test
    public void testTransferEther() throws Exception {
        BigInteger nonce = getNonce(ALICE.getAddress());
        RawTransaction rawTransaction = createHucerTransaction(nonce, BOB.getAddress());

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, ALICE);
        String hexValue = Numeric.toHexString(signedMessage);

        HucSendRepTransaction hucSendRepTransaction = webuj.hucSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = hucSendRepTransaction.getTransactionHash();

        assertFalse(transactionHash.isEmpty());

        RepTransactionReceipt repTransactionReceipt = waitForTransactionReceipt(transactionHash);

        assertThat(repTransactionReceipt.getTransactionHash(), is(transactionHash));
    }

    @Test
    public void testDeploySmartContract() throws Exception {
        BigInteger nonce = getNonce(ALICE.getAddress());
        RawTransaction rawTransaction = createSmartContractTransaction(nonce);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, ALICE);
        String hexValue = Numeric.toHexString(signedMessage);

        HucSendRepTransaction hucSendRepTransaction = webuj.hucSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = hucSendRepTransaction.getTransactionHash();

        assertFalse(transactionHash.isEmpty());

        RepTransactionReceipt repTransactionReceipt = waitForTransactionReceipt(transactionHash);

        assertThat(repTransactionReceipt.getTransactionHash(), is(transactionHash));

        assertFalse("Contract execution ran out of gas", rawTransaction.getGasLimit().equals(repTransactionReceipt.getGasUsed()));
    }

    private static RawTransaction createHucerTransaction(BigInteger nonce, String toAddress) {
        BigInteger value = Convert.toWei("0.5", Convert.Unit.HUC).toBigInteger();

        return RawTransaction.createHucerTransaction(nonce, GAS_PRICE, GAS_LIMIT, toAddress, value);
    }

    private static RawTransaction createSmartContractTransaction(BigInteger nonce) throws Exception {
        return RawTransaction.createContractTransaction(nonce, GAS_PRICE, GAS_LIMIT, BigInteger.ZERO, getFibonacciSolidityBinary());
    }

    BigInteger getNonce(String address) throws Exception {
        HucGetRepTransactionCount hucGetRepTransactionCount = webuj.hucGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();

        return hucGetRepTransactionCount.getTransactionCount();
    }
}
