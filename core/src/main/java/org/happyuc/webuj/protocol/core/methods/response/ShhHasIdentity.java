package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * shh_hasIdentity.
 */
public class ShhHasIdentity extends Response<Boolean> {

    public boolean hasPrivateKeyForIdentity() {
        return getResult();
    }
}
