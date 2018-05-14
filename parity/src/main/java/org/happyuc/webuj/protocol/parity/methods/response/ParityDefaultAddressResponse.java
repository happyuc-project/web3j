package org.happyuc.webuj.protocol.parity.methods.response;

import org.happyuc.webuj.protocol.core.Response;
import org.happyuc.webuj.protocol.core.Response;

/**
 * parity_getDappDefaultAddress
 * parity_getNewDappsDefaultAddress.
 */
public class ParityDefaultAddressResponse extends Response<String> {
    public String getAddress() {
        return getResult();
    }
}
