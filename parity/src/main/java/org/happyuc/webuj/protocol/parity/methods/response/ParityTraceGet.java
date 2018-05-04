package org.happyuc.webuj.protocol.parity.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * trace_get.
 */
public class ParityTraceGet extends Response<Trace> {
    public Trace getTrace() {
        return getResult();
    }
}
