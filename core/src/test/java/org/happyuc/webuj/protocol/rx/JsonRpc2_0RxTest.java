package org.happyuc.webuj.protocol.rx;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;
import rx.Observable;
import rx.Subscription;

import org.happyuc.webuj.protocol.ObjectMapperFactory;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.WebujService;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterNumber;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.response.HucBlock;
import org.happyuc.webuj.protocol.core.methods.response.HucFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucLog;
import org.happyuc.webuj.protocol.core.methods.response.HucUninstallFilter;
import org.happyuc.webuj.utils.Numeric;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonRpc2_0RxTest {

    private final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    private Webuj webuj;

    private WebujService webujService;

    @Before
    public void setUp() {
        webujService = mock(WebujService.class);
        webuj = Webuj.build(webujService, 1000, Executors.newSingleThreadScheduledExecutor());
    }

    @Test
    public void testReplayBlocksObservable() throws Exception {

        List<HucBlock> hucBlocks = Arrays.asList(createBlock(0), createBlock(1), createBlock(2));

        OngoingStubbing<HucBlock> stubbing = when(webujService.send(any(Request.class), eq(HucBlock.class)));
        for (HucBlock hucBlock : hucBlocks) {
            stubbing = stubbing.thenReturn(hucBlock);
        }

        Observable<HucBlock> observable = webuj.replayBlocksObservable(new DefaultBlockParameterNumber(BigInteger.ZERO), new DefaultBlockParameterNumber(BigInteger.valueOf(2)), false);

        CountDownLatch transactionLatch = new CountDownLatch(hucBlocks.size());
        CountDownLatch completedLatch = new CountDownLatch(1);

        List<HucBlock> results = new ArrayList<>(hucBlocks.size());
        Subscription subscription = observable.subscribe(result -> {
            results.add(result);
            transactionLatch.countDown();
        }, throwable -> fail(throwable.getMessage()), completedLatch::countDown);

        transactionLatch.await(1, TimeUnit.SECONDS);
        assertThat(results, equalTo(hucBlocks));

        subscription.unsubscribe();

        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isUnsubscribed());
    }

    @Test
    public void testReplayBlocksDescendingObservable() throws Exception {

        List<HucBlock> hucBlocks = Arrays.asList(createBlock(2), createBlock(1), createBlock(0));

        OngoingStubbing<HucBlock> stubbing = when(webujService.send(any(Request.class), eq(HucBlock.class)));
        for (HucBlock hucBlock : hucBlocks) {
            stubbing = stubbing.thenReturn(hucBlock);
        }

        Observable<HucBlock> observable = webuj.replayBlocksObservable(new DefaultBlockParameterNumber(BigInteger.ZERO), new DefaultBlockParameterNumber(BigInteger.valueOf(2)), false, false);

        CountDownLatch transactionLatch = new CountDownLatch(hucBlocks.size());
        CountDownLatch completedLatch = new CountDownLatch(1);

        List<HucBlock> results = new ArrayList<>(hucBlocks.size());
        Subscription subscription = observable.subscribe(result -> {
            results.add(result);
            transactionLatch.countDown();
        }, throwable -> fail(throwable.getMessage()), completedLatch::countDown);

        transactionLatch.await(1, TimeUnit.SECONDS);
        assertThat(results, equalTo(hucBlocks));

        subscription.unsubscribe();

        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isUnsubscribed());
    }

    @Test
    public void testCatchUpToLatestAndSubscribeToNewBlockObservable() throws Exception {
        List<HucBlock> expected = Arrays.asList(createBlock(0), createBlock(1), createBlock(2), createBlock(3), createBlock(4), createBlock(5), createBlock(6));

        List<HucBlock> hucBlocks = Arrays.asList(expected.get(2),  // greatest block
                expected.get(0), expected.get(1), expected.get(2), expected.get(4), // greatest block
                expected.get(3), expected.get(4), expected.get(4),  // greatest block
                expected.get(5),  // initial response from hucGetFilterLogs call
                expected.get(6)); // subsequent block from new block observable

        OngoingStubbing<HucBlock> stubbing = when(webujService.send(any(Request.class), eq(HucBlock.class)));
        for (HucBlock hucBlock : hucBlocks) {
            stubbing = stubbing.thenReturn(hucBlock);
        }

        HucFilter hucFilter = objectMapper.readValue("{\n" + "  \"id\":1,\n" + "  \"jsonrpc\": \"2.0\",\n" + "  \"result\": \"0x1\"\n" + "}", HucFilter.class);
        HucLog hucLog = objectMapper.readValue("{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":[" + "\"0x31c2342b1e0b8ffda1507fbffddf213c4b3c1e819ff6a84b943faabb0ebf2403\"" + "]}", HucLog.class);
        HucUninstallFilter hucUninstallFilter = objectMapper.readValue("{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":true}", HucUninstallFilter.class);

        when(webujService.send(any(Request.class), eq(HucFilter.class))).thenReturn(hucFilter);
        when(webujService.send(any(Request.class), eq(HucLog.class))).thenReturn(hucLog);
        when(webujService.send(any(Request.class), eq(HucUninstallFilter.class))).thenReturn(hucUninstallFilter);

        Observable<HucBlock> observable = webuj.catchUpToLatestAndSubscribeToNewBlocksObservable(new DefaultBlockParameterNumber(BigInteger.ZERO), false);

        CountDownLatch transactionLatch = new CountDownLatch(expected.size());
        CountDownLatch completedLatch = new CountDownLatch(1);

        List<HucBlock> results = new ArrayList<>(expected.size());
        Subscription subscription = observable.subscribe(result -> {
            results.add(result);
            transactionLatch.countDown();
        }, throwable -> fail(throwable.getMessage()), completedLatch::countDown);

        transactionLatch.await(1250, TimeUnit.MILLISECONDS);
        assertThat(results, equalTo(expected));

        subscription.unsubscribe();

        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isUnsubscribed());
    }

    private HucBlock createBlock(int number) {
        HucBlock hucBlock = new HucBlock();
        HucBlock.Block block = new HucBlock.Block();
        block.setNumber(Numeric.encodeQuantity(BigInteger.valueOf(number)));

        hucBlock.setResult(block);
        return hucBlock;
    }
}
