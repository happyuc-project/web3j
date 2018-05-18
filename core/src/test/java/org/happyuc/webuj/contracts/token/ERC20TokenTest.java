package org.happyuc.webuj.contracts.token;

import org.happyuc.webuj.crypto.CipherException;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.crypto.WalletUtils;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.WebujFactory;
import org.happyuc.webuj.protocol.http.HttpService;
import org.happyuc.webuj.tx.Contract;
import org.happyuc.webuj.tx.ManagedTransaction;
import org.happyuc.webuj.utils.Convert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ERC20TokenTest {
    private Webuj webuj;

    private Credentials credentials;

    public static final String NODE_URL = "http://112.74.96.198:8545";
    public static final String _TO = "0x82a85ec8b94779ae96e8fdfa752aa7ac3932252d";
    public static final String CONTRACT_ADDR = "0xd851b36f3f792dcd16864feabee0c78d8e41bb54";
    public static final String PRIVATE_KEY = "";
    public static final String SOURCE = "/home/ldc/blockchain/huc/keystore/" + "UTC--2018-04-11T08-26-55.177995665Z--6d3ba18195cd8dd969d81a252f27c844900cd5ba";

    @Before
    public void setUp() throws IOException, CipherException {
        webuj = WebujFactory.build(new HttpService());
        // credentials = Credentials.create(PRIVATE_KEY);
        credentials = WalletUtils.loadCredentials("123456", SOURCE);
    }

    @Test
    public void transfer() throws Exception {
        ERC20Token contract = ERC20Token.load(CONTRACT_ADDR, webuj, credentials);
        BigInteger before = contract.balanceOf(credentials.getAddress()).send();
        contract.transfer(_TO, "1", Convert.Unit.HUC, "").send();
        BigInteger after = contract.balanceOf(credentials.getAddress()).send();
        BigDecimal balTail = Convert.fromWei(before.subtract(after).toString(), Convert.Unit.HUC);
        assertEquals("Transfer 1 ERC20", balTail, BigDecimal.ONE);
    }

    @Test
    public void deploy() throws Exception {
        String name = "ERCJ(Test)";
        String symbol = "ERCJ";
        BigInteger price = ManagedTransaction.GAS_PRICE;
        BigInteger limit = Contract.GAS_LIMIT;
        BigInteger amount = BigInteger.valueOf(1000000);
        ERC20Token contract = ERC20Token.deploy(webuj, credentials, price, limit, amount, name, symbol).send();
        assertTrue("Create new contract token", contract.isValid());
    }

    @Test
    public void load() throws IOException {
        ERC20Token contract = ERC20Token.load(CONTRACT_ADDR, webuj, credentials);
        assertTrue("Load existing contract token", contract.isValid());
    }

}
