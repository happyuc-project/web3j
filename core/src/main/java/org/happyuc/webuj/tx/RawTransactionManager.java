package org.happyuc.webuj.tx;

import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.crypto.RawTransaction;
import org.happyuc.webuj.crypto.TransactionEncoder;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionCount;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;
import org.happyuc.webuj.tx.response.TransactionReceiptProcessor;
import org.happyuc.webuj.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

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

    public RawTransactionManager(Webuj webuj, Credentials credentials, byte chainId, int attempts, int sleepDuration) {
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
        HucGetRepTransactionCount count = webuj.hucGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.PENDING).send();
        return count.getTransactionCount();
    }

    @Override
    public Request<?, HucSendRepTransaction> makeReqTransaction(TransactionData txData) throws IOException {
        BigInteger nonce = txData.getNonce();
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce != null ? nonce : getNonce(),
                txData.getGasPrice(),
                txData.getGasLimit(),
                txData.getTo(),
                txData.getValue(),
                txData.getData());
        String hexSignTxData = sign(rawTransaction);
        return webuj.hucSendRawTransaction(hexSignTxData);
    }

    public String sign(RawTransaction rawTransaction) {
        byte[] signedMessage;
        if (chainId > ChainId.NONE) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }
        return Numeric.toHexString(signedMessage);
    }

}
