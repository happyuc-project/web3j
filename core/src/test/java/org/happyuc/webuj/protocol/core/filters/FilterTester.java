package org.happyuc.webuj.protocol.core.filters;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import rx.Observable;
import rx.Subscription;

import org.happyuc.webuj.protocol.ObjectMapperFactory;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.WebujService;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.response.HucFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucLog;
import org.happyuc.webuj.protocol.core.methods.response.HucUninstallFilter;

import static org.hamcrest.CoreMatchers.equalTo;
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
        webuj = Webuj.build(webujService, 1000, scheduledExecutorService);
    }

    <T> void runTest(HucLog hucLog, Observable<T> observable) throws Exception {
        HucFilter hucFilter = objectMapper.readValue("{\n" + "  \"id\":1,\n" + "  \"jsonrpc\": \"2.0\",\n" + "  \"result\": \"0x1\"\n" + "}", HucFilter.class);

        HucUninstallFilter hucUninstallFilter = objectMapper.readValue("{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":true}", HucUninstallFilter.class);

        @SuppressWarnings("unchecked") List<T> expected = createExpected(hucLog);
        Set<T> results = Collections.synchronizedSet(new HashSet<T>());

        CountDownLatch transactionLatch = new CountDownLatch(expected.size());

        CountDownLatch completedLatch = new CountDownLatch(1);

        when(webujService.send(any(Request.class), eq(HucFilter.class))).thenReturn(hucFilter);
        when(webujService.send(any(Request.class), eq(HucLog.class))).thenReturn(hucLog);
        when(webujService.send(any(Request.class), eq(HucUninstallFilter.class))).thenReturn(hucUninstallFilter);

        Subscription subscription = observable.subscribe(result -> {
            results.add(result);
            transactionLatch.countDown();
        }, throwable -> fail(throwable.getMessage()), () -> completedLatch.countDown());

        transactionLatch.await(1, TimeUnit.SECONDS);
        assertThat(results, equalTo(new HashSet<>(expected)));

        subscription.unsubscribe();

        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isUnsubscribed());
    }

    List createExpected(HucLog hucLog) {
        List<HucLog.LogResult> logResults = hucLog.getLogs();
        if (logResults.isEmpty()) {
            fail("Results cannot be empty");
        }

        return hucLog.getLogs().stream().map(t -> t.get()).collect(Collectors.toList());
    }
}
