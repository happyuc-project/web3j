package org.happyuc.webuj.contracts.token;

import org.happyuc.webuj.crypto.CipherException;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.crypto.WalletUtils;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.request.ReqTransaction;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.protocol.http.HttpService;
import org.happyuc.webuj.tx.ManagedTransaction;
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
    private Credentials credentials1;
    private Credentials credentials2;
    private Credentials credentials3;
    private Credentials credentials4;
    private Credentials credentials5;

    public static final String NODE_URL = "http://112.74.96.198:8545";
    public static final String PRIVATE_KEY = "";
    public static String CONTRACT_ADDR = "0x318F54081a8706AA3B66b97eE173AE750D1B6C38";

    public static final String KEYSTOREDIR = "/home/ldc/blockchain/hucdev/data0/keystore/";
    public static final String SOURCE1 = KEYSTOREDIR + "UTC--2018-04-11T04-07-53.751740912Z--82a85ec8b94779ae96e8fdfa752aa7ac3932252d";
    public static final String SOURCE2 = KEYSTOREDIR + "UTC--2018-04-23T13-48-37.872576165Z--d0ca89a6d9435a6a4857c1083f165af01fbfda7d";
    public static final String SOURCE3 = KEYSTOREDIR + "UTC--2018-04-23T13-48-42.528788235Z--b3f1507591583ebf14b5b31d134d700c83c20fa1";
    public static final String SOURCE4 = KEYSTOREDIR + "UTC--2018-04-25T09-45-45.144366258Z--7a61b41e166d165c18b23a31b0506d95576c974a";
    public static final String SOURCE5 = KEYSTOREDIR + "UTC--2018-04-25T09-45-45.144366258Z--7a61b41e166d165c18b23a31b0506d95576c974a";

    @Before
    public void setUp() throws IOException, CipherException {
        webuj = Webuj.build(new HttpService());
        credentials1 = WalletUtils.loadCredentials("123456", SOURCE1);
        credentials2 = WalletUtils.loadCredentials("123456", SOURCE2);
        credentials3 = WalletUtils.loadCredentials("123456", SOURCE3);
        credentials4 = WalletUtils.loadCredentials("123456", SOURCE4);
        credentials4 = WalletUtils.loadCredentials("123456", SOURCE5);
    }

    @Test
    public void deploy() throws Exception {
        Credentials credentials = credentials3;
        String name = "Token(Test)";
        String symbol = "Token";
        BigInteger price = ManagedTransaction.GAS_PRICE;
        BigInteger limit = ReqTransaction.DEFAULT_GAS;
        BigInteger amount = BigInteger.valueOf(1000000);
        HappyERC contract = HappyERC.deploy(webuj, credentials, price, limit, amount, name, symbol).send();
        assertTrue("Create deploy failed", contract.isValid());
        System.out.println(contract.getContractAddress());
    }

    @Test
    public void load() throws Exception {
        Credentials credentials = credentials1;
        HappyERC contract = HappyERC.load(CONTRACT_ADDR, webuj, credentials);
        assertTrue("Contract address does not exist", contract.isValid());
    }

    @Test
    public void balanceOf() throws Exception {
        Credentials credentials = credentials1;
        HappyERC contract = HappyERC.load(CONTRACT_ADDR, webuj, credentials);
        BigInteger balance = contract.balanceOf(credentials.getAddress()).send();
        assertNotNull(balance);
        System.out.println(Convert.fromWei(balance.toString(), Convert.Unit.HUC));
    }

    @Test
    public void transfer() throws Exception {
        Credentials credentials = credentials3;
        HappyERC contract = HappyERC.load(CONTRACT_ADDR, webuj, credentials);
        RepTransactionReceipt rece = contract.transfer(credentials2.getAddress(), "1", "").send();
        assertNotNull(rece);
        List<EventResponse.TransferEr> txsInfo = contract.simpleGetTxEvents(rece);
        txsInfo.forEach(EventResponse.TransferEr::print);
    }

    @Test
    public void mulTransfer() throws Exception {
        Credentials credentials = credentials1;
        List<String> addresses = Arrays.asList(
                "0xd0ca89a6d9435a6a4857c1083f165af01fbfda7d",
                "0x7a61b41e166d165c18b23a31b0506d95576c974a",
                "0x46f6207f090a262dc8079dca37d557dc353be368");
        List<String> values = Arrays.asList("1", "2", "3");
        List<String> remarks = Arrays.asList("", "", "");

        HappyERC contract = HappyERC.load(CONTRACT_ADDR, webuj, credentials);
        RepTransactionReceipt rece = contract.mulTransfer(addresses, values, remarks).send();
        assertNotNull(rece);
        List<EventResponse.TransferEr> txsInfo = contract.simpleGetTxEvents(rece);
        txsInfo.forEach(EventResponse.TransferEr::print);
    }

}
