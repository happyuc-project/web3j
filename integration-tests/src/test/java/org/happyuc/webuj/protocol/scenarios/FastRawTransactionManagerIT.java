package org.happyuc.webuj.protocol.scenarios;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.protocol.core.methods.response.TransactionReceipt;
import org.happyuc.webuj.tx.FastRawTransactionManager;
import org.happyuc.webuj.tx.Transfer;
import org.happyuc.webuj.tx.response.Callback;
import org.happyuc.webuj.tx.response.PollingTransactionReceiptProcessor;
import org.happyuc.webuj.tx.response.QueuingTransactionReceiptProcessor;
import org.happyuc.webuj.utils.Convert;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.happyuc.webuj.tx.TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH;

public class FastRawTransactionManagerIT extends Scenario {

    private static final int COUNT = 10;  // don't set too high if using a real HappyUC network
    private static final long POLLING_FREQUENCY = 15000;

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();

    @Test
    public void testTransactionPolling() throws Exception {

        List<Future<TransactionReceipt>> transactionReceipts = new LinkedList<>();
        FastRawTransactionManager transactionManager = new FastRawTransactionManager(webuj, ALICE, new PollingTransactionReceiptProcessor(webuj, POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH));

        Transfer transfer = new Transfer(webuj, transactionManager);
        BigInteger gasPrice = transfer.requestCurrentGasPrice();

        for (int i = 0; i < COUNT; i++) {

            Future<TransactionReceipt> transactionReceiptFuture = createTransaction(transfer, gasPrice).sendAsync();
            transactionReceipts.add(transactionReceiptFuture);
        }

        for (int i = 0; i < DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH && !transactionReceipts.isEmpty(); i++) {

            for (Iterator<Future<TransactionReceipt>> iterator = transactionReceipts.iterator(); iterator.hasNext(); ) {
                Future<TransactionReceipt> transactionReceiptFuture = iterator.next();

                if (transactionReceiptFuture.isDone()) {
                    TransactionReceipt transactionReceipt = transactionReceiptFuture.get();
                    assertFalse(transactionReceipt.getBlockHash().isEmpty());
                    iterator.remove();
                }
            }

            Thread.sleep(POLLING_FREQUENCY);
        }

        assertTrue(transactionReceipts.isEmpty());
    }

    @Test
    public void testTransactionQueuing() throws Exception {

        Map<String, Object> pendingTransactions = new ConcurrentHashMap<>();
        ConcurrentLinkedQueue<TransactionReceipt> transactionReceipts = new ConcurrentLinkedQueue<>();

        FastRawTransactionManager transactionManager = new FastRawTransactionManager(webuj, ALICE, new QueuingTransactionReceiptProcessor(webuj, new Callback() {
            @Override
            public void accept(TransactionReceipt transactionReceipt) {
                transactionReceipts.add(transactionReceipt);
            }

            @Override
            public void exception(Exception exception) {

            }
        }, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH, POLLING_FREQUENCY));

        Transfer transfer = new Transfer(webuj, transactionManager);

        BigInteger gasPrice = transfer.requestCurrentGasPrice();

        for (int i = 0; i < COUNT; i++) {
            TransactionReceipt transactionReceipt = createTransaction(transfer, gasPrice).send();
            pendingTransactions.put(transactionReceipt.getTransactionHash(), new Object());
        }

        for (int i = 0; i < DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH && !pendingTransactions.isEmpty(); i++) {
            for (TransactionReceipt transactionReceipt : transactionReceipts) {
                assertFalse(transactionReceipt.getBlockHash().isEmpty());
                pendingTransactions.remove(transactionReceipt.getTransactionHash());
                transactionReceipts.remove(transactionReceipt);
            }

            Thread.sleep(POLLING_FREQUENCY);
        }

        assertTrue(transactionReceipts.isEmpty());
    }


    private RemoteCall<TransactionReceipt> createTransaction(Transfer transfer, BigInteger gasPrice) {
        return transfer.sendFunds(BOB.getAddress(), BigDecimal.valueOf(1.0), Convert.Unit.KWEI, gasPrice, Transfer.GAS_LIMIT);
    }
}
