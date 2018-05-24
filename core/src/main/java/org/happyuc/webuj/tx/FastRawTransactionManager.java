package org.happyuc.webuj.tx;

import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Simple RawTransactionManager derivative that manages nonces to facilitate multiple transactions
 * per block.
 */
public class FastRawTransactionManager extends RawTransactionManager {

    private volatile BigInteger nonce = BigInteger.valueOf(-1);

    public FastRawTransactionManager(Webuj webuj, Credentials credentials, byte chainId) {
        super(webuj, credentials, chainId);
    }

    public FastRawTransactionManager(Webuj webuj, Credentials credentials) {
        super(webuj, credentials);
    }


    public FastRawTransactionManager(Webuj webuj, Credentials credentials, int attempts, int sleepDuration) {
        super(webuj, credentials, attempts, sleepDuration);
    }

    public FastRawTransactionManager(Webuj webuj, Credentials credentials, TransactionReceiptProcessor transactionReceiptProcessor) {
        super(webuj, credentials, ChainId.NONE, transactionReceiptProcessor);
    }

    public FastRawTransactionManager(Webuj webuj, Credentials credentials, byte chainId, TransactionReceiptProcessor transactionReceiptProcessor) {
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
