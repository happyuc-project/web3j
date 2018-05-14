package org.happyuc.webuj.protocol.core.filters;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.Response;
import org.happyuc.webuj.protocol.core.methods.response.HucLog;
import org.happyuc.webuj.protocol.core.methods.response.HucUninstallFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucRepFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * Class for creating managed filter requests with callbacks.
 */
public abstract class Filter<T> {

    private static final Logger log = LoggerFactory.getLogger(Filter.class);

    final Webuj webuj;
    final Callback<T> callback;

    private volatile BigInteger filterId;

    private ScheduledFuture<?> schedule;

    public Filter(Webuj webuj, Callback<T> callback) {
        this.webuj = webuj;
        this.callback = callback;
    }

    public void run(ScheduledExecutorService scheduledExecutorService, long blockTime) {
        try {
            final HucRepFilter hucRepFilter = sendRequest();
            if (hucRepFilter.hasError()) {
                throwException(hucRepFilter.getError());
            }

            filterId = hucRepFilter.getFilterId();

            scheduledExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    Filter.this.getInitialFilterLogs();
                }
            });

            /*
            We want the filter to be resilient against client issues. On numerous occasions
            users have reported socket timeout exceptions when connected over HTTP to Geth and
            Parity clients. For examples, refer to
            https://github.com/web3j/web3j/issues/144 and
            https://github.com/ethereum/go-ethereum/issues/15243.

            Hence we consume errors and log them as errors, allowing our polling for changes to
            resume. The downside of this approach is that users will not be notified of
            downstream connection issues. But given the intermittent nature of the connection
            issues, this seems like a reasonable compromise.

            The alternative approach would be to have another thread that blocks waiting on
            schedule.get(), catching any Exceptions thrown, and passing them back up to the
            caller. However, the user would then be required to recreate subscriptions manually
            which isn't ideal given the aforementioned issues.
            */
            schedule = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        Filter.this.pollFilter(hucRepFilter);
                    } catch (Throwable e) {
                        // All exceptions must be caught, otherwise our job terminates without
                        // any notification
                        log.error("Error sending request", e);
                    }
                }
            }, 0, blockTime, TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            throwException(e);
        }
    }

    private void getInitialFilterLogs() {
        try {
            Request<?, HucLog> request = this.getFilterLogs(this.filterId);
            HucLog hucLog = null;
            if (request != null) {
                hucLog = request.send();
            } else {
                hucLog = new HucLog();
                hucLog.setResult(Collections.<HucLog.LogResult>emptyList());
            }
            process(hucLog.getLogs());

        } catch (IOException e) {
            throwException(e);
        }
    }

    private void pollFilter(HucRepFilter hucRepFilter) {
        HucLog hucLog = null;
        try {
            hucLog = webuj.hucGetFilterChanges(filterId).send();
        } catch (IOException e) {
            throwException(e);
        }
        if (hucLog.hasError()) {
            throwException(hucLog.getError());
        } else {
            process(hucLog.getLogs());
        }
    }

    abstract HucRepFilter sendRequest() throws IOException;

    abstract void process(List<HucLog.LogResult> logResults);

    public void cancel() {
        schedule.cancel(false);

        try {
            HucUninstallFilter hucUninstallFilter = webuj.hucUninstallFilter(filterId).send();
            if (hucUninstallFilter.hasError()) {
                throwException(hucUninstallFilter.getError());
            }

            if (!hucUninstallFilter.isUninstalled()) {
                throw new FilterException("Filter with id '" + filterId + "' failed to uninstall");
            }
        } catch (IOException e) {
            throwException(e);
        }
    }

    /**
     * Retrieves historic filters for the filter with the given id.
     * Getting historic logs is not supported by all filters.
     * If not the method should return an empty HucLog object
     *
     * @param filterId Id of the filter for which the historic log should be retrieved
     * @return Historic logs, or an empty optional if the filter cannot retrieve historic logs
     */
    protected abstract Request<?, HucLog> getFilterLogs(BigInteger filterId);

    void throwException(Response.Error error) {
        throw new FilterException("Invalid request: " + (error == null ? "Unknown Error" : error.getMessage()));
    }

    void throwException(Throwable cause) {
        throw new FilterException("Error sending request", cause);
    }
}

