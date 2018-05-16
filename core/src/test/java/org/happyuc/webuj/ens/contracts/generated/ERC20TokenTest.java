package org.happyuc.webuj.ens.contracts.generated;

import org.happyuc.webuj.contracts.token.ERC20Token;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.http.HttpService;
import org.happyuc.webuj.utils.Convert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public class ERC20TokenTest {
    private Webuj webuj;

    private Credentials credentials;

    public static final String contractAddr = "";
    public static final String _to = "";

    @Before
    public void setUp() {
        webuj = Webuj.build(new HttpService());
        credentials = Credentials.create("");
    }

    @Test
    public void transfer() throws Exception {
        ERC20Token contract = ERC20Token.load(contractAddr, webuj, credentials);
        BigInteger before = contract.balanceOf(credentials.getAddress()).send();
        contract.transfer(_to, BigInteger.ONE, Convert.Unit.HUC, "").send();
        BigInteger after = contract.balanceOf(credentials.getAddress()).send();
        assertEquals("Transfer 1 ERC20", Convert.fromWei(before.subtract(after), Convert.Unit.HUC).toString(), BigInteger.ONE.toString());
    }

}
