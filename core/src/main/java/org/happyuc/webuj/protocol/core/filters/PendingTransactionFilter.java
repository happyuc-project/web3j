package org.happyuc.webuj.protocol.core.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.response.HucRepFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucLog;

/**
 * Handler for working with transaction filter requests.
 */
public class PendingTransactionFilter extends Filter<String> {

    public PendingTransactionFilter(Webuj webuj, Callback<String> callback) {
        super(webuj, callback);
    }

    @Override
    HucRepFilter sendRequest() throws IOException {
        return webuj.hucNewPendingTransactionFilter().send();
    }

    @Override
    void process(List<HucLog.LogResult> logResults) {
        for (HucLog.LogResult logResult : logResults) {
            if (logResult instanceof HucLog.Hash) {
                String transactionHash = ((HucLog.Hash) logResult).get();
                callback.onEvent(transactionHash);
            } else {
                throw new FilterException("Unexpected result type: " + logResult.get() + ", required Hash");
            }
        }
    }

    /**
     * Since the pending transaction filter does not support historic filters,
     * the filterId is ignored and an empty optional is returned
     *
     * @param filterId Id of the filter for which the historic log should be retrieved
     * @return Optional.empty()
     */
    @Override
    protected Optional<Request<?, HucLog>> getFilterLogs(BigInteger filterId) {
        return Optional.empty();
    }
}

