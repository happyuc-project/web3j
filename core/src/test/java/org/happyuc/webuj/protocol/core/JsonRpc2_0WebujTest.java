package org.happyuc.webuj.protocol.core;

import java.util.concurrent.ScheduledExecutorService;

import org.junit.Test;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.WebujService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JsonRpc2_0WebujTest {

    @Test
    public void testStopExecutorOnShutdown() {
        ScheduledExecutorService scheduledExecutorService = mock(ScheduledExecutorService.class);

        Webuj webuj = Webuj.build(mock(WebujService.class), 10, scheduledExecutorService);

        webuj.shutdown();

        verify(scheduledExecutorService).shutdown();
    }
}