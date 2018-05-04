package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * shh_newIdentity.
 */
public class ShhNewIdentity extends Response<String> {

    public String getAddress() {
        return getResult();
    }
}
