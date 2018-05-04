package org.happyuc.webuj.protocol.core.methods.request;

/**
 * Filter implementation as per <a href="https://github.com/happyuc-project/wiki/wiki/JSON-RPC#eth_newfilter">docs</a>
 */
public class ShhFilter extends Filter<ShhFilter> {
    private String to;

    public ShhFilter(String to) {
        super();
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    @Override
    ShhFilter getThis() {
        return this;
    }
}
