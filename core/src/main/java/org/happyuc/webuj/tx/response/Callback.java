package org.happyuc.webuj.tx.response;

import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;

/**
 * ReqTransaction receipt processor callback.
 */
public interface Callback {
    void accept(RepTransactionReceipt repTransactionReceipt);

    void exception(Exception exception);
}
