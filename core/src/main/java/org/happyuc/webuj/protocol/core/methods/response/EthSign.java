package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * huc_sign.
 */
public class HucSign extends Response<String> {
    public String getSignature() {
        return getResult();
    }
}
