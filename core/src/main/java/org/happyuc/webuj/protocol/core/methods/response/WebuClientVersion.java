package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * webu_clientVersion.
 */
public class WebuClientVersion extends Response<String> {

    public String getWeb3ClientVersion() {
        return getResult();
    }
}
