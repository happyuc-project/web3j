package org.happyuc.webuj.protocol.parity.methods.response;

import java.util.ArrayList;

import org.happyuc.webuj.protocol.core.Response;
import org.happyuc.webuj.protocol.core.Response;

/**
 * parity_getGetDappAddresses
 * parity_getGetNewDappsAddresses
 * parity_importGethAccounts
 * parity_listGethAccounts.
 */
public class ParityAddressesResponse extends Response<ArrayList<String>> {
    public ArrayList<String> getAddresses() {
        return getResult();
    }
}
