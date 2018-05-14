package org.happyuc.webuj.protocol.admin.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * Boolean response type.
 */
public class BooleanResponse extends Response<Boolean> {
    public boolean success() {
        return getResult();
    }
}
