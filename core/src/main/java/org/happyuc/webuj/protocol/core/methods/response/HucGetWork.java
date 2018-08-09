package org.happyuc.webuj.protocol.core.methods.response;

import java.util.List;

import org.happyuc.webuj.protocol.core.Response;

/**
 * irc_getWork.
 */
public class HucGetWork extends Response<List<String>> {

    public String getCurrentBlockHeaderPowHash() {
        return getResult().get(0);
    }

    public String getSeedHashForDag() {
        return getResult().get(1);
    }

    public String getBoundaryCondition() {
        return getResult().get(2);
    }
}
