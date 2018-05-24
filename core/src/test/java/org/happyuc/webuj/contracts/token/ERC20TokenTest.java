package org.happyuc.webuj.contracts.token;

import org.happyuc.webuj.crypto.CipherException;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.crypto.WalletUtils;
import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.RemoteCall;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionReceipt;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ERC20TokenTest {

    private Webuj webuj;
    private TransactionManager transactionManager;

    public static final String NODE_URL = "http://112.74.96.198:8550";
    public static final String PRIVATE_KEY = "";
    public static String CONTRACT_ADDR = "0xcce9a9caa18e1f70e35104c7e75c3abd908213a5";

    public static final String _TO = "0xd0ca89a6d9435a6a4857c1083f165af01fbfda7d";
    public static final String SOURCE = "/home/ldc/blockchain/hucdev/data0/keystore" + "/UTC--2018-04-23T13-48-42.528788235Z--b3f1507591583ebf14b5b31d134d700c83c20fa1";

    @Before
    public void setUp() throws IOException, CipherException {
        webuj = Webuj.build(new HttpService());
        Credentials credentials = WalletUtils.loadCredentials("123456", SOURCE);
        transactionManager = new FastRawTransactionManager(webuj, credentials, 10, 3000);
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
            contract = ERC20Token.deploy(webuj, transactionManager, price, limit, amount, name, symbol).send();
            assertTrue("Create deploy failed", contract.isValid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void load() throws Exception {
        ERC20Token contract = ERC20Token.load(CONTRACT_ADDR, webuj, transactionManager);
        assertTrue("Contract address does not exist", contract.isValid());
    }

    @Test
    public void transfer() {
        ERC20Token contract = ERC20Token.load(CONTRACT_ADDR, webuj, transactionManager);
        RepTransactionReceipt rece = null;
        try {
            rece = contract.transfer(_TO, "1", Convert.Unit.HUC, "").send();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(rece);
        List<EventResponse.TransferEr> txsInfo = contract.simpleGetTxEvents(rece);
        txsInfo.forEach(EventResponse.TransferEr::print);
    }

    @Test
    public void mulDeploy() {
        List<String> names = Arrays.asList("SouthPark", "Happy Tree Friends", "");
        names.forEach(s -> new Thread(this::deploy).run());
    }

    @Test
    public void mulTransfer() {
        final int limit = 10;
        final ERC20Token contract = ERC20Token.load(CONTRACT_ADDR, webuj, transactionManager);

        List<RemoteCall<RepTransactionReceipt>> remotes = new ArrayList<>();

        for (int i = 0; i < limit; i++) {
            remotes.add(contract.transfer(_TO, "1", Convert.Unit.HUC, ""));
        }

        contract.mulTransfer(remotes).forEach(txRecp -> {
            assertNotNull(txRecp);
            List<EventResponse.TransferEr> txsInfo = contract.simpleGetTxEvents(txRecp);
            txsInfo.forEach(EventResponse.TransferEr::print);
        });
    }

    @Test
    public void mulTransfer2() {
        final int limit = 10;
        final ERC20Token contract = ERC20Token.load(CONTRACT_ADDR, webuj, transactionManager);

        RepTransactionReceipt txRecp;

        for (int i = 0; i < limit; i++) {
            try {
                txRecp = contract.transfer(_TO, "1", Convert.Unit.HUC, "").sendAsync().get();
                assertNotNull(txRecp);
                List<EventResponse.TransferEr> txsInfo = contract.simpleGetTxEvents(txRecp);
                txsInfo.forEach(EventResponse.TransferEr::print);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void mulTransfer3() {
        final int limit = 10;
        final ERC20Token contract = ERC20Token.load(CONTRACT_ADDR, webuj, transactionManager);

        HucSendRepTransaction repTx;
        HucGetRepTransactionReceipt getTxRecp;
        RepTransactionReceipt txRecp;

        for (int i = 0; i < limit; i++) {
            try {
                repTx = contract.transfer2(_TO, "1", Convert.Unit.HUC, "");
                assert !repTx.hasError();

                getTxRecp = webuj.hucGetTransactionReceipt(repTx.getTransactionHash()).send();
                assert !getTxRecp.hasError();

                txRecp = getTxRecp.getResult();
                assert txRecp != null;

                List<EventResponse.TransferEr> txsInfo = contract.simpleGetTxEvents(txRecp);
                txsInfo.forEach(EventResponse.TransferEr::print);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
