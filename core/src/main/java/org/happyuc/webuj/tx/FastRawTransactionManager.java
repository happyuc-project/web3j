package org.happyuc.webuj.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.protocol.webuj;
import org.happyuc.webuj.tx.response.Callback;
import org.happyuc.webuj.tx.response.QueuingTransactionReceiptProcessor;
import org.happyuc.webuj.tx.response.TransactionReceiptProcessor;

/**
 * Simple RawTransactionManager derivative that manages nonces to facilitate multiple transactions
 * per block.
 */
public class FastRawTransactionManager extends RawTransactionManager {

    private volatile BigInteger nonce = BigInteger.valueOf(-1);

    public FastRawTransactionManager(webuj webuj, Credentials credentials, byte chainId) {
        super(webuj, credentials, chainId);
    }

    public FastRawTransactionManager(webuj webuj, Credentials credentials) {
        super(webuj, credentials);
    }

    public FastRawTransactionManager(
            webuj webuj, Credentials credentials,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(webuj, credentials, ChainId.NONE, transactionReceiptProcessor);
    }

    public FastRawTransactionManager(
            webuj webuj, Credentials credentials, byte chainId,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(webuj, credentials, chainId, transactionReceiptProcessor);
    }

    @Override
    protected synchronized BigInteger getNonce() throws IOException {
        if (nonce.signum() == -1) {
            // obtain lock
            nonce = super.getNonce();
        } else {
            nonce = nonce.add(BigInteger.ONE);
        }
        return nonce;
    }

    public BigInteger getCurrentNonce() {
        return nonce;
    }

    public synchronized void resetNonce() throws IOException {
        nonce = super.getNonce();
    }

    public synchronized void setNonce(BigInteger value) {
        nonce = value;
    }
}
