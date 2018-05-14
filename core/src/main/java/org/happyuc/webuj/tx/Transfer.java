package org.happyuc.webuj.tx;

import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.protocol.exceptions.TransactionException;
import org.happyuc.webuj.utils.Convert;
import org.happyuc.webuj.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Class for performing Ether transactions on the Happyuc blockchain.
 */
public class Transfer extends ManagedTransaction {

    // This is the cost to send Ether between parties
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000);

    public Transfer(Webuj web3j, TransactionManager transactionManager) {
        super(web3j, transactionManager);
    }

    /**
     * Given the duration required to execute a transaction, asyncronous execution is strongly
     * recommended via {@link Transfer#sendFunds(String, BigDecimal, Convert.Unit)}.
     *
     * @param toAddress destination address
     * @param value     amount to send
     * @param unit      of specified send
     * @return the transaction receipt
     * @throws ExecutionException   if the computation threw an
     *                              exception
     * @throws InterruptedException if the current thread was interrupted
     *                              while waiting
     * @throws TransactionException if the transaction was not mined while waiting
     */
    private RepTransactionReceipt send(String toAddress, BigDecimal value, Convert.Unit unit) throws IOException, InterruptedException, TransactionException {

        BigInteger gasPrice = requestCurrentGasPrice();
        return send(toAddress, value, unit, gasPrice, GAS_LIMIT);
    }

    private RepTransactionReceipt send(String toAddress, BigDecimal value, Convert.Unit unit, BigInteger gasPrice, BigInteger gasLimit) throws IOException, InterruptedException, TransactionException {

        BigDecimal weiValue = Convert.toWei(value, unit);
        if (!Numeric.isIntegerValue(weiValue)) {
            throw new UnsupportedOperationException("Non decimal Wei value provided: " + value + " " + unit.toString() + " = " + weiValue + " Wei");
        }

        String resolvedAddress = ensResolver.resolve(toAddress);
        return send(resolvedAddress, "", weiValue.toBigIntegerExact(), gasPrice, gasLimit);
    }

    public static RemoteCall<RepTransactionReceipt> sendFunds(final Webuj web3j, final Credentials credentials, final String toAddress, final BigDecimal value, final Convert.Unit unit) throws InterruptedException, IOException, TransactionException {

        final TransactionManager transactionManager = new RawTransactionManager(web3j, credentials);

        return new RemoteCall<RepTransactionReceipt>(new Callable<RepTransactionReceipt>() {
            @Override
            public RepTransactionReceipt call() throws Exception {
                return new Transfer(web3j, transactionManager).send(toAddress, value, unit);
            }
        });
    }

    /**
     * Execute the provided function as a transaction asynchronously. This is intended for one-off
     * fund transfers. For multiple, create an instance.
     *
     * @param toAddress destination address
     * @param value     amount to send
     * @param unit      of specified send
     * @return {@link RemoteCall} containing executing transaction
     */
    public RemoteCall<RepTransactionReceipt> sendFunds(final String toAddress, final BigDecimal value, final Convert.Unit unit) {
        return new RemoteCall<RepTransactionReceipt>(new Callable<RepTransactionReceipt>() {
            @Override
            public RepTransactionReceipt call() throws Exception {
                return Transfer.this.send(toAddress, value, unit);
            }
        });
    }

    public RemoteCall<RepTransactionReceipt> sendFunds(final String toAddress, final BigDecimal value, final Convert.Unit unit, final BigInteger gasPrice, final BigInteger gasLimit) {
        return new RemoteCall<RepTransactionReceipt>(new Callable<RepTransactionReceipt>() {
            @Override
            public RepTransactionReceipt call() throws Exception {
                return Transfer.this.send(toAddress, value, unit, gasPrice, gasLimit);
            }
        });
    }
}
