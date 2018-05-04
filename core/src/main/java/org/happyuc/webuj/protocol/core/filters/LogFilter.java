package org.happyuc.webuj.protocol.core.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.happyuc.webuj.protocol.webuj;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.response.HucFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucLog;
import org.happyuc.webuj.protocol.core.methods.response.Log;

/**
 * Log filter handler.
 */
public class LogFilter extends Filter<Log> {

    private final org.happyuc.webuj.protocol.core.methods.request.HucFilter hucFilter;

    public LogFilter(
            webuj webuj, Callback<Log> callback,
            org.happyuc.webuj.protocol.core.methods.request.HucFilter hucFilter) {
        super(webuj, callback);
        this.hucFilter = hucFilter;
    }


    @Override
    HucFilter sendRequest() throws IOException {
        return webuj.hucNewFilter(ethFilter).send();
    }

    @Override
    void process(List<HucLog.LogResult> logResults) {
        for (HucLog.LogResult logResult : logResults) {
            if (logResult instanceof HucLog.LogObject) {
                Log log = ((HucLog.LogObject) logResult).get();
                callback.onEvent(log);
            } else {
                throw new FilterException(
                        "Unexpected result type: " + logResult.get() + " required LogObject");
            }
        }
    }

    @Override
    protected Optional<Request<?, HucLog>> getFilterLogs(BigInteger filterId) {
        return Optional.of(webuj.hucGetFilterLogs(filterId));
    }
}
