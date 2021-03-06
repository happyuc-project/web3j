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
import java.util.Optional;

/**
 * Class for performing Huc transactions on the HappyUC blockchain.
 */
public class Transfer extends ManagedTransaction {

    // This is the cost to send Huc between parties
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000);

    public Transfer(Webuj webuj, TransactionManager transactionManager) {
        super(webuj, transactionManager);
    }

    /**
     * Given the duration required to execute a transaction, asyncronous execution is strongly
     * recommended via {@link Transfer#sendFunds(String, BigDecimal, Convert.Unit)}.
     *
     * @param toAddress destination address
     * @param value     amount to send
     * @param unit      of specified send
     * @return {@link Optional} containing our transaction receipt
     * @throws IOException   if the computation threw an exception
     * @throws TransactionException if the transaction was not mined while waiting
     */
    private RepTransactionReceipt send(String toAddress, BigDecimal value, Convert.Unit unit) throws IOException, TransactionException {
        BigInteger gasPrice = requestCurrentGasPrice();
        return send(toAddress, value, unit, gasPrice, GAS_LIMIT);
    }

    private RepTransactionReceipt send(String toAddress, BigDecimal value, Convert.Unit unit, BigInteger gasPrice, BigInteger gasLimit) throws IOException, TransactionException {
        BigDecimal weiValue = Convert.toWei(value, unit);
        if (!Numeric.isIntegerValue(weiValue)) {
            throw new UnsupportedOperationException("Non decimal Wei value provided: " + value + " " + unit.toString() + " = " + weiValue + " Wei");
        }

        String resolvedAddress = ensResolver.resolve(toAddress);
        return send(resolvedAddress, "", weiValue.toBigIntegerExact(), gasPrice, gasLimit);
    }

    public static RemoteCall<RepTransactionReceipt> sendFunds(Webuj webuj, Credentials credentials, String toAddress, BigDecimal value, Convert.Unit unit, String remark) {
        TransactionManager transactionManager = new RawTransactionManager(webuj, credentials);
        return new RemoteCall<>(() -> new Transfer(webuj, transactionManager).send(toAddress, value, unit));
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
    public RemoteCall<RepTransactionReceipt> sendFunds(String toAddress, BigDecimal value, Convert.Unit unit) {
        return new RemoteCall<>(() -> send(toAddress, value, unit));
    }

    public RemoteCall<RepTransactionReceipt> sendFunds(String toAddress, BigDecimal value, Convert.Unit unit, BigInteger gasPrice, BigInteger gasLimit) {
        return new RemoteCall<>(() -> send(toAddress, value, unit, gasPrice, gasLimit));
    }
}
