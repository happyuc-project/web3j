package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * irc_sendRawTransaction.
 */
public class HucSendRawRepTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
