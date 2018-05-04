package org.happyuc.webuj.protocol.parity.methods.response;

import java.util.ArrayList;

import org.happyuc.webuj.protocol.core.Response;

/**
 * parity_listAccounts
 * parity_getGetDappAddresses
 * parity_getGetNewDappsAddresses
 * parity_importGhucAccounts
 * parity_listGhucAccounts.
 */
public class ParityAddressesResponse extends Response<ArrayList<String>> {
    public ArrayList<String> getAddresses() {
        return getResult();
    }
}
