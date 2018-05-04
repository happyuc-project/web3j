package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * db_getString.
 */
public class DbGetString extends Response<String> {

    public String getStoredValue() {
        return getResult();
    }
}
