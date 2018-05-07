package org.happyuc.webuj.protocol.core.methods.response;

import java.math.BigInteger;

import org.happyuc.webuj.protocol.core.Response;
import org.happyuc.webuj.utils.Numeric;

/**
 * huc_getBlockTransactionCountByHash.
 */
public class HucGetBlockTransactionCountByHash extends Response<String> {
    public BigInteger getTransactionCount() {
        return Numeric.decodeQuantity(getResult());
    }
}
