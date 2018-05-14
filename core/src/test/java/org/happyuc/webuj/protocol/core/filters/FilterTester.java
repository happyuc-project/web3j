package org.happyuc.webuj.protocol.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.happyuc.webuj.protocol.ObjectMapperFactory;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.WebujFactory;
import org.happyuc.webuj.protocol.WebujService;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.response.HucLog;
import org.happyuc.webuj.protocol.core.methods.response.HucRepFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucUninstallFilter;
import org.junit.Before;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class FilterTester {

    private WebujService webujService;
    Webuj webuj;

    final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Before
    public void setUp() {
        webujService = mock(WebujService.class);
        webuj = WebujFactory.build(webujService, 1000, scheduledExecutorService);
    }

    @SuppressWarnings("unchecked")
    <T> void runTest(HucLog hucLog, Observable<T> observable) throws Exception {
        HucRepFilter hucRepFilter = objectMapper.readValue("{\n" + "  \"id\":1,\n" + "  \"jsonrpc\": \"2.0\",\n" + "  \"result\": \"0x1\"\n" + "}", HucRepFilter.class);

        HucUninstallFilter hucUninstallFilter = objectMapper.readValue("{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":true}", HucUninstallFilter.class);

        final List<T> expected = createExpected(hucLog);
        final Set<T> results = Collections.synchronizedSet(new HashSet<T>());

        final CountDownLatch transactionLatch = new CountDownLatch(expected.size());

        final CountDownLatch completedLatch = new CountDownLatch(1);

        when(webujService.send(any(Request.class), eq(HucRepFilter.class))).thenReturn(hucRepFilter);
        when(webujService.send(any(Request.class), eq(HucLog.class))).thenReturn(hucLog);
        when(webujService.send(any(Request.class), eq(HucUninstallFilter.class))).thenReturn(hucUninstallFilter);

        Subscription subscription = observable.subscribe(new Action1<T>() {
            @Override
            public void call(T result) {
                results.add(result);
                transactionLatch.countDown();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                fail(throwable.getMessage());
            }
        }, new Action0() {
            @Override
            public void call() {
                completedLatch.countDown();
            }
        });

        transactionLatch.await(1, TimeUnit.SECONDS);
        assertThat(results, CoreMatchers.<Set<T>>equalTo(new HashSet<T>(expected)));

        subscription.unsubscribe();

        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isUnsubscribed());
    }

    @SuppressWarnings("unchecked")
    List createExpected(HucLog hucLog) {
        List<HucLog.LogResult> logResults = hucLog.getLogs();
        if (logResults.isEmpty()) {
            fail("Results cannot be empty");
        }

        List expected = new ArrayList();
        for (HucLog.LogResult logResult : hucLog.getLogs()) {
            expected.add(logResult.get());
        }
        return expected;
    }
}
