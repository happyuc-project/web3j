package org.happyuc.webuj.tx;

import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.crypto.RawTransaction;
import org.happyuc.webuj.crypto.TransactionEncoder;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionCount;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;
import org.happyuc.webuj.tx.response.TransactionReceiptProcessor;
import org.happyuc.webuj.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

/**
 * TransactionManager implementation using Happyuc wallet file to create and sign transactions
 * locally.
 *
 * <p>This transaction manager provides support for specifying the chain id for transactions as per
 * <a href="https://github.com/ethereum/EIPs/issues/155">EIP155</a>.
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
        HucGetRepTransactionCount hucGetRepTransactionCount = webuj.hucGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.PENDING).send();

        return hucGetRepTransactionCount.getTransactionCount();
    }

    @Override
    public HucSendRepTransaction sendTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) throws IOException {

        BigInteger nonce = getNonce();

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);

        return signAndSend(rawTransaction);
    }

    public HucSendRepTransaction signAndSend(RawTransaction rawTransaction) throws IOException {

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
