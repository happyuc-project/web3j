package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * irc_getStorageAt.
 */
public class HucGetStorageAt extends Response<String> {
    public String getData() {
        return getResult();
    }
}
