package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * irc_getCode.
 */
public class HucGetCode extends Response<String> {
    public String getCode() {
        return getResult();
    }
}
