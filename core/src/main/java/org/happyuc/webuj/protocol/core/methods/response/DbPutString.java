package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * db_putString.
 */
public class DbPutString extends Response<Boolean> {

    public boolean valueStored() {
        return getResult();
    }
}
