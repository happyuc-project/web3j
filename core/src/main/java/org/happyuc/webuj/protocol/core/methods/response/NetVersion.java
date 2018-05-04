package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * net_version.
 */
public class NetVersion extends Response<String> {
    public String getNetVersion() {
        return getResult();
    }
}
