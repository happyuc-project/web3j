package org.happyuc.webuj.tx;

import java.io.IOException;

import org.junit.Before;

import org.happyuc.webuj.crypto.SampleKeys;
import org.happyuc.webuj.protocol.webuj;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.mhucods.response.HucGetTransactionCount;
import org.happyuc.webuj.protocol.core.mhucods.response.HucGetTransactionReceipt;
import org.happyuc.webuj.protocol.core.mhucods.response.HucSendTransaction;
import org.happyuc.webuj.protocol.core.mhucods.response.TransactionReceipt;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public abstract class ManagedTransactionTester {

    static final String ADDRESS = "0x3d6cb163f7c72d20b0fcd6baae5889329d138a4a";
    static final String TRANSACTION_HASH = "0xHASH";
    protected webuj webuj;

    @Before
    public void setUp() throws Exception {
        webuj = mock(webuj.class);
    }

    void prepareTransaction(TransactionReceipt transactionReceipt) throws IOException {
        prepareNonceRequest();
        prepareTransactionRequest();
        prepareTransactionReceipt(transactionReceipt);
    }

    @SuppressWarnings("unchecked")
    void prepareNonceRequest() throws IOException {
        HucGetTransactionCount hucGetTransactionCount = new HucGetTransactionCount();
        hucGetTransactionCount.setResult("0x1");

        Request<?, HucGetTransactionCount> transactionCountRequest = mock(Request.class);
        when(transactionCountRequest.send())
                .thenReturn(hucGetTransactionCount);
        when(webuj.hucGetTransactionCount(SampleKeys.ADDRESS, DefaultBlockParameterName.PENDING))
                .thenReturn((Request) transactionCountRequest);
    }

    @SuppressWarnings("unchecked")
    void prepareTransactionRequest() throws IOException {
        HucSendTransaction hucSendTransaction = new HucSendTransaction();
        hucSendTransaction.setResult(TRANSACTION_HASH);

        Request<?, HucSendTransaction> rawTransactionRequest = mock(Request.class);
        when(rawTransactionRequest.send()).thenReturn(hucSendTransaction);
        when(webuj.hucSendRawTransaction(any(String.class)))
                .thenReturn((Request) rawTransactionRequest);
    }

    @SuppressWarnings("unchecked")
    void prepareTransactionReceipt(TransactionReceipt transactionReceipt) throws IOException {
        HucGetTransactionReceipt hucGetTransactionReceipt = new HucGetTransactionReceipt();
        hucGetTransactionReceipt.setResult(transactionReceipt);

        Request<?, HucGetTransactionReceipt> getTransactionReceiptRequest = mock(Request.class);
        when(getTransactionReceiptRequest.send())
                .thenReturn(hucGetTransactionReceipt);
        when(webuj.hucGetTransactionReceipt(TRANSACTION_HASH))
                .thenReturn((Request) getTransactionReceiptRequest);
    }
}
