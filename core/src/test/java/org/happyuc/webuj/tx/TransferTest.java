package org.happyuc.webuj.tx;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import org.happyuc.webuj.crypto.SampleKeys;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.response.HucGasPrice;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.utils.Convert;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransferTest extends ManagedReqRepTransactionTester {

    private RepTransactionReceipt repTransactionReceipt;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        repTransactionReceipt = prepareTransfer();
    }

    @Test
    public void testSendFunds() throws Exception {
        assertThat(Transfer.sendFunds(webuj, SampleKeys.CREDENTIALS, ADDRESS, BigDecimal.TEN, Convert.Unit.HUC).send(), is(
                repTransactionReceipt));
    }

    @Test
    public void testSendFundsAsync() throws Exception {
        assertThat(Transfer.sendFunds(webuj, SampleKeys.CREDENTIALS, ADDRESS, BigDecimal.TEN, Convert.Unit.HUC).send(), is(
                repTransactionReceipt));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTransferInvalidValue() throws Exception {
        Transfer.sendFunds(webuj, SampleKeys.CREDENTIALS, ADDRESS, new BigDecimal(0.1), Convert.Unit.WEI).send();
    }

    @SuppressWarnings("unchecked")
    private RepTransactionReceipt prepareTransfer() throws IOException {
        RepTransactionReceipt repTransactionReceipt = new RepTransactionReceipt();
        repTransactionReceipt.setTransactionHash(TRANSACTION_HASH);
        repTransactionReceipt.setStatus("0x1");
        prepareTransaction(repTransactionReceipt);

        HucGasPrice hucGasPrice = new HucGasPrice();
        hucGasPrice.setResult("0x1");

        Request<?, HucGasPrice> gasPriceRequest = mock(Request.class);
        when(gasPriceRequest.send()).thenReturn(hucGasPrice);
        when(webuj.hucGasPrice()).thenReturn((Request) gasPriceRequest);

        return repTransactionReceipt;
    }
}
