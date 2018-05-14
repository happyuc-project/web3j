package org.happyuc.webuj.protocol.core.filters;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.request.HucReqFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucLog;
import org.happyuc.webuj.protocol.core.methods.response.HucRepFilter;
import org.happyuc.webuj.protocol.core.methods.response.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * Log filter handler.
 */
public class LogFilter extends Filter<Log> {

    private final HucReqFilter hucReqFilter;

    public LogFilter(Webuj web3j, Callback<Log> callback, HucReqFilter hucReqFilter) {
        super(web3j, callback);
        this.hucReqFilter = hucReqFilter;
    }


    @Override
    HucRepFilter sendRequest() throws IOException {
        return webuj.hucNewFilter(hucReqFilter).send();
    }

    @Override
    void process(List<HucLog.LogResult> logResults) {
        for (HucLog.LogResult logResult : logResults) {
            if (logResult instanceof HucLog.LogObject) {
                Log log = ((HucLog.LogObject) logResult).get();
                callback.onEvent(log);
            } else {
                throw new FilterException("Unexpected result type: " + logResult.get() + " required LogObject");
            }
        }
    }

    @Override
    protected Request<?, HucLog> getFilterLogs(BigInteger filterId) {
        return webuj.hucGetFilterLogs(filterId);
    }
}
