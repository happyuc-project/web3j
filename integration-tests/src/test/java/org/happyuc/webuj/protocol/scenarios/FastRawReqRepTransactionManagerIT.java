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
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.tx.FastRawTransactionManager;
import org.happyuc.webuj.tx.Transfer;
import org.happyuc.webuj.tx.response.Callback;
import org.happyuc.webuj.tx.response.PollingTransactionReceiptProcessor;
import org.happyuc.webuj.tx.response.QueuingTransactionReceiptProcessor;
import org.happyuc.webuj.utils.Convert;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.happyuc.webuj.tx.TransactionManager.DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH;

public class FastRawReqRepTransactionManagerIT extends Scenario {

    private static final int COUNT = 10;  // don't set too high if using a real HappyUC network
    private static final long POLLING_FREQUENCY = 15000;

    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();

    @Test
    public void testTransactionPolling() throws Exception {

        List<Future<RepTransactionReceipt>> transactionReceipts = new LinkedList<>();
        FastRawTransactionManager transactionManager = new FastRawTransactionManager(webuj, ALICE, new PollingTransactionReceiptProcessor(webuj, POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH));

        Transfer transfer = new Transfer(webuj, transactionManager);
        BigInteger gasPrice = transfer.requestCurrentGasPrice();

        for (int i = 0; i < COUNT; i++) {

            Future<RepTransactionReceipt> transactionReceiptFuture = createTransaction(transfer, gasPrice).sendAsync();
            transactionReceipts.add(transactionReceiptFuture);
        }

        for (int i = 0; i < DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH && !transactionReceipts.isEmpty(); i++) {

            for (Iterator<Future<RepTransactionReceipt>> iterator = transactionReceipts.iterator(); iterator.hasNext(); ) {
                Future<RepTransactionReceipt> transactionReceiptFuture = iterator.next();

                if (transactionReceiptFuture.isDone()) {
                    RepTransactionReceipt repTransactionReceipt = transactionReceiptFuture.get();
                    assertFalse(repTransactionReceipt.getBlockHash().isEmpty());
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
        ConcurrentLinkedQueue<RepTransactionReceipt> repTransactionReceipts = new ConcurrentLinkedQueue<>();

        FastRawTransactionManager transactionManager = new FastRawTransactionManager(webuj, ALICE, new QueuingTransactionReceiptProcessor(webuj, new Callback() {
            @Override
            public void accept(RepTransactionReceipt transactionReceipt) {
                repTransactionReceipts.add(transactionReceipt);
            }

            @Override
            public void exception(Exception exception) {

            }
        }, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH, POLLING_FREQUENCY));

        Transfer transfer = new Transfer(webuj, transactionManager);

        BigInteger gasPrice = transfer.requestCurrentGasPrice();

        for (int i = 0; i < COUNT; i++) {
            RepTransactionReceipt repTransactionReceipt = createTransaction(transfer, gasPrice).send();
            pendingTransactions.put(repTransactionReceipt.getTransactionHash(), new Object());
        }

        for (int i = 0; i < DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH && !pendingTransactions.isEmpty(); i++) {
            for (RepTransactionReceipt repTransactionReceipt : repTransactionReceipts) {
                assertFalse(repTransactionReceipt.getBlockHash().isEmpty());
                pendingTransactions.remove(repTransactionReceipt.getTransactionHash());
                repTransactionReceipts.remove(repTransactionReceipt);
            }

            Thread.sleep(POLLING_FREQUENCY);
        }

        assertTrue(repTransactionReceipts.isEmpty());
    }


    private RemoteCall<RepTransactionReceipt> createTransaction(Transfer transfer, BigInteger gasPrice) {
        return transfer.sendFunds(BOB.getAddress(), BigDecimal.valueOf(1.0), Convert.Unit.KWEI, gasPrice, Transfer.GAS_LIMIT);
    }
}
