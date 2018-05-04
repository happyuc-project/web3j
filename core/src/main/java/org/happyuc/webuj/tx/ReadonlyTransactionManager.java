package org.happyuc.webuj.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.happyuc.webuj.protocol.webuj;
import org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction;

/**
 * Transaction manager implementation for read-only operations on smart contracts.
 */
public class ReadonlyTransactionManager extends TransactionManager {

    public ReadonlyTransactionManager(webuj webuj, String fromAddress) {
        super(webuj, fromAddress);
    }

    @Override
    public HucSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
            throws IOException {
        throw new UnsupportedOperationException(
                "Only read operations are supported by this transaction manager");
    }
}
