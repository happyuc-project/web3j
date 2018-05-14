package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * huc_call.
 */
public class HucCall extends Response<String> {
    public String getValue() {
        return getResult();
    }
}
