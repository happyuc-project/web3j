package org.happyuc.webuj.contracts.token;

import org.happyuc.webuj.crypto.CipherException;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.crypto.WalletUtils;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.protocol.http.HttpService;
import org.happyuc.webuj.tx.Contract;
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
    public static String CONTRACT_ADDR = "0xe1ff0eab3b4fe5c0741eab7635314935b9a6ecd1";

    public static final String KEYSTOREDIR = "/home/ldc/blockchain/ircdev/data0/keystore/";
    public static final String SOURCE1 = KEYSTOREDIR + "UTC--2018-04-11T04-07-53" +
            ".751740912Z--82a85ec8b94779ae96e8fdfa752aa7ac3932252d";
    public static final String SOURCE2 = KEYSTOREDIR + "UTC--2018-04-23T13-48-37" +
            ".872576165Z--d0ca89a6d9435a6a4857c1083f165af01fbfda7d";
    public static final String SOURCE3 = KEYSTOREDIR + "UTC--2018-04-23T13-48-42" +
            ".528788235Z--b3f1507591583ebf14b5b31d134d700c83c20fa1";

    @Before
    public void setUp() throws IOException, CipherException {
        webuj = Webuj.build(new HttpService());
        credentials1 = WalletUtils.loadCredentials("123456", SOURCE1);
        credentials2 = WalletUtils.loadCredentials("123456", SOURCE2);
        credentials3 = WalletUtils.loadCredentials("123456", SOURCE3);
    }

    @Test
    public void deploy() throws Exception {
        Credentials credentials = credentials3;
        BigInteger price = Contract.GAS_PRICE;
        BigInteger limit = Contract.GAS_LIMIT_DEPLOY;
        String name = "Token(Test)";
        String symbol = "Token";
        long supply = 1000000L;
        long costmin = 1L;
        long costmax = 1000L;
        long costpc = 1L;
        boolean extend = false;
        HappyERC contract = HappyERC.deploy(webuj, credentials, price, limit, BigInteger.valueOf(100),
                name, symbol, supply, costmin, costmax, costpc, extend).send();
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
