package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * irc_sendTransaction.
 */
public class HucSendRepTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
