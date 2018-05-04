package org.happyuc.webuj.protocol.admin;

import java.math.BigInteger;

import org.junit.Test;

import org.happyuc.webuj.protocol.RequestTester;
import org.happyuc.webuj.protocol.core.methods.request.Transaction;
import org.happyuc.webuj.protocol.http.HttpService;

public class RequestTest extends RequestTester {
    
    private Admin webuj;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        webuj = Admin.build(httpService);
    }
    
    @Test
    public void testPersonalListAccounts() throws Exception {
        webuj.personalListAccounts().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_listAccounts\","
                + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testPersonalNewAccount() throws Exception {
        webuj.personalNewAccount("password").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_newAccount\","
                + "\"params\":[\"password\"],\"id\":1}");
    } 

    @Test
    public void testPersonalSendTransaction() throws Exception {
        webuj.personalSendTransaction(
                new Transaction(
                        "FROM",
                        BigInteger.ONE,
                        BigInteger.TEN,
                        BigInteger.ONE,
                        "TO",
                        BigInteger.ZERO,
                        "DATA"
                ),
                "password"
        ).send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_sendTransaction\",\"params\":[{\"from\":\"FROM\",\"to\":\"TO\",\"gas\":\"0x1\",\"gasPrice\":\"0xa\",\"value\":\"0x0\",\"data\":\"0xDATA\",\"nonce\":\"0x1\"},\"password\"],\"id\":1}");
        //CHECKSTYLE:ON
    }   

    @Test
    public void testPersonalUnlockAccount() throws Exception {
        webuj.personalUnlockAccount(
                "0xfc390d8a8ddb591b010fda52f4db4945742c3809", "hunter2", BigInteger.ONE).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_unlockAccount\","
                + "\"params\":[\"0xfc390d8a8ddb591b010fda52f4db4945742c3809\",\"hunter2\",1],"
                + "\"id\":1}");
    }

    @Test
    public void testPersonalUnlockAccountNoDuration() throws Exception {
        webuj.personalUnlockAccount("0xfc390d8a8ddb591b010fda52f4db4945742c3809", "hunter2").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"personal_unlockAccount\","
                + "\"params\":[\"0xfc390d8a8ddb591b010fda52f4db4945742c3809\",\"hunter2\",null],"
                + "\"id\":1}");
    }
}
