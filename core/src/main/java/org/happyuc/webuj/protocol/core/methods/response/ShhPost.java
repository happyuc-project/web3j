package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * shh_post.
 */
public class ShhPost extends Response<Boolean> {

    public boolean messageSent() {
        return getResult();
    }
}
