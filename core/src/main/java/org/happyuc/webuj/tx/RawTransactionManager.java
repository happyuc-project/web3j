package org.happyuc.webuj.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.crypto.RawTransaction;
import org.happyuc.webuj.crypto.TransactionEncoder;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.methods.response.HucGetTransactionCount;
import org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction;
import org.happyuc.webuj.tx.response.TransactionReceiptProcessor;
import org.happyuc.webuj.utils.Numeric;

/**
 * TransactionManager implementation using HappyUC wallet file to create and sign transactions
 * locally.
 *
 * <p>This transaction manager provides support for specifying the chain id for transactions as per
 * <a href="https://github.com/happyuc-project/EIPs/issues/155">EIP155</a>.
 */
public class RawTransactionManager extends TransactionManager {

    private final Webuj webuj;
    final Credentials credentials;

    private final byte chainId;

    public RawTransactionManager(Webuj webuj, Credentials credentials, byte chainId) {
        super(webuj, credentials.getAddress());

        this.webuj = webuj;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public RawTransactionManager(Webuj webuj, Credentials credentials, byte chainId, TransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, credentials.getAddress());

        this.webuj = webuj;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public RawTransactionManager(Webuj webuj, Credentials credentials, byte chainId, int attempts, long sleepDuration) {
        super(webuj, attempts, sleepDuration, credentials.getAddress());

        this.webuj = webuj;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public RawTransactionManager(Webuj webuj, Credentials credentials) {
        this(webuj, credentials, ChainId.NONE);
    }

    public RawTransactionManager(Webuj webuj, Credentials credentials, int attempts, int sleepDuration) {
        this(webuj, credentials, ChainId.NONE, attempts, sleepDuration);
    }

    protected BigInteger getNonce() throws IOException {
        HucGetTransactionCount hucGetTransactionCount = webuj.hucGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.PENDING).send();

        return hucGetTransactionCount.getTransactionCount();
    }

    @Override
    public HucSendTransaction sendTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) throws IOException {

        BigInteger nonce = getNonce();

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);

        return signAndSend(rawTransaction);
    }

    public HucSendTransaction signAndSend(RawTransaction rawTransaction) throws IOException {

        byte[] signedMessage;

        if (chainId > ChainId.NONE) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }

        String hexValue = Numeric.toHexString(signedMessage);

        return webuj.hucSendRawTransaction(hexValue).send();
    }
}
