package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * huc_sendRawTransaction.
 */
public class HucSendRawRepTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
