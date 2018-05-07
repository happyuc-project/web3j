package org.happyuc.webuj.protocol.core.methods.response;

import java.math.BigInteger;

import org.happyuc.webuj.protocol.core.Response;
import org.happyuc.webuj.utils.Numeric;

/**
 * huc_getUncleCountByBlockHash.
 */
public class HucGetUncleCountByBlockHash extends Response<String> {
    public BigInteger getUncleCount() {
        return Numeric.decodeQuantity(getResult());
    }
}
