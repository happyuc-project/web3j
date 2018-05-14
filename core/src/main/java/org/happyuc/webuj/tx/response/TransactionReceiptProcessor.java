package org.happyuc.webuj.tx.response;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionReceipt;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.protocol.exceptions.TransactionException;

import java.io.IOException;

/**
 * Abstraction for managing how we wait for transaction receipts to be generated on the network.
 */
public abstract class TransactionReceiptProcessor {

    private final Webuj webuj;

    public TransactionReceiptProcessor(Webuj webuj) {
        this.webuj = webuj;
    }

    public abstract RepTransactionReceipt waitForTransactionReceipt(String transactionHash) throws IOException, TransactionException;

    RepTransactionReceipt sendTransactionReceiptRequest(String transactionHash) throws IOException, TransactionException {
        HucGetRepTransactionReceipt transactionReceipt = webuj.hucGetTransactionReceipt(transactionHash).send();
        if (transactionReceipt.hasError()) {
            throw new TransactionException("Error processing request: " + transactionReceipt.getError().getMessage());
        }

        return transactionReceipt.getTransactionReceipt();
    }
}
