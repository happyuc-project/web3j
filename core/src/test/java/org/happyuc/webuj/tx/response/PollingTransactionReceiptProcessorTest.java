package org.happyuc.webuj.tx.response;

import java.io.IOException;

import org.happyuc.webuj.protocol.Webuj;
import org.junit.Before;
import org.junit.Test;

import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.Response;
import org.happyuc.webuj.protocol.core.methods.response.HucGetTransactionReceipt;
import org.happyuc.webuj.protocol.core.methods.response.TransactionReceipt;
import org.happyuc.webuj.protocol.exceptions.TransactionException;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PollingTransactionReceiptProcessorTest {
    private static final String TRANSACTION_HASH = "0x00";
    private Webuj webuj;
    private long sleepDuration;
    private int attemps;
    private PollingTransactionReceiptProcessor processor;

    @Before
    public void setUp() {
        webuj = mock(Webuj.class);
        sleepDuration = 100;
        attemps = 3;
        processor = new PollingTransactionReceiptProcessor(webuj, sleepDuration, attemps);
    }

    @Test
    public void returnsTransactionReceiptWhenItIsAvailableInstantly() throws Exception {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        doReturn(requestReturning(response(transactionReceipt))).when(webuj).hucGetTransactionReceipt(TRANSACTION_HASH);

        TransactionReceipt receipt = processor.waitForTransactionReceipt(TRANSACTION_HASH);

        assertThat(receipt, sameInstance(transactionReceipt));
    }

    @Test
    public void throwsTransactionExceptionWhenReceiptIsNotAvailableInTime() throws Exception {
        doReturn(requestReturning(response(null))).when(webuj).hucGetTransactionReceipt(TRANSACTION_HASH);
        try {
            processor.waitForTransactionReceipt(TRANSACTION_HASH);
            fail("call should fail with TransactionException");
        } catch (TransactionException e) {
            // this is expected
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Response<?>> Request<String, T> requestReturning(T response) {
        Request<String, T> request = mock(Request.class);
        try {
            when(request.send()).thenReturn(response);
        } catch (IOException e) {
            // this will never happen
        }
        return request;
    }

    private static HucGetTransactionReceipt response(TransactionReceipt transactionReceipt) {
        HucGetTransactionReceipt response = new HucGetTransactionReceipt();
        response.setResult(transactionReceipt);
        return response;
    }
}
