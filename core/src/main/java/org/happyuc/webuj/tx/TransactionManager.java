package org.happyuc.webuj.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction;
import org.happyuc.webuj.protocol.core.methods.response.TransactionReceipt;
import org.happyuc.webuj.protocol.exceptions.TransactionException;
import org.happyuc.webuj.tx.response.PollingTransactionReceiptProcessor;
import org.happyuc.webuj.tx.response.TransactionReceiptProcessor;

import static org.happyuc.webuj.protocol.core.JsonRpc2_0Webuj.DEFAULT_BLOCK_TIME;

/**
 * Transaction manager abstraction for executing transactions with HappyUC client via
 * various mechanisms.
 */
public abstract class TransactionManager {

    public static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 40;
    public static final long DEFAULT_POLLING_FREQUENCY = DEFAULT_BLOCK_TIME;

    private final TransactionReceiptProcessor transactionReceiptProcessor;
    private final String fromAddress;

    protected TransactionManager(TransactionReceiptProcessor transactionReceiptProcessor, String fromAddress) {
        this.transactionReceiptProcessor = transactionReceiptProcessor;
        this.fromAddress = fromAddress;
    }

    protected TransactionManager(Webuj webu, String fromAddress) {
        this(new PollingTransactionReceiptProcessor(webu, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH), fromAddress);
    }

    protected TransactionManager(Webuj webuj, int attempts, long sleepDuration, String fromAddress) {
        this(new PollingTransactionReceiptProcessor(webuj, sleepDuration, attempts), fromAddress);
    }

    protected TransactionReceipt executeTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) throws IOException, TransactionException {

        HucSendTransaction hucSendTransaction = sendTransaction(gasPrice, gasLimit, to, data, value);
        return processResponse(hucSendTransaction);
    }

    public abstract HucSendTransaction sendTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) throws IOException;

    public String getFromAddress() {
        return fromAddress;
    }

    private TransactionReceipt processResponse(HucSendTransaction transactionResponse) throws IOException, TransactionException {
        if (transactionResponse.hasError()) {
            throw new RuntimeException("Error processing transaction request: " + transactionResponse.getError().getMessage());
        }

        String transactionHash = transactionResponse.getTransactionHash();

        return transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
    }


}
