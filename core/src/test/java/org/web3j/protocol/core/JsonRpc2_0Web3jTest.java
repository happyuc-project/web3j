package org.happyuc.webuj.protocol.core;

import java.util.concurrent.ScheduledExecutorService;

import org.junit.Test;

import org.happyuc.webuj.protocol.webuj;
import org.happyuc.webuj.protocol.webujService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JsonRpc2_0webujTest {

    @Test
    public void testStopExecutorOnShutdown() {
        ScheduledExecutorService scheduledExecutorService = mock(ScheduledExecutorService.class);

        webuj webuj = webuj.build(
                mock(webujService.class), 10, scheduledExecutorService
        );

        webuj.shutdown();

        verify(scheduledExecutorService).shutdown();
    }
}