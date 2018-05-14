package org.happyuc.webuj.protocol.scenarios;

import org.happyuc.webuj.abi.TypeReference;
import org.happyuc.webuj.abi.datatypes.Function;
import org.happyuc.webuj.abi.datatypes.Type;
import org.happyuc.webuj.abi.datatypes.Uint;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.protocol.admin.Admin;
import org.happyuc.webuj.protocol.admin.AdminFactory;
import org.happyuc.webuj.protocol.admin.methods.response.PersonalUnlockAccount;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionCount;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionReceipt;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.protocol.http.HttpService;
import org.happyuc.webuj.utils.Files;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;

import static junit.framework.TestCase.fail;

/**
 * Common methods & settings used accross scenarios.
 */
public class Scenario {

    static final BigInteger GAS_PRICE = BigInteger.valueOf(22000000000L);
    static final BigInteger GAS_LIMIT = BigInteger.valueOf(4300000);

    // testnet
    private static final String WALLET_PASSWORD = "";

    /*
    If you want to use regular Happyuc wallet addresses, provide a WALLET address variable
    "0x..." // 20 bytes (40 hex characters) & replace instances of ALICE.getAddress() with this
    WALLET address variable you've defined.
    */
    static final Credentials ALICE = Credentials.create("",  // 32 byte hex value
            "0x"  // 64 byte hex value
    );

    static final Credentials BOB = Credentials.create("",  // 32 byte hex value
            "0x"  // 64 byte hex value
    );

    private static final BigInteger ACCOUNT_UNLOCK_DURATION = BigInteger.valueOf(30);

    private static final int SLEEP_DURATION = 15000;
    private static final int ATTEMPTS = 40;

    Admin webuj;

    public Scenario() { }

    @Before
    public void setUp() {
        this.webuj = AdminFactory.build(new HttpService());
    }

    boolean unlockAccount() throws Exception {
        PersonalUnlockAccount personalUnlockAccount = webuj.personalUnlockAccount(ALICE.getAddress(), WALLET_PASSWORD, ACCOUNT_UNLOCK_DURATION).sendAsync().get();
        return personalUnlockAccount.accountUnlocked();
    }

    RepTransactionReceipt waitForTransactionReceipt(String transactionHash) throws Exception {

        RepTransactionReceipt repTransactionReceipt = getTransactionReceipt(transactionHash, SLEEP_DURATION, ATTEMPTS);

        if (repTransactionReceipt == null) {
            fail("ReqTransaction reciept not generated after " + ATTEMPTS + " attempts");
        }

        return repTransactionReceipt;
    }

    private RepTransactionReceipt getTransactionReceipt(String transactionHash, int sleepDuration, int attempts) throws Exception {

        RepTransactionReceipt receiptOptional = sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (receiptOptional == null) {
                Thread.sleep(sleepDuration);
                receiptOptional = sendTransactionReceiptRequest(transactionHash);
            } else {
                break;
            }
        }

        return receiptOptional;
    }

    private RepTransactionReceipt sendTransactionReceiptRequest(String transactionHash) throws Exception {
        HucGetRepTransactionReceipt transactionReceipt = webuj.hucGetTransactionReceipt(transactionHash).sendAsync().get();

        return transactionReceipt.getTransactionReceipt();
    }

    BigInteger getNonce(String address) throws Exception {
        HucGetRepTransactionCount hucGetRepTransactionCount = webuj.hucGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();

        return hucGetRepTransactionCount.getTransactionCount();
    }

    Function createFibonacciFunction() {
        return new Function("fibonacciNotify", Collections.<Type>singletonList(new Uint(BigInteger.valueOf(7))), Collections.<TypeReference<?>>singletonList(new TypeReference<Uint>() {}));
    }

    static String load(String filePath) throws URISyntaxException, IOException {
        URL url = Scenario.class.getClass().getResource(filePath);
        byte[] bytes = Files.readBytes(new File(url.toURI()));
        return new String(bytes);
    }

    static String getFibonacciSolidityBinary() throws Exception {
        return load("/solidity/fibonacci/build/Fibonacci.bin");
    }
}
