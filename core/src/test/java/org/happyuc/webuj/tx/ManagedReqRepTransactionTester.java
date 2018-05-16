package org.happyuc.webuj.tx;

import java.io.IOException;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionCount;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionReceipt;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.junit.Before;

import org.happyuc.webuj.crypto.SampleKeys;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.Request;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public abstract class ManagedReqRepTransactionTester {

    static final String ADDRESS = "0x3d6cb163f7c72d20b0fcd6baae5889329d138a4a";
    static final String TRANSACTION_HASH = "0xHASH";
    protected Webuj webuj;

    @Before
    public void setUp() throws Exception {
        webuj = mock(Webuj.class);
    }

    void prepareTransaction(RepTransactionReceipt repTransactionReceipt) throws IOException {
        prepareNonceRequest();
        prepareTransactionRequest();
        prepareTransactionReceipt(repTransactionReceipt);
    }

    @SuppressWarnings("unchecked")
    void prepareNonceRequest() throws IOException {
        HucGetRepTransactionCount hucGetRepTransactionCount = new HucGetRepTransactionCount();
        hucGetRepTransactionCount.setResult("0x1");

        Request<?, HucGetRepTransactionCount> transactionCountRequest = mock(Request.class);
        when(transactionCountRequest.send()).thenReturn(hucGetRepTransactionCount);
        when(webuj.hucGetTransactionCount(SampleKeys.ADDRESS, DefaultBlockParameterName.PENDING)).thenReturn((Request) transactionCountRequest);
    }

    @SuppressWarnings("unchecked")
    void prepareTransactionRequest() throws IOException {
        HucSendRepTransaction hucSendRepTransaction = new HucSendRepTransaction();
        hucSendRepTransaction.setResult(TRANSACTION_HASH);

        Request<?, HucSendRepTransaction> rawTransactionRequest = mock(Request.class);
        when(rawTransactionRequest.send()).thenReturn(hucSendRepTransaction);
        when(webuj.hucSendRawTransaction(any(String.class))).thenReturn((Request) rawTransactionRequest);
    }

    @SuppressWarnings("unchecked")
    void prepareTransactionReceipt(RepTransactionReceipt repTransactionReceipt) throws IOException {
        HucGetRepTransactionReceipt hucGetRepTransactionReceipt = new HucGetRepTransactionReceipt();
        hucGetRepTransactionReceipt.setResult(repTransactionReceipt);

        Request<?, HucGetRepTransactionReceipt> getTransactionReceiptRequest = mock(Request.class);
        when(getTransactionReceiptRequest.send()).thenReturn(hucGetRepTransactionReceipt);
        when(webuj.hucGetTransactionReceipt(TRANSACTION_HASH)).thenReturn((Request) getTransactionReceiptRequest);
    }
}
