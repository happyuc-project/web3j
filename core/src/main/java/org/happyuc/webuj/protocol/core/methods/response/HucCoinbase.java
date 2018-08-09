package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * irc_coinbase.
 */
public class HucCoinbase extends Response<String> {
    public String getAddress() {
        return getResult();
    }
}
