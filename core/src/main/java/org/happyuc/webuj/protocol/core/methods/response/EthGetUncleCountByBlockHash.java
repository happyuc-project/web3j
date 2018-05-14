package org.happyuc.webuj.protocol.core.methods.response;

import java.math.BigInteger;

import org.happyuc.webuj.utils.Numeric;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

/**
 * eth_getUncleCountByBlockHash.
 */
public class EthGetUncleCountByBlockHash extends Response<String> {
    public BigInteger getUncleCount() {
        return Numeric.decodeQuantity(getResult());
    }
}
