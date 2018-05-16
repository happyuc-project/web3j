package org.happyuc.webuj.tx;

import java.io.IOException;
import java.math.BigInteger;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;

/**
 * ReqTransaction manager implementation for read-only operations on smart contracts.
 */
public class ReadonlyTransactionManager extends TransactionManager {

    public ReadonlyTransactionManager(Webuj webuj, String fromAddress) {
        super(webuj, fromAddress);
    }

    @Override
    public HucSendRepTransaction sendTransaction(BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value) throws IOException {
        throw new UnsupportedOperationException("Only read operations are supported by this transaction manager");
    }
}
