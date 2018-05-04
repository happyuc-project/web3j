package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * huc_sendTransaction.
 */
public class HucSendTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
