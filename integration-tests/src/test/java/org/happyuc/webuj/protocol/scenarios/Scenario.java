package org.happyuc.webuj.protocol.scenarios;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;

import org.junit.Before;

import org.happyuc.webuj.abi.TypeReference;
import org.happyuc.webuj.abi.datatypes.Function;
import org.happyuc.webuj.abi.datatypes.Uint;
import org.happyuc.webuj.crypto.Credentials;
import org.happyuc.webuj.protocol.admin.Admin;
import org.happyuc.webuj.protocol.admin.methods.response.PersonalUnlockAccount;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.methods.response.HucGetTransactionCount;
import org.happyuc.webuj.protocol.core.methods.response.HucGetTransactionReceipt;
import org.happyuc.webuj.protocol.core.methods.response.TransactionReceipt;
import org.happyuc.webuj.protocol.http.HttpService;

import static junit.framework.TestCase.fail;

/**
 * Common methods & settings used accross scenarios.
 */
public class Scenario {

    static final BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);
    static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);

    // testnet
    private static final String WALLET_PASSWORD = "";

    /*
    If you want to use regular HappyUC wallet addresses, provide a WALLET address variable
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
        this.webuj = Admin.build(new HttpService());
    }

    boolean unlockAccount() throws Exception {
        PersonalUnlockAccount personalUnlockAccount = webuj.personalUnlockAccount(ALICE.getAddress(), WALLET_PASSWORD, ACCOUNT_UNLOCK_DURATION).sendAsync().get();
        return personalUnlockAccount.accountUnlocked();
    }

    TransactionReceipt waitForTransactionReceipt(String transactionHash) throws Exception {

        Optional<TransactionReceipt> transactionReceiptOptional = getTransactionReceipt(transactionHash, SLEEP_DURATION, ATTEMPTS);

        if (!transactionReceiptOptional.isPresent()) {
            fail("Transaction receipt not generated after " + ATTEMPTS + " attempts");
        }

        return transactionReceiptOptional.get();
    }

    private Optional<TransactionReceipt> getTransactionReceipt(String transactionHash, int sleepDuration, int attempts) throws Exception {

        Optional<TransactionReceipt> receiptOptional = sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (!receiptOptional.isPresent()) {
                Thread.sleep(sleepDuration);
                receiptOptional = sendTransactionReceiptRequest(transactionHash);
            } else {
                break;
            }
        }

        return receiptOptional;
    }

    private Optional<TransactionReceipt> sendTransactionReceiptRequest(String transactionHash) throws Exception {
        HucGetTransactionReceipt transactionReceipt = webuj.hucGetTransactionReceipt(transactionHash).sendAsync().get();

        return transactionReceipt.getTransactionReceipt();
    }

    BigInteger getNonce(String address) throws Exception {
        HucGetTransactionCount hucGetTransactionCount = webuj.hucGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();

        return hucGetTransactionCount.getTransactionCount();
    }

    Function createFibonacciFunction() {
        return new Function("fibonacciNotify", Collections.singletonList(new Uint(BigInteger.valueOf(7))), Collections.singletonList(new TypeReference<Uint>() {}));
    }

    static String load(String filePath) throws URISyntaxException, IOException {
        URL url = Scenario.class.getClass().getResource(filePath);
        byte[] bytes = Files.readAllBytes(Paths.get(url.toURI()));
        return new String(bytes);
    }

    static String getFibonacciSolidityBinary() throws Exception {
        return load("/solidity/fibonacci/build/Fibonacci.bin");
    }
}
