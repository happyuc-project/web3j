package org.happyuc.webuj.protocol.core;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.WebujFactory;
import org.happyuc.webuj.protocol.core.methods.request.HucReqFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucBlock;
import org.happyuc.webuj.protocol.http.HttpService;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Observable callback tests.
 */
public class ObservableIT {

    private static final int EVENT_COUNT = 5;
    private static final int TIMEOUT_MINUTES = 5;

    private Webuj webuj;

    @Before
    public void setUp() {
        this.webuj = WebujFactory.build(new HttpService());
    }

    @Test
    public void testBlockObservable() throws Exception {
        run(webuj.blockObservable(false));
    }

    @Test
    public void testPendingTransactionObservable() throws Exception {
        run(webuj.pendingTransactionObservable());
    }

    @Test
    public void testTransactionObservable() throws Exception {
        run(webuj.transactionObservable());
    }

    @Test
    public void testLogObservable() throws Exception {
        run(webuj.hucLogObservable(new HucReqFilter()));
    }

    @Test
    public void testReplayObservable() throws Exception {
        run(webuj.replayBlocksObservable(new DefaultBlockParameterNumber(0), new DefaultBlockParameterNumber(EVENT_COUNT), true));
    }

    @Test
    public void testCatchUpToLatestAndSubscribeToNewBlocksObservable() throws Exception {
        HucBlock hucBlock = webuj.hucGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
        BigInteger latestBlockNumber = hucBlock.getBlock().getNumber();
        run(webuj.catchUpToLatestAndSubscribeToNewBlocksObservable(new DefaultBlockParameterNumber(latestBlockNumber.subtract(BigInteger.ONE)), false));
    }

    private <T> void run(Observable<T> observable) throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(EVENT_COUNT);
        final CountDownLatch completedLatch = new CountDownLatch(EVENT_COUNT);

        Subscription subscription = observable.subscribe(new Action1<T>() {
            @Override
            public void call(final T x) {
                countDownLatch.countDown();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(final Throwable throwable) {
                throwable.printStackTrace();
            }
        }, new Action0() {
            @Override
            public void call() {
                completedLatch.countDown();
            }
        });

        countDownLatch.await(TIMEOUT_MINUTES, TimeUnit.MINUTES);
        subscription.unsubscribe();
        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isUnsubscribed());
    }
}
