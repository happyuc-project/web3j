package org.happyuc.webuj.protocol.parity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import org.happyuc.webuj.crypto.WalletFile;
import org.happyuc.webuj.protocol.RequestTester;
import org.happyuc.webuj.protocol.core.DefaultBlockParameter;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterName;
import org.happyuc.webuj.protocol.core.DefaultBlockParameterNumber;
import org.happyuc.webuj.protocol.core.methods.request.Transaction;
import org.happyuc.webuj.protocol.http.HttpService;
import org.happyuc.webuj.protocol.parity.methods.request.Derivation;
import org.happyuc.webuj.protocol.parity.methods.request.TraceFilter;
import org.happyuc.webuj.utils.Numeric;

public class RequestTest extends RequestTester {

    private Parity webuj;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        webuj = Parity.build(httpService);
    }

    @Test
    public void testParityAllAccountsInfo() throws Exception {
        webuj.parityAllAccountsInfo().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_allAccountsInfo\","
                + "\"params\":[],\"id\":1}");
    }
    
    @Test
    public void testParityChangePassword() throws Exception {
        //CHECKSTYLE:OFF
        webuj.parityChangePassword("0x407d73d8a49eeb85d32cf465507dd71d507100c1", "hunter2", "bazqux5").send();
        
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_changePassword\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"hunter2\",\"bazqux5\"],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    @Test
    public void testParityDeriveAddressHash() throws Exception { 
        Derivation hashType = Derivation.createDerivationHash(
                "0x2547ea3382099c7c76d33dd468063b32d41016aacb02cbd51ebc14ff5d2b6a43", "hard");
        
        webuj.parityDeriveAddressHash("0x407d73d8a49eeb85d32cf465507dd71d507100c1", 
                "hunter2", hashType, false).send();
        
        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_deriveAddressHash\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"hunter2\","
                + "{\"hash\":\"0x2547ea3382099c7c76d33dd468063b32d41016aacb02cbd51ebc14ff5d2b6a43\",\"type\":\"hard\"},false],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    @Test
    public void testParityDeriveAddressIndex() throws Exception {               
        List<Derivation> indexType = new ArrayList<>();
        indexType.add(Derivation.createDerivationIndex(1, "hard"));
        indexType.add(Derivation.createDerivationIndex(2, "soft"));
        
        //CHECKSTYLE:OFF
        webuj.parityDeriveAddressIndex("0x407d73d8a49eeb85d32cf465507dd71d507100c1", "hunter2", indexType, false).send();
        
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_deriveAddressIndex\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"hunter2\","
                + "[{\"index\":1,\"type\":\"hard\"},{\"index\":2,\"type\":\"soft\"}],false],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    @Test
    public void testParityExportAccount() throws Exception {
        webuj.parityExportAccount("0x407d73d8a49eeb85d32cf465507dd71d507100c1", "hunter2").send();
        
        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_exportAccount\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"hunter2\"],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    @Test
    public void testParityGetDappAddresses() throws Exception {
        webuj.parityGetDappAddresses("web").send();
        
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_getDappAddresses\","
                + "\"params\":[\"web\"],\"id\":1}");
    }
    
    @Test
    public void testParityGetDefaultDappAddress() throws Exception {
        webuj.parityGetDappDefaultAddress("web").send();
        
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_getDappDefaultAddress\","
                + "\"params\":[\"web\"],\"id\":1}");
    }
    
    public void testParityGetNewDappsAddresses() throws Exception {
        webuj.parityAllAccountsInfo().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_getNewDappsAddresses\","
                + "\"params\":[],\"id\":1}");
    }
    
    public void testParityGetNewDappsDefaultAddress() throws Exception {
        webuj.parityGetNewDappsDefaultAddress().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_getNewDappsDefaultAddress\","
                + "\"params\":[],\"id\":1}");
    }
    
    public void testParityImportGhucAccounts() throws Exception {
        ArrayList<String> ghucAccounts = new ArrayList<>();
        ghucAccounts.add("0x407d73d8a49eeb85d32cf465507dd71d507100c1");
        webuj.parityImportGhucAccounts(ghucAccounts).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_importGhucAccounts\","
                + "\"params\":[[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"]],\"id\":1}");
    }
    
    public void testParityKillAccount() throws Exception {
        webuj.parityKillAccount("0x407d73d8a49eeb85d32cf465507dd71d507100c1","hunter2").send();
        
        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_killAccount\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"hunter2\"],\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testParityListAccountsNoAccountOffsetNoBlockTag() throws Exception {
        BigInteger maxQuantityReturned = BigInteger.valueOf(100);
        webuj.parityListAccounts(maxQuantityReturned, null, null).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_listAccounts\","
                + "\"params\":[100,null],\"id\":1}");
    }

    @Test
    public void testParityListAccountsNoAccountOffsetWithBlockTag() throws Exception {
        BigInteger maxQuantityReturned = BigInteger.valueOf(100);
        DefaultBlockParameter blockParameter = new DefaultBlockParameterNumber(BigInteger.ONE);
        webuj.parityListAccounts(maxQuantityReturned, null, blockParameter).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_listAccounts\","
                + "\"params\":[100,null,\"0x1\"],\"id\":1}");
    }

    @Test
    public void testParityListAccountsWithAccountOffsetWithBlockTag() throws Exception {
        BigInteger maxQuantityReturned = BigInteger.valueOf(100);
        DefaultBlockParameter blockParameter = DefaultBlockParameterName.LATEST;
        webuj.parityListAccounts(maxQuantityReturned,
                "0x407d73d8a49eeb85d32cf465507dd71d507100c1", blockParameter).send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_listAccounts\","
                + "\"params\":[100,\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"latest\"],\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testParityListAccountsWithAccountOffsetNoBlockTag() throws Exception {
        BigInteger maxQuantityReturned = BigInteger.valueOf(100);
        webuj.parityListAccounts(maxQuantityReturned,
                "0x407d73d8a49eeb85d32cf465507dd71d507100c1", null).send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_listAccounts\","
                + "\"params\":[100,\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"],\"id\":1}");
        //CHECKSTYLE:ON
    }

    public void testParityListGhucAccounts() throws Exception {
        webuj.parityListGhucAccounts().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_listGhucAccounts\","
                + "\"params\":[],\"id\":1}");
    }
    
    public void testParityListRecentDapps() throws Exception {
        webuj.parityListRecentDapps().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_listRecentDapps\","
                + "\"params\":[],\"id\":1}");
    }
    
    @Test
    public void testParityNewAccountFromPhrase() throws Exception {
        webuj.parityNewAccountFromPhrase("phrase", "password").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_newAccountFromPhrase\","
                + "\"params\":[\"phrase\",\"password\"],\"id\":1}");
    }
    
    @Test
    public void testParityNewAccountFromSecret() throws Exception {
        //CHECKSTYLE:OFF
        webuj.parityNewAccountFromSecret("0x1db2c0cf57505d0f4a3d589414f0a0025ca97421d2cd596a9486bc7e2cd2bf8b", "password").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_newAccountFromSecret\","
                + "\"params\":[\"0x1db2c0cf57505d0f4a3d589414f0a0025ca97421d2cd596a9486bc7e2cd2bf8b\",\"password\"],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    @Test
    public void testParityNewAccountFromWallet() throws Exception {
        WalletFile walletFile = new WalletFile();
        walletFile.setAddress("0x...");

        WalletFile.Crypto crypto = new WalletFile.Crypto();
        crypto.setCipher("CIPHER");
        crypto.setCiphertext("CIPHERTEXT");
        walletFile.setCrypto(crypto);

        WalletFile.CipherParams cipherParams = new WalletFile.CipherParams();
        cipherParams.setIv("IV");
        crypto.setCipherparams(cipherParams);

        crypto.setKdf("KDF");
        WalletFile.ScryptKdfParams kdfParams = new WalletFile.ScryptKdfParams();
        kdfParams.setDklen(32);
        kdfParams.setN(1);
        kdfParams.setP(10);
        kdfParams.setR(100);
        kdfParams.setSalt("SALT");
        crypto.setKdfparams(kdfParams);

        crypto.setMac("MAC");
        walletFile.setCrypto(crypto);
        walletFile.setId("cab06c9e-79a9-48ea-afc7-d3bdb3a59526");
        walletFile.setVersion(1);

        webuj.parityNewAccountFromWallet(walletFile, "password").send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_newAccountFromWallet\",\"params\":[{\"address\":\"0x...\",\"id\":\"cab06c9e-79a9-48ea-afc7-d3bdb3a59526\",\"version\":1,\"crypto\":{\"cipher\":\"CIPHER\",\"ciphertext\":\"CIPHERTEXT\",\"cipherparams\":{\"iv\":\"IV\"},\"kdf\":\"KDF\",\"kdfparams\":{\"dklen\":32,\"n\":1,\"p\":10,\"r\":100,\"salt\":\"SALT\"},\"mac\":\"MAC\"}},\"password\"],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    public void testParityRemoveAddress() throws Exception {
        webuj.parityRemoveAddress("0x407d73d8a49eeb85d32cf465507dd71d507100c1").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_removeAddress\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"],\"id\":1}");
    }
    
    @Test
    public void testParitySetAccountMeta() throws Exception {
        Map<String, Object> meta = new HashMap<>(1);
        meta.put("foo", "bar");
        webuj.paritySetAccountMeta("0xfc390d8a8ddb591b010fda52f4db4945742c3809", meta).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_setAccountMeta\","
                + "\"params\":[\"0xfc390d8a8ddb591b010fda52f4db4945742c3809\",{\"foo\":\"bar\"}],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParitySetAccountName() throws Exception {
        webuj.paritySetAccountName("0xfc390d8a8ddb591b010fda52f4db4945742c3809", "Savings")
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_setAccountName\","
                + "\"params\":[\"0xfc390d8a8ddb591b010fda52f4db4945742c3809\",\"Savings\"],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParitySetDappAddresses() throws Exception {
        ArrayList<String> dAppAddresses = new ArrayList<>();
        dAppAddresses.add("0x407d73d8a49eeb85d32cf465507dd71d507100c1");
        webuj.paritySetDappAddresses("web", dAppAddresses)
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_setDappAddresses\","
                + "\"params\":[\"web\",[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"]],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParitySetDappDefaultAddress() throws Exception {
        webuj.paritySetDappDefaultAddress("web", "0x407d73d8a49eeb85d32cf465507dd71d507100c1")
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_setDappDefaultAddress\","
                + "\"params\":[\"web\",\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParitySetNewDappsAddresses() throws Exception {
        ArrayList<String> dAppAddresses = new ArrayList<>();
        dAppAddresses.add("0x407d73d8a49eeb85d32cf465507dd71d507100c1");
        webuj.paritySetNewDappsAddresses(dAppAddresses)
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_setNewDappsAddresses\","
                + "\"params\":[[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"]],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParitySetNewDappsDefaultAddress() throws Exception {
        webuj.paritySetNewDappsDefaultAddress("0x407d73d8a49eeb85d32cf465507dd71d507100c1")
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_setNewDappsDefaultAddress\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParityTestPassword() throws Exception {
        webuj.parityTestPassword("0x407d73d8a49eeb85d32cf465507dd71d507100c1", "hunter2")
                .send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_testPassword\","
                + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"hunter2\"],"
                + "\"id\":1}");
    }
    
    @Test
    public void testParitySignMessage() throws Exception {
        //CHECKSTYLE:OFF
        webuj.paritySignMessage("0xc171033d5cbff7175f29dfd3a63dda3d6f8f385e","password1","0xbc36789e7a1e281436464229828f817d6612f7b477d66591ff96a9e064bcc98a").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"parity_signMessage\","
                + "\"params\":[\"0xc171033d5cbff7175f29dfd3a63dda3d6f8f385e\",\"password1\","
                + "\"0xbc36789e7a1e281436464229828f817d6612f7b477d66591ff96a9e064bcc98a\"],\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    @Test
    public void testTraceCall() throws Exception {
        Transaction transaction = Transaction.createFunctionCallTransaction(
                "0xc171033d5cbff7175f29dfd3a63dda3d6f8f385e",
                BigInteger.ONE,
                Numeric.toBigInt("0x9184e72a000"),
                Numeric.toBigInt("0x76c0"),
                "0xb60e8dd61c5d32be8058bb8eb970870f07233155",
                Numeric.toBigInt("0x9184e72a"),
                "0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb"
                    + "970870f072445675058bb8eb970870f072445675"
        );
        webuj.traceCall(
                transaction,
                Arrays.asList("trace", "vmTrace", "stateDiff"),
                DefaultBlockParameterName.LATEST
        ).send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"trace_call\","
                + "\"params\":["
                + "{\"from\":\"0xc171033d5cbff7175f29dfd3a63dda3d6f8f385e\","
                + "\"to\":\"0xb60e8dd61c5d32be8058bb8eb970870f07233155\","
                + "\"gas\":\"0x76c0\","
                + "\"gasPrice\":\"0x9184e72a000\","
                + "\"value\":\"0x9184e72a\","
                + "\"data\":\"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\","
                + "\"nonce\":\"0x1\"},"
                + "[\"trace\",\"vmTrace\",\"stateDiff\"],"
                + "\"latest\"],"
                + "\"id\":1}");
        //CHECKSTYLE:ON
    }
    
    @Test
    public void testTraceRawTransaction() throws Exception {
        //CHECKSTYLE:OFF
        webuj.traceRawTransaction(
                "0xf869808504e3b292008305499d94781ab1a38837e351bfe1e318c6587766848abffa8084b46300ec26a0b1ffd8f843e08a9dbf0a42b3c7dd5288a48885cd6e3bcdb2609e943d0b0053d4a07bfdb3c12a7cec896bfc2cfc7c346a2cb411e1aca62ad085e8d7abbb6532e128",
                Arrays.asList("trace", "vmTrace", "stateDiff")
        ).send();
    
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"trace_rawTransaction\","
                + "\"params\":["
                + "\"0xf869808504e3b292008305499d94781ab1a38837e351bfe1e318c6587766848abffa8084b46300ec26a0b1ffd8f843e08a9dbf0a42b3c7"
                + "dd5288a48885cd6e3bcdb2609e943d0b0053d4a07bfdb3c12a7cec896bfc2cfc7c346a2cb411e1aca62ad085e8d7abbb6532e128\","
                + "[\"trace\",\"vmTrace\",\"stateDiff\"]],"
                + "\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testTraceReplayTransaction() throws Exception {
        webuj.traceReplayTransaction(
                "0x090a4bbdeb57f15fe252cccc924255855eda45a2d8f65b12ec81f03e2cc33249",
                Arrays.asList("trace", "vmTrace", "stateDiff")
        ).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"trace_replayTransaction\","
                + "\"params\":["
                + "\"0x090a4bbdeb57f15fe252cccc924255855eda45a2d8f65b12ec81f03e2cc33249\","
                + "[\"trace\",\"vmTrace\",\"stateDiff\"]],"
                + "\"id\":1}");
    }

    @Test
    public void testTraceBlock() throws Exception {
        webuj.traceBlock(DefaultBlockParameterName.LATEST).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"trace_block\","
                + "\"params\":[\"latest\"],"
                + "\"id\":1}");
    }

    @Test
    public void testTraceFilter() throws Exception {
        webuj.traceFilter(new TraceFilter(
                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                Collections.singletonList("0xa9bebd4853ce06c3dc1b711bbafa1514ed5b5130"),
                Collections.singletonList("0xB4d9b203d8D16f41916a62DEab83389cF2b7eeCb")
        )).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"trace_filter\","
                + "\"params\":[{\"fromBlock\":\"earliest\",\"toBlock\":\"latest\","
                + "\"fromAddress\":[\"0xa9bebd4853ce06c3dc1b711bbafa1514ed5b5130\"],"
                + "\"toAddress\":[\"0xB4d9b203d8D16f41916a62DEab83389cF2b7eeCb\"]}],"
                + "\"id\":1}");
    }

    @Test
    public void testTraceGet() throws Exception {
        webuj.traceGet(
                "0x090a4bbdeb57f15fe252cccc924255855eda45a2d8f65b12ec81f03e2cc33249",
                Arrays.asList(BigInteger.valueOf(2), BigInteger.ZERO, BigInteger.ZERO)
        ).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"trace_get\","
                + "\"params\":["
                + "\"0x090a4bbdeb57f15fe252cccc924255855eda45a2d8f65b12ec81f03e2cc33249\","
                + "[\"0x2\",\"0x0\",\"0x0\"]],"
                + "\"id\":1}");
    }
}