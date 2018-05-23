package org.happyuc.webuj.contracts.token;

import org.happyuc.webuj.crypto.CipherException;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.crypto.WalletUtils;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.protocol.http.HttpService;
import org.happyuc.webuj.tx.Contract;
import org.happyuc.webuj.tx.ManagedTransaction;
import org.happyuc.webuj.utils.Convert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ERC20TokenTest {
    private Webuj webuj;

    private Credentials credentials;

    public static final String NODE_URL = "http://112.74.96.198:8550";
    public static final String PRIVATE_KEY = "";
    public static String CONTRACT_ADDR = "";

    public static final String _TO = "0xd0ca89a6d9435a6a4857c1083f165af01fbfda7d";
    public static final String SOURCE = "/home/ldc/blockchain/hucdev/data0/keystore" + "/UTC--2018-04-23T13-48-42.528788235Z--b3f1507591583ebf14b5b31d134d700c83c20fa1";

    @Before
    public void setUp() throws IOException, CipherException {
        webuj = Webuj.build(new HttpService(NODE_URL));
        // credentials = Credentials.create(PRIVATE_KEY);
        credentials = WalletUtils.loadCredentials("123456", SOURCE);
    }

    @Test
    public void deploy() {
        String name = "Token(Test)";
        String symbol = "Token";
        BigInteger price = ManagedTransaction.GAS_PRICE;
        BigInteger limit = Contract.GAS_LIMIT;
        BigInteger amount = BigInteger.valueOf(1000000);
        ERC20Token contract;
        try {
            contract = ERC20Token.deploy(webuj, credentials, price, limit, amount, name, symbol).send();
            assertTrue("Create deploy failed", contract.isValid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void load() throws Exception {
        ERC20Token contract = ERC20Token.load(CONTRACT_ADDR, webuj, credentials);
        assertTrue("Contract address does not exist", contract.isValid());
    }

    @Test
    public void transfer() throws Exception {
        ERC20Token contract = ERC20Token.load(CONTRACT_ADDR, webuj, credentials);
        BigInteger before = contract.balanceOf(credentials.getAddress()).send();
        RepTransactionReceipt rece = contract.transfer(_TO, "1", Convert.Unit.HUC, "").send();
        List<EventResponse.TransferEr> txsInfo = contract.simpleGetTxEvents(rece);
        txsInfo.forEach(EventResponse.TransferEr::print);
        BigInteger after = contract.balanceOf(credentials.getAddress()).send();
        BigDecimal balTail = Convert.fromWei(before.subtract(after).toString(), Convert.Unit.HUC);
        assertEquals("Transfer failed", balTail, BigDecimal.ONE);
    }

    @Test
    public void mulDeploy() {
        List<String> names = Arrays.asList("SouthPark", "Happy Tree Friends", "");
        names.forEach(s -> new Thread(this::deploy));
    }

}
