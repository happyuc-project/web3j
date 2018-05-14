package org.happyuc.webuj.protocol.core.methods.request;

import org.happyuc.webuj.protocol.core.DefaultBlockParameter;

import java.util.Collections;
import java.util.List;

/**
 * Filter implementation as per
 * <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#huc_newfilter">docs</a>.
 */
public class HucReqFilter extends Filter<HucReqFilter> {
    private DefaultBlockParameter fromBlock;  // optional, params - defaults to latest for both
    private DefaultBlockParameter toBlock;
    private List<String> address;  // spec. implies this can be single address as string or list

    public HucReqFilter() {
        super();
    }

    public HucReqFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock, List<String> address) {
        super();
        this.fromBlock = fromBlock;
        this.toBlock = toBlock;
        this.address = address;
    }

    public HucReqFilter(DefaultBlockParameter fromBlock, DefaultBlockParameter toBlock, String address) {
        this(fromBlock, toBlock, Collections.singletonList(address));
    }

    public DefaultBlockParameter getFromBlock() {
        return fromBlock;
    }

    public DefaultBlockParameter getToBlock() {
        return toBlock;
    }

    public List<String> getAddress() {
        return address;
    }

    @Override
    HucReqFilter getThis() {
        return this;
    }
}
