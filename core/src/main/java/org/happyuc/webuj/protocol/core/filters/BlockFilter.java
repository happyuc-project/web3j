package org.happyuc.webuj.protocol.core.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.response.HucLog;
import org.happyuc.webuj.protocol.core.methods.response.HucRepFilter;
import org.happyuc.webuj.protocol.core.Request;

/**
 * Handler for working with block filter requests.
 */
public class BlockFilter extends Filter<String> {

    public BlockFilter(Webuj web3j, Callback<String> callback) {
        super(web3j, callback);
    }

    @Override
    HucRepFilter sendRequest() throws IOException {
        return webuj.hucNewBlockFilter().send();
    }

    @Override
    void process(List<HucLog.LogResult> logResults) {
        for (HucLog.LogResult logResult : logResults) {
            if (logResult instanceof HucLog.Hash) {
                String blockHash = ((HucLog.Hash) logResult).get();
                callback.onEvent(blockHash);
            } else {
                throw new FilterException("Unexpected result type: " + logResult.get() + ", required Hash");
            }
        }
    }

    /**
     * Since the block filter does not support historic filters, the filterId is ignored
     * and an empty optional is returned.
     *
     * @param filterId Id of the filter for which the historic log should be retrieved
     * @return null
     */
    @Override
    protected Request<?, HucLog> getFilterLogs(BigInteger filterId) {
        return null;
    }
}

