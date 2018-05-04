package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * shh_version.
 */
public class ShhVersion extends Response<String> {

    public String getVersion() {
        return getResult();
    }
}
