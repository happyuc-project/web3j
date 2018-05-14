package org.happyuc.webuj.tx.response;

import java.io.IOException;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.protocol.exceptions.TransactionException;

/**
 * With each provided transaction hash, poll until we obtain a transaction receipt.
 */
public class PollingTransactionReceiptProcessor extends TransactionReceiptProcessor {

    private final long sleepDuration;
    private final int attempts;

    public PollingTransactionReceiptProcessor(Webuj web3j, long sleepDuration, int attempts) {
        super(web3j);
        this.sleepDuration = sleepDuration;
        this.attempts = attempts;
    }

    @Override
    public RepTransactionReceipt waitForTransactionReceipt(String transactionHash) throws IOException, TransactionException {

        return getTransactionReceipt(transactionHash, sleepDuration, attempts);
    }

    private RepTransactionReceipt getTransactionReceipt(String transactionHash, long sleepDuration, int attempts) throws IOException, TransactionException {

        RepTransactionReceipt repTransactionReceipt = sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (repTransactionReceipt == null) {
                try {
                    Thread.sleep(sleepDuration);
                } catch (InterruptedException e) {
                    throw new TransactionException(e);
                }
                repTransactionReceipt = sendTransactionReceiptRequest(transactionHash);
            } else {
                return repTransactionReceipt;
            }
        }

        throw new TransactionException("ReqTransaction receipt was not generated after " + ((sleepDuration * attempts) / 1000 + " seconds for transaction: " + transactionHash));
    }
}
