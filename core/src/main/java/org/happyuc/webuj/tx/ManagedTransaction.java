package org.happyuc.webuj.tx;

import org.happyuc.webuj.ens.EnsResolver;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.response.HucGasPrice;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.math.BigInteger;


/**
 * Generic transaction manager.
 */
public abstract class ManagedTransaction {
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(18_000_000_000L);

    protected Webuj webuj;

    protected TransactionManager transactionManager;

    protected EnsResolver ensResolver;

    protected ManagedTransaction(Webuj webuj, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.webuj = webuj;
        this.ensResolver = new EnsResolver(webuj);
    }

    /**
     * This should only be used in case you need to get the {@link EnsResolver#syncThreshold}
     * parameter, which dictates the threshold in milliseconds since the last processed block
     * timestamp should be to considered in sync the blockchain.
     *
     * @return sync threshold value in milliseconds
     */
    public long getSyncThreshold() {
        return ensResolver.getSyncThreshold();
    }

    /**
     * This should only be used in case you need to modify the {@link EnsResolver#syncThreshold}
     * parameter, which dictates the threshold in milliseconds since the last processed block
     * timestamp should be to considered in sync the blockchain.
     *
     * @param syncThreshold the sync threshold in milliseconds
     */
    public void setSyncThreshold(long syncThreshold) {
        ensResolver.setSyncThreshold(syncThreshold);
    }

    /**
     * Return the current gas price from the happyuc node.
     *
     * @return the current gas price, determined dynamically at invocation
     * @throws IOException if there's a problem communicating with the happyuc node
     */
    public BigInteger requestCurrentGasPrice() throws IOException {
        HucGasPrice hucGasPrice = webuj.hucGasPrice().send();
        return hucGasPrice.getGasPrice();
    }

    protected RepTransactionReceipt send(String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit) throws IOException, TransactionException {
        TransactionData txData = new TransactionData(to, data, value, gasPrice, gasLimit);
        return transactionManager.executeTransaction(txData);
    }
}
