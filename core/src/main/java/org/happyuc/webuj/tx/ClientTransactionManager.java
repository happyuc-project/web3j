package org.happyuc.webuj.tx;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.request.ReqTransaction;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;
import org.happyuc.webuj.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;

/**
 * TransactionManager implementation for using an Happyuc node to transact.
 *
 * <p><b>Note</b>: accounts must be unlocked on the node for transactions to be successful.
 */
public class ClientTransactionManager extends TransactionManager {

    private final Webuj webuj;

    public ClientTransactionManager(Webuj webuj, String fromAddress) {
        super(webuj, fromAddress);
        this.webuj = webuj;
    }

    public ClientTransactionManager(Webuj webuj, String fromAddress, int attempts, int sleepDuration) {
        super(webuj, attempts, sleepDuration, fromAddress);
        this.webuj = webuj;
    }

    public ClientTransactionManager(Webuj webuj, String fromAddress, TransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, fromAddress);
        this.webuj = webuj;
    }

    @Override
    public HucSendRepTransaction sendTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) throws IOException {

        ReqTransaction reqTransaction = new ReqTransaction(getFromAddress(), null, gasPrice, gasLimit, to, value, data);

        return webuj.hucSendTransaction(reqTransaction).send();
    }
}
