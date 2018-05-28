package org.happyuc.webuj.contracts.token;

import org.happyuc.webuj.crypto.CipherException;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.crypto.WalletUtils;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.protocol.http.HttpService;
import org.happyuc.webuj.tx.Contract;
import org.happyuc.webuj.tx.FastRawTransactionManager;
import org.happyuc.webuj.tx.ManagedTransaction;
import org.happyuc.webuj.tx.TransactionManager;
import org.happyuc.webuj.utils.Convert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HappyERCTest {

    private Webuj webuj;
    private Credentials credentials;
    private TransactionManager transactionManager;

    public static final String NODE_URL = "http://112.74.96.198:8550";
    public static final String PRIVATE_KEY = "";
    public static String CONTRACT_ADDR = "0x8967553d299a734dbd7ed7b34bc46ff0c37c2c86";

    public static final String _TO = "0xd0ca89a6d9435a6a4857c1083f165af01fbfda7d";
    public static final String SOURCE = "/home/ldc/blockchain/hucdev/data0/keystore" + "/UTC--2018-04-23T13-48-42.528788235Z--b3f1507591583ebf14b5b31d134d700c83c20fa1";

    @Before
    public void setUp() throws IOException, CipherException {
        webuj = Webuj.build(new HttpService());
        credentials = WalletUtils.loadCredentials("123456", SOURCE);
        transactionManager = new FastRawTransactionManager(webuj, credentials, 10, 3000);
    }

    @Test
    public void deploy() {
        String name = "Token(Test)";
        String symbol = "Token";
        BigInteger price = ManagedTransaction.GAS_PRICE;
        BigInteger limit = Contract.GAS_LIMIT;
        BigInteger amount = BigInteger.valueOf(1000000);
        HappyERC contract;
        try {
            contract = HappyERC.deploy(webuj, credentials, price, limit, amount, name, symbol).send();
            assertTrue("Create deploy failed", contract.isValid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void load() throws Exception {
        HappyERC contract = HappyERC.load(CONTRACT_ADDR, webuj, credentials);
        assertTrue("Contract address does not exist", contract.isValid());
    }

    @Test
    public void balanceOf() throws Exception {
        HappyERC contract = HappyERC.load(CONTRACT_ADDR, webuj, credentials);
        BigInteger balance = contract.balanceOf(credentials.getAddress()).send();
        assertNotNull(balance);
        System.out.println(Convert.fromWei(balance.toString(), Convert.Unit.HUC));
    }

    @Test
    public void transfer() {
        HappyERC contract = HappyERC.load(CONTRACT_ADDR, webuj, credentials);
        RepTransactionReceipt rece = null;
        try {
            rece = contract.transfer(_TO, "1", "").send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(rece);
        List<EventResponse.TransferEr> txsInfo = contract.simpleGetTxEvents(rece);
        txsInfo.forEach(EventResponse.TransferEr::print);
    }

    @Test
    public void mulTransfer() {
        List<String> addresses = Arrays.asList(
                "0xd0ca89a6d9435a6a4857c1083f165af01fbfda7d",
                "0x7a61b41e166d165c18b23a31b0506d95576c974a",
                "0x46f6207f090a262dc8079dca37d557dc353be368");
        List<String> values = Arrays.asList("1", "2", "3");
        List<String> remarks = Arrays.asList("", "", "");

        HappyERC contract = HappyERC.load(CONTRACT_ADDR, webuj, credentials);
        RepTransactionReceipt rece = null;
        try {
            rece = contract.mulTransfer(addresses, values, remarks).send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(rece);
        List<EventResponse.TransferEr> txsInfo = contract.simpleGetTxEvents(rece);
        txsInfo.forEach(EventResponse.TransferEr::print);
    }

}
