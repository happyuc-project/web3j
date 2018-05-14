package org.happyuc.webuj.protocol;

import java.util.concurrent.ScheduledExecutorService;

import org.happyuc.webuj.protocol.core.JsonRpc2_0Webuj;

/**
 * webuj factory implementation.
 */
public class WebujFactory {

    /**
     * Construct a new Webuj instance.
     *
     * @param webujService webuj service instance - i.e. HTTP or IPC
     * @return new Webuj instance
     */
    public static Webuj build(WebujService webujService) {
        return new JsonRpc2_0Webuj(webujService);
    }

    /**
     * Construct a new Webuj instance.
     *
     * @param webujService             webuj service instance - i.e. HTTP or IPC
     * @param pollingInterval          polling interval for responses from network nodes
     * @param scheduledExecutorService executor service to use for scheduled tasks.
     *                                 <strong>You are responsible for terminating this thread
     *                                 pool</strong>
     * @return new Webuj instance
     */
    public static Webuj build(WebujService webujService, long pollingInterval, ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0Webuj(webujService, pollingInterval, scheduledExecutorService);
    }
}
