package org.happyuc.webuj.protocol.parity.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * parity_deriveAddressHash
 * parity_deriveAddressIndex.
 */
public class ParityDeriveAddress extends Response<String> {
    public String getAddress() {
        return getResult();
    }
}
