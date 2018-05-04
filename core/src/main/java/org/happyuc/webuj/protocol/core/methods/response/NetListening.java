package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * net_listening.
 */
public class NetListening extends Response<Boolean> {
    public boolean isListening() {
        return getResult();
    }
}
