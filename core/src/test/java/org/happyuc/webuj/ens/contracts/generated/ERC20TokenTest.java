package org.happyuc.webuj.ens.contracts.generated;

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

    public static final String contractAddr = "0x13f46a078f874bf70e4283f6110a3c3a609c98b4";
    public static final String _to = "0x82a85ec8b94779ae96e8fdfa752aa7ac3932252d";

    @Before
    public void setUp() {
        webuj = Webuj.build(new HttpService("http://59.111.94.209:8545/"));
        credentials = Credentials.create("d59660d73fcc0e22b754f2b7a074dff257a7c0c787f2b0f8c7b3c26d346b6d50");
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
