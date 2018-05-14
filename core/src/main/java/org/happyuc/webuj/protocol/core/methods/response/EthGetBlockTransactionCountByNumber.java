package org.happyuc.webuj.protocol.core.methods.response;

import java.math.BigInteger;

import org.happyuc.webuj.utils.Numeric;
import org.web3j.protocol.core.Response;
import org.web3j.utils.Numeric;

/**
 * eth_getBlockTransactionCountByNumber.
 */
public class EthGetBlockTransactionCountByNumber extends Response<String> {
    public BigInteger getTransactionCount() {
        return Numeric.decodeQuantity(getResult());
    }
}
