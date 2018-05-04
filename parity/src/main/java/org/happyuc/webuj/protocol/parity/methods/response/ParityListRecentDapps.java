package org.happyuc.webuj.protocol.parity.methods.response;

import java.util.ArrayList;

import org.happyuc.webuj.protocol.core.Response;

/**
 * parity_ListRecentDapps.
 */
public class ParityListRecentDapps extends Response<ArrayList<String>> {
    public ArrayList<String> getDappsIds() {
        return getResult();
    }
}
