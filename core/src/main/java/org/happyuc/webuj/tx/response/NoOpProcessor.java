package org.happyuc.webuj.tx.response;

import java.io.IOException;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.protocol.exceptions.TransactionException;

/**
 * Return an {@link EmptyRepTransactionReceipt} receipt back to callers containing only the
 * transaction hash.
 */
public class NoOpProcessor extends TransactionReceiptProcessor {

    public NoOpProcessor(Webuj web3j) {
        super(web3j);
    }

    @Override
    public RepTransactionReceipt waitForTransactionReceipt(String transactionHash) throws IOException, TransactionException {
        return new EmptyRepTransactionReceipt(transactionHash);
    }
}
