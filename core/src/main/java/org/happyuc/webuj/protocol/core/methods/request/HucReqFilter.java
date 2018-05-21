package org.happyuc.webuj.protocol.core.methods.request;

import java.util.Arrays;
import java.util.List;

import org.happyuc.webuj.protocol.core.DefaultBlockParameter;

/**
 * Filter implementation as per
 * <a href="https://github.com/happyuc-project/wiki/wiki/JSON-RPC#huc_newfilter">docs</a>.
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
        this(fromBlock, toBlock, Arrays.asList(address));
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
