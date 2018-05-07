package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * web3_clientVersion.
 */
public class WebuClientVersion extends Response<String> {

    public String getWebuClientVersion() {
        return getResult();
    }
}
