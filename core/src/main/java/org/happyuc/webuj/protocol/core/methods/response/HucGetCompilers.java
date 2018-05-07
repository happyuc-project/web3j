package org.happyuc.webuj.protocol.core.methods.response;

import java.util.List;

import org.happyuc.webuj.protocol.core.Response;

/**
 * huc_getCompilers.
 */
public class HucGetCompilers extends Response<List<String>> {
    public List<String> getCompilers() {
        return getResult();
    }
}
