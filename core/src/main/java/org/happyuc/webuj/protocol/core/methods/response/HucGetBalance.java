package org.happyuc.webuj.protocol.core.methods.response;

import java.math.BigInteger;

import org.happyuc.webuj.protocol.core.Response;
import org.happyuc.webuj.utils.Numeric;

/**
 * irc_getBalance.
 */
public class HucGetBalance extends Response<String> {
    public BigInteger getBalance() {
        return Numeric.decodeQuantity(getResult());
    }
}
