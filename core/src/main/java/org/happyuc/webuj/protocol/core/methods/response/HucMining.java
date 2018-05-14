package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * huc_mining.
 */
public class HucMining extends Response<Boolean> {
    public boolean isMining() {
        return getResult();
    }
}
