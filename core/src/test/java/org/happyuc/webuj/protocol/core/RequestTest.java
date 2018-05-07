package org.happyuc.webuj.protocol.core;

import java.math.BigInteger;
import java.util.Arrays;

import org.happyuc.webuj.protocol.Webuj;
import org.junit.Test;

import org.happyuc.webuj.protocol.RequestTester;
import org.happyuc.webuj.protocol.core.methods.request.HucFilter;
import org.happyuc.webuj.protocol.core.methods.request.ShhFilter;
import org.happyuc.webuj.protocol.core.methods.request.ShhPost;
import org.happyuc.webuj.protocol.core.methods.request.Transaction;
import org.happyuc.webuj.protocol.http.HttpService;
import org.happyuc.webuj.utils.Numeric;

public class RequestTest extends RequestTester {

    private Webuj webuj;

    @Override
    protected void initWeb3Client(HttpService httpService) {
        webuj = Webuj.build(httpService);
    }

    @Test
    public void testWeb3ClientVersion() throws Exception {
        webuj.webuClientVersion().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"web3_clientVersion\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testWeb3Sha3() throws Exception {
        webuj.webuSha3("0x68656c6c6f20776f726c64").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"web3_sha3\"," + "\"params\":[\"0x68656c6c6f20776f726c64\"],\"id\":1}");
    }

    @Test
    public void testNetVersion() throws Exception {
        webuj.netVersion().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"net_version\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testNetListening() throws Exception {
        webuj.netListening().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"net_listening\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testNetPeerCount() throws Exception {
        webuj.netPeerCount().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"net_peerCount\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testHucProtocolVersion() throws Exception {
        webuj.hucProtocolVersion().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_protocolVersion\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testHucSyncing() throws Exception {
        webuj.hucSyncing().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_syncing\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testHucCoinbase() throws Exception {
        webuj.hucCoinbase().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_coinbase\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testHucMining() throws Exception {
        webuj.hucMining().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_mining\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testHucHashrate() throws Exception {
        webuj.hucHashrate().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_hashrate\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testHucGasPrice() throws Exception {
        webuj.hucGasPrice().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_gasPrice\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testHucAccounts() throws Exception {
        webuj.hucAccounts().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_accounts\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testHucBlockNumber() throws Exception {
        webuj.hucBlockNumber().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_blockNumber\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testHucGetBalance() throws Exception {
        webuj.hucGetBalance("0x407d73d8a49eeb85d32cf465507dd71d507100c1", DefaultBlockParameterName.LATEST).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getBalance\"," + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"latest\"]," + "\"id\":1}");
    }

    @Test
    public void testHucGetStorageAt() throws Exception {
        webuj.hucGetStorageAt("0x295a70b2de5e3953354a6a8344e616ed314d7251", BigInteger.ZERO, DefaultBlockParameterName.LATEST).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getStorageAt\"," + "\"params\":[\"0x295a70b2de5e3953354a6a8344e616ed314d7251\",\"0x0\",\"latest\"]," + "\"id\":1}");
    }

    @Test
    public void testHucGetTransactionCount() throws Exception {
        webuj.hucGetTransactionCount("0x407d73d8a49eeb85d32cf465507dd71d507100c1", DefaultBlockParameterName.LATEST).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getTransactionCount\"," + "\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\",\"latest\"]," + "\"id\":1}");
    }

    @Test
    public void testHucGetBlockTransactionCountByHash() throws Exception {
        webuj.hucGetBlockTransactionCountByHash("0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238").send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getBlockTransactionCountByHash\",\"params\":[\"0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238\"],\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testHucGetBlockTransactionCountByNumber() throws Exception {
        webuj.hucGetBlockTransactionCountByNumber(DefaultBlockParameter.valueOf(Numeric.toBigInt("0xe8"))).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getBlockTransactionCountByNumber\"," + "\"params\":[\"0xe8\"],\"id\":1}");
    }

    @Test
    public void testHucGetUncleCountByBlockHash() throws Exception {
        webuj.hucGetUncleCountByBlockHash("0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238").send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getUncleCountByBlockHash\",\"params\":[\"0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238\"],\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testHucGetUncleCountByBlockNumber() throws Exception {
        webuj.hucGetUncleCountByBlockNumber(DefaultBlockParameter.valueOf(Numeric.toBigInt("0xe8"))).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getUncleCountByBlockNumber\"," + "\"params\":[\"0xe8\"],\"id\":1}");
    }

    @Test
    public void testHucGetCode() throws Exception {
        webuj.hucGetCode("0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b", DefaultBlockParameter.valueOf(Numeric.toBigInt("0x2"))).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getCode\"," + "\"params\":[\"0xa94f5374fce5edbc8e2a8697c15331677e6ebf0b\",\"0x2\"],\"id\":1}");
    }

    @Test
    public void testHucSign() throws Exception {
        webuj.hucSign("0x8a3106a3e50576d4b6794a0e74d3bb5f8c9acaab", "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_sign\"," + "\"params\":[\"0x8a3106a3e50576d4b6794a0e74d3bb5f8c9acaab\"," + "\"0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470\"]," + "\"id\":1}");
    }

    @Test
    public void testHucSendTransaction() throws Exception {
        webuj.hucSendTransaction(new Transaction("0xb60e8dd61c5d32be8058bb8eb970870f07233155", BigInteger.ONE, Numeric.toBigInt("0x9184e72a000"), Numeric.toBigInt("0x76c0"), "0xb60e8dd61c5d32be8058bb8eb970870f07233155", Numeric.toBigInt("0x9184e72a"), "0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb" + "970870f072445675058bb8eb970870f072445675")).send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_sendTransaction\",\"params\":[{\"from\":\"0xb60e8dd61c5d32be8058bb8eb970870f07233155\",\"to\":\"0xb60e8dd61c5d32be8058bb8eb970870f07233155\",\"gas\":\"0x76c0\",\"gasPrice\":\"0x9184e72a000\",\"value\":\"0x9184e72a\",\"data\":\"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\",\"nonce\":\"0x1\"}],\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testHucSendRawTransaction() throws Exception {
        webuj.hucSendRawTransaction("0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f" + "072445675058bb8eb970870f072445675").send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_sendRawTransaction\",\"params\":[\"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"],\"id\":1}");
        //CHECKSTYLE:ON
    }


    @Test
    public void testHucCall() throws Exception {
        webuj.hucCall(Transaction.createHucCallTransaction("0xa70e8dd61c5d32be8058bb8eb970870f07233155", "0xb60e8dd61c5d32be8058bb8eb970870f07233155", "0x0"), DefaultBlockParameter.valueOf("latest")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_call\"," + "\"params\":[{\"from\":\"0xa70e8dd61c5d32be8058bb8eb970870f07233155\"," + "\"to\":\"0xb60e8dd61c5d32be8058bb8eb970870f07233155\",\"data\":\"0x0\"}," + "\"latest\"],\"id\":1}");
    }

    @Test
    public void testHucEstimateGas() throws Exception {
        webuj.hucEstimateGas(Transaction.createHucCallTransaction("0xa70e8dd61c5d32be8058bb8eb970870f07233155", "0x52b93c80364dc2dd4444c146d73b9836bbbb2b3f", "0x0")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_estimateGas\"," + "\"params\":[{\"from\":\"0xa70e8dd61c5d32be8058bb8eb970870f07233155\"," + "\"to\":\"0x52b93c80364dc2dd4444c146d73b9836bbbb2b3f\",\"data\":\"0x0\"}]," + "\"id\":1}");
    }

    @Test
    public void testHucEstimateGasContractCreation() throws Exception {
        webuj.hucEstimateGas(Transaction.createContractTransaction("0x52b93c80364dc2dd4444c146d73b9836bbbb2b3f", BigInteger.ONE, BigInteger.TEN, "")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_estimateGas\"," + "\"params\":[{\"from\":\"0x52b93c80364dc2dd4444c146d73b9836bbbb2b3f\"," + "\"gasPrice\":\"0xa\",\"data\":\"0x\",\"nonce\":\"0x1\"}],\"id\":1}");
    }

    @Test
    public void testHucGetBlockByHash() throws Exception {
        webuj.hucGetBlockByHash("0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331", true).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getBlockByHash\",\"params\":[" + "\"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331\"" + ",true],\"id\":1}");
    }

    @Test
    public void testHucGetBlockByNumber() throws Exception {
        webuj.hucGetBlockByNumber(DefaultBlockParameter.valueOf(Numeric.toBigInt("0x1b4")), true).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getBlockByNumber\"," + "\"params\":[\"0x1b4\",true],\"id\":1}");
    }

    @Test
    public void testHucGetTransactionByHash() throws Exception {
        webuj.hucGetTransactionByHash("0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getTransactionByHash\",\"params\":[" + "\"0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238\"]," + "\"id\":1}");
    }

    @Test
    public void testHucGetTransactionByBlockHashAndIndex() throws Exception {
        webuj.hucGetTransactionByBlockHashAndIndex("0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331", BigInteger.ZERO).send();

        //CHECKSTYLE:OFF
        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getTransactionByBlockHashAndIndex\",\"params\":[\"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331\",\"0x0\"],\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testHucGetTransactionByBlockNumberAndIndex() throws Exception {
        webuj.hucGetTransactionByBlockNumberAndIndex(DefaultBlockParameter.valueOf(Numeric.toBigInt("0x29c")), BigInteger.ZERO).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getTransactionByBlockNumberAndIndex\"," + "\"params\":[\"0x29c\",\"0x0\"],\"id\":1}");
    }

    @Test
    public void testHucGetTransactionReceipt() throws Exception {
        webuj.hucGetTransactionReceipt("0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getTransactionReceipt\",\"params\":[" + "\"0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238\"]," + "\"id\":1}");
    }

    @Test
    public void testHucGetUncleByBlockHashAndIndex() throws Exception {
        webuj.hucGetUncleByBlockHashAndIndex("0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b", BigInteger.ZERO).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getUncleByBlockHashAndIndex\"," + "\"params\":[" + "\"0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b\",\"0x0\"]," + "\"id\":1}");
    }

    @Test
    public void testHucGetUncleByBlockNumberAndIndex() throws Exception {
        webuj.hucGetUncleByBlockNumberAndIndex(DefaultBlockParameter.valueOf(Numeric.toBigInt("0x29c")), BigInteger.ZERO).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getUncleByBlockNumberAndIndex\"," + "\"params\":[\"0x29c\",\"0x0\"],\"id\":1}");
    }

    @Test
    public void testHucGetCompilers() throws Exception {
        webuj.hucGetCompilers().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getCompilers\"," + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testHucCompileSolidity() throws Exception {
        webuj.hucCompileSolidity("contract test { function multiply(uint a) returns(uint d) {   return a * 7;   } }").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_compileSolidity\"," + "\"params\":[\"contract test { function multiply(uint a) returns(uint d) {" + "   return a * 7;   } }\"],\"id\":1}");
    }

    @Test
    public void testHucCompileLLL() throws Exception {
        webuj.hucCompileLLL("(returnlll (suicide (caller)))").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_compileLLL\"," + "\"params\":[\"(returnlll (suicide (caller)))\"],\"id\":1}");
    }

    @Test
    public void testHucCompileSerpent() throws Exception {
        webuj.hucCompileSerpent("/* some serpent */").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_compileSerpent\"," + "\"params\":[\"/* some serpent */\"],\"id\":1}");
    }

    @Test
    public void testHucNewFilter() throws Exception {
        HucFilter hucFilter = new HucFilter().addSingleTopic("0x12341234");

        webuj.hucNewFilter(hucFilter).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_newFilter\"," + "\"params\":[{\"topics\":[\"0x12341234\"]}],\"id\":1}");
    }

    @Test
    public void testHucNewBlockFilter() throws Exception {
        webuj.hucNewBlockFilter().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_newBlockFilter\"," + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testHucNewPendingTransactionFilter() throws Exception {
        webuj.hucNewPendingTransactionFilter().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_newPendingTransactionFilter\"," + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testHucUninstallFilter() throws Exception {
        webuj.hucUninstallFilter(Numeric.toBigInt("0xb")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_uninstallFilter\"," + "\"params\":[\"0x0b\"],\"id\":1}");
    }

    @Test
    public void testHucGetFilterChanges() throws Exception {
        webuj.hucGetFilterChanges(Numeric.toBigInt("0x16")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getFilterChanges\"," + "\"params\":[\"0x16\"],\"id\":1}");
    }

    @Test
    public void testHucGetFilterLogs() throws Exception {
        webuj.hucGetFilterLogs(Numeric.toBigInt("0x16")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getFilterLogs\"," + "\"params\":[\"0x16\"],\"id\":1}");
    }

    @Test
    public void testHucGetLogs() throws Exception {
        webuj.hucGetLogs(new HucFilter().addSingleTopic("0x000000000000000000000000a94f5374fce5edbc8e2a8697c15331677e6ebf0b")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getLogs\"," + "\"params\":[{\"topics\":[" + "\"0x000000000000000000000000a94f5374fce5edbc8e2a8697c15331677e6ebf0b\"]}]," + "\"id\":1}");
    }

    @Test
    public void testHucGetLogsWithNumericBlockRange() throws Exception {
        webuj.hucGetLogs(new HucFilter(DefaultBlockParameter.valueOf(Numeric.toBigInt("0xe8")), DefaultBlockParameter.valueOf("latest"), "")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getLogs\"," + "\"params\":[{\"topics\":[],\"fromBlock\":\"0xe8\"," + "\"toBlock\":\"latest\",\"address\":[\"\"]}],\"id\":1}");
    }

    @Test
    public void testHucGetWork() throws Exception {
        webuj.hucGetWork().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_getWork\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testHucSubmitWork() throws Exception {
        webuj.hucSubmitWork("0x0000000000000001", "0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef", "0xD1FE5700000000000000000000000000D1FE5700000000000000000000000000").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_submitWork\"," + "\"params\":[\"0x0000000000000001\"," + "\"0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef\"," + "\"0xD1FE5700000000000000000000000000D1FE5700000000000000000000000000\"]," + "\"id\":1}");
    }

    @Test
    public void testHucSubmitHashRate() throws Exception {
        webuj.hucSubmitHashrate("0x0000000000000000000000000000000000000000000000000000000000500000", "0x59daa26581d0acd1fce254fb7e85952f4c09d0915afd33d3886cd914bc7d283c").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"huc_submitHashrate\"," + "\"params\":[" + "\"0x0000000000000000000000000000000000000000000000000000000000500000\"," + "\"0x59daa26581d0acd1fce254fb7e85952f4c09d0915afd33d3886cd914bc7d283c\"]," + "\"id\":1}");
    }

    @Test
    public void testDbPutString() throws Exception {
        webuj.dbPutString("testDB", "myKey", "myString").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"db_putString\"," + "\"params\":[\"testDB\",\"myKey\",\"myString\"],\"id\":1}");
    }

    @Test
    public void testDbGetString() throws Exception {
        webuj.dbGetString("testDB", "myKey").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"db_getString\"," + "\"params\":[\"testDB\",\"myKey\"],\"id\":1}");
    }

    @Test
    public void testDbPutHex() throws Exception {
        webuj.dbPutHex("testDB", "myKey", "0x68656c6c6f20776f726c64").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"db_putHex\"," + "\"params\":[\"testDB\",\"myKey\",\"0x68656c6c6f20776f726c64\"],\"id\":1}");
    }

    @Test
    public void testDbGetHex() throws Exception {
        webuj.dbGetHex("testDB", "myKey").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"db_getHex\"," + "\"params\":[\"testDB\",\"myKey\"],\"id\":1}");
    }

    @Test
    public void testShhVersion() throws Exception {
        webuj.shhVersion().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_version\"," + "\"params\":[],\"id\":1}");
    }

    @Test
    public void testShhPost() throws Exception {
        //CHECKSTYLE:OFF
        webuj.shhPost(new ShhPost("0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1", "0x3e245533f97284d442460f2998cd41858798ddf04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a0d4d661997d3940272b717b1", Arrays.asList("0x776869737065722d636861742d636c69656e74", "0x4d5a695276454c39425154466b61693532"), "0x7b2274797065223a226d6", Numeric.toBigInt("0x64"), Numeric.toBigInt("0x64"))).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_post\",\"params\":[{\"from\":\"0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1\",\"to\":\"0x3e245533f97284d442460f2998cd41858798ddf04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a0d4d661997d3940272b717b1\",\"topics\":[\"0x776869737065722d636861742d636c69656e74\",\"0x4d5a695276454c39425154466b61693532\"],\"payload\":\"0x7b2274797065223a226d6\",\"priority\":\"0x64\",\"ttl\":\"0x64\"}],\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testShhNewIdentity() throws Exception {
        webuj.shhNewIdentity().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_newIdentity\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testShhHasIdentity() throws Exception {
        //CHECKSTYLE:OFF
        webuj.shhHasIdentity("0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_hasIdentity\",\"params\":[\"0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1\"],\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testShhNewGroup() throws Exception {
        webuj.shhNewGroup().send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_newGroup\",\"params\":[],\"id\":1}");
    }

    @Test
    public void testShhAddToGroup() throws Exception {
        //CHECKSTYLE:OFF
        webuj.shhAddToGroup("0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1").send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_addToGroup\",\"params\":[\"0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1\"],\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testShhNewFilter() throws Exception {
        //CHECKSTYLE:OFF
        webuj.shhNewFilter(new ShhFilter("0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1").addSingleTopic("0x12341234bf4b564f")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_newFilter\",\"params\":[{\"topics\":[\"0x12341234bf4b564f\"],\"to\":\"0x04f96a5e25610293e42a73908e93ccc8c4d4dc0edcfa9fa872f50cb214e08ebf61a03e245533f97284d442460f2998cd41858798ddfd4d661997d3940272b717b1\"}],\"id\":1}");
        //CHECKSTYLE:ON
    }

    @Test
    public void testShhUninstallFilter() throws Exception {
        webuj.shhUninstallFilter(Numeric.toBigInt("0x7")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_uninstallFilter\"," + "\"params\":[\"0x07\"],\"id\":1}");
    }

    @Test
    public void testShhGetFilterChanges() throws Exception {
        webuj.shhGetFilterChanges(Numeric.toBigInt("0x7")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_getFilterChanges\"," + "\"params\":[\"0x07\"],\"id\":1}");
    }

    @Test
    public void testShhGetMessages() throws Exception {
        webuj.shhGetMessages(Numeric.toBigInt("0x7")).send();

        verifyResult("{\"jsonrpc\":\"2.0\",\"method\":\"shh_getMessages\"," + "\"params\":[\"0x07\"],\"id\":1}");
    }
}
