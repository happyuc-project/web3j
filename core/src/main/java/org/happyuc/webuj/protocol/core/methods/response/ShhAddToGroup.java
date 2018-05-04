package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * shh_addToGroup.
 */
public class ShhAddToGroup extends Response<Boolean> {

    public boolean addedToGroup() {
        return getResult();
    }
}
