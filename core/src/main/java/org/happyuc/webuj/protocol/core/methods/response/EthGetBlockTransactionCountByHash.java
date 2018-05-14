package org.happyuc.webuj.protocol.core.methods.response;

import java.math.BigInteger;

import org.happyuc.webuj.utils.Numeric;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

/**
 * eth_getBlockTransactionCountByHash.
 */
public class EthGetBlockTransactionCountByHash extends Response<String> {
    public BigInteger getTransactionCount() {
        return Numeric.decodeQuantity(getResult());
    }
}
