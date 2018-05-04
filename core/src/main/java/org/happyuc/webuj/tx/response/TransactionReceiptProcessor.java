package org.happyuc.webuj.tx.response;

import java.io.IOException;
import java.util.Optional;

import org.happyuc.webuj.protocol.webuj;
import org.happyuc.webuj.protocol.core.methods.response.HucGetTransactionReceipt;
import org.happyuc.webuj.protocol.core.methods.response.TransactionReceipt;
import org.happyuc.webuj.protocol.exceptions.TransactionException;

/**
 * Abstraction for managing how we wait for transaction receipts to be generated on the network.
 */
public abstract class TransactionReceiptProcessor {

    private final webuj webuj;

    public TransactionReceiptProcessor(webuj webuj) {
        this.webuj = webuj;
    }

    public abstract TransactionReceipt waitForTransactionReceipt(
            String transactionHash)
            throws IOException, TransactionException;

    Optional<TransactionReceipt> sendTransactionReceiptRequest(
            String transactionHash) throws IOException, TransactionException {
        HucGetTransactionReceipt transactionReceipt =
                webuj.hucGetTransactionReceipt(transactionHash).send();
        if (transactionReceipt.hasError()) {
            throw new TransactionException("Error processing request: "
                    + transactionReceipt.getError().getMessage());
        }

        return transactionReceipt.getTransactionReceipt();
    }
}
