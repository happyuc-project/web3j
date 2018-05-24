package org.happyuc.webuj.tx;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;

/**
 * ReqTransaction manager implementation for read-only operations on smart contracts.
 */
public class ReadonlyTransactionManager extends TransactionManager {

    public ReadonlyTransactionManager(Webuj webuj, String fromAddress) {
        super(webuj, fromAddress);
    }

    @Override
    public Request<?, HucSendRepTransaction> makeReqTransaction(TransactionData txData) {
        throw new UnsupportedOperationException("Only read operations are supported by this transaction manager");
    }
}
