package org.happyuc.webuj.tx;

import org.happyuc.webuj.crypto.Credentials;
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

    public static final BigInteger GAS_PRICE = BigInteger.valueOf(22000000000L);

    protected Webuj webuj;

    protected TransactionManager transactionManager;

    protected EnsResolver ensResolver;

    protected ManagedTransaction(Webuj webuj, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.webuj = webuj;
        this.ensResolver = new EnsResolver(webuj);
    }

    protected ManagedTransaction(Webuj webuj) {
        this.webuj = webuj;
        this.ensResolver = new EnsResolver(webuj);
    }

    public void setTransactionManager(TransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    public void setTransactionManager(Credentials credentials){
        this.transactionManager = new RawTransactionManager(webuj, credentials);
    }

    /**
     * This should only be used in case you need to get the {@link EnsResolver#syncThreshold}
     * parameter, which dictates the threshold in milliseconds since the last processed block
     * timestamp should be to considered in sync the blockchain.
     *
     * <p>It is currently experimental and only used in ENS name resolution, but will probably
     * be made available for read calls in the future.
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
     * <p>It is currently experimental and only used in ENS name resolution, but will probably
     * be made available for read calls in the future.
     *
     * @param syncThreshold the sync threshold in milliseconds
     */
    public void setSyncThreshold(long syncThreshold) {
        ensResolver.setSyncThreshold(syncThreshold);
    }

    /**
     * Return the current gas price from the ethereum node.
     * <p>
     * Note: this method was previously called {@code getGasPrice} but was renamed to
     * distinguish it when a bean accessor method on {@link Contract} was added with that name.
     * If you have a Contract subclass that is calling this method (unlikely since those
     * classes are usually generated and until very recently those generated subclasses were
     * marked {@code final}), then you will need to change your code to call this method
     * instead, if you want the dynamic behavior.
     * </p>
     *
     * @return the current gas price, determined dynamically at invocation
     * @throws IOException if there's a problem communicating with the ethereum node
     */
    public BigInteger requestCurrentGasPrice() throws IOException {
        HucGasPrice hucGasPrice = webuj.hucGasPrice().send();

        return hucGasPrice.getGasPrice();
    }

    protected RepTransactionReceipt send(String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit) throws IOException, TransactionException {

        return transactionManager.executeTransaction(gasPrice, gasLimit, to, data, value);
    }
}
