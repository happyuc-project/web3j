package org.happyuc.webuj.protocol.core.methods.response;

import java.math.BigInteger;

import org.happyuc.webuj.protocol.core.Response;
import org.happyuc.webuj.utils.Numeric;

/**
 * huc_hashrate.
 */
public class HucHashrate extends Response<String> {
    public BigInteger getHashrate() {
        return Numeric.decodeQuantity(getResult());
    }
}
