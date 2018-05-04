package org.happyuc.webuj.tx.response;

import java.io.IOException;

import org.happyuc.webuj.protocol.webuj;
import org.happyuc.webuj.protocol.core.methods.response.TransactionReceipt;
import org.happyuc.webuj.protocol.exceptions.TransactionException;

/**
 * Return an {@link EmptyTransactionReceipt} receipt back to callers containing only the
 * transaction hash.
 */
public class NoOpProcessor extends TransactionReceiptProcessor {

    public NoOpProcessor(webuj webuj) {
        super(webuj);
    }

    @Override
    public TransactionReceipt waitForTransactionReceipt(String transactionHash)
            throws IOException, TransactionException {
        return new EmptyTransactionReceipt(transactionHash);
    }
}
