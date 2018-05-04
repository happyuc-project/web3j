package org.happyuc.webuj.protocol.parity.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * trace_rawTransaction
 * trace_replayTransaction.
 */
public class ParityFullTraceResponse extends Response<FullTraceInfo> {
    public FullTraceInfo getFullTraceInfo() {
        return getResult();
    }
}
