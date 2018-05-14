package org.happyuc.webuj.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

/**
 * eth_sendRawTransaction.
 */
public class EthSendRawTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
