package org.happyuc.webuj.protocol.core;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.WebujFactory;
import org.happyuc.webuj.protocol.core.methods.request.HucReqFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucCompileLLL;
import org.happyuc.webuj.protocol.core.methods.response.HucCompileSerpent;
import org.happyuc.webuj.protocol.core.methods.response.HucCompileSolidity;
import org.happyuc.webuj.protocol.core.methods.response.HucEstimateGas;
import org.happyuc.webuj.protocol.core.methods.response.HucGasPrice;
import org.happyuc.webuj.protocol.core.methods.response.HucGetBalance;
import org.happyuc.webuj.protocol.core.methods.response.HucGetBlockRepTransactionCountByHash;
import org.happyuc.webuj.protocol.core.methods.response.HucGetBlockRepTransactionCountByNumber;
import org.happyuc.webuj.protocol.core.methods.response.HucGetCode;
import org.happyuc.webuj.protocol.core.methods.response.HucGetCompilers;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionCount;
import org.happyuc.webuj.protocol.core.methods.response.HucGetStorageAt;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionReceipt;
import org.happyuc.webuj.protocol.core.methods.response.HucGetUncleCountByBlockHash;
import org.happyuc.webuj.protocol.core.methods.response.HucGetUncleCountByBlockNumber;
import org.happyuc.webuj.protocol.core.methods.response.HucHashrate;
import org.happyuc.webuj.protocol.core.methods.response.HucLog;
import org.happyuc.webuj.protocol.core.methods.response.HucMining;
import org.happyuc.webuj.protocol.core.methods.response.HucProtocolVersion;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;
import org.happyuc.webuj.protocol.core.methods.response.HucSyncing;
import org.happyuc.webuj.protocol.core.methods.response.HucRepTransaction;
import org.happyuc.webuj.protocol.core.methods.response.HucUninstallFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucAccounts;
import org.happyuc.webuj.protocol.core.methods.response.HucBlock;
import org.happyuc.webuj.protocol.core.methods.response.HucBlockNumber;
import org.happyuc.webuj.protocol.core.methods.response.HucCall;
import org.happyuc.webuj.protocol.core.methods.response.HucCoinbase;
import org.happyuc.webuj.protocol.core.methods.response.HucRepFilter;
import org.happyuc.webuj.protocol.core.methods.response.NetListening;
import org.happyuc.webuj.protocol.core.methods.response.NetPeerCount;
import org.happyuc.webuj.protocol.core.methods.response.NetVersion;
import org.happyuc.webuj.protocol.core.methods.response.RepTransaction;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.happyuc.webuj.protocol.core.methods.response.ShhNewGroup;
import org.happyuc.webuj.protocol.core.methods.response.ShhNewIdentity;
import org.happyuc.webuj.protocol.core.methods.response.ShhVersion;
import org.happyuc.webuj.protocol.core.methods.response.WebuClientVersion;
import org.happyuc.webuj.protocol.core.methods.response.WebuSha3;
import org.happyuc.webuj.protocol.http.HttpService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigInteger;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * JSON-RPC 2.0 Integration Tests.
 */
public class CoreIT {

    private Webuj webuj;

    private IntegrationTestConfig config = new TestnetConfig();

    public CoreIT() { }

    @Before
    public void setUp() {
        this.webuj = WebujFactory.build(new HttpService());
    }

    @Test
    public void testWeb3ClientVersion() throws Exception {
        WebuClientVersion webuClientVersion = webuj.webuClientVersion().send();
        String clientVersion = webuClientVersion.getWeb3ClientVersion();
        System.out.println("Happyuc client version: " + clientVersion);
        assertFalse(clientVersion.isEmpty());
    }

    @Test
    public void testWeb3Sha3() throws Exception {
        WebuSha3 webuSha3 = webuj.webuSha3("0x68656c6c6f20776f726c64").send();
        assertThat(webuSha3.getResult(), is("0x47173285a8d7341e5e972fc677286384f802f8ef42a5ec5f03bbfa254cb01fad"));
    }

    @Test
    public void testNetVersion() throws Exception {
        NetVersion netVersion = webuj.netVersion().send();
        assertFalse(netVersion.getNetVersion().isEmpty());
    }

    @Test
    public void testNetListening() throws Exception {
        NetListening netListening = webuj.netListening().send();
        assertTrue(netListening.isListening());
    }

    @Test
    public void testNetPeerCount() throws Exception {
        NetPeerCount netPeerCount = webuj.netPeerCount().send();
        assertTrue(netPeerCount.getQuantity().signum() == 1);
    }

    @Test
    public void testEthProtocolVersion() throws Exception {
        HucProtocolVersion hucProtocolVersion = webuj.hucProtocolVersion().send();
        assertFalse(hucProtocolVersion.getProtocolVersion().isEmpty());
    }

    @Test
    public void testEthSyncing() throws Exception {
        HucSyncing hucSyncing = webuj.hucSyncing().send();
        assertNotNull(hucSyncing.getResult());
    }

    @Test
    public void testEthCoinbase() throws Exception {
        HucCoinbase hucCoinbase = webuj.hucCoinbase().send();
        assertNotNull(hucCoinbase.getAddress());
    }

    @Test
    public void testEthMining() throws Exception {
        HucMining hucMining = webuj.hucMining().send();
        assertNotNull(hucMining.getResult());
    }

    @Test
    public void testEthHashrate() throws Exception {
        HucHashrate hucHashrate = webuj.hucHashrate().send();
        assertThat(hucHashrate.getHashrate(), is(BigInteger.ZERO));
    }

    @Test
    public void testEthGasPrice() throws Exception {
        HucGasPrice hucGasPrice = webuj.hucGasPrice().send();
        assertTrue(hucGasPrice.getGasPrice().signum() == 1);
    }

    @Test
    public void testEthAccounts() throws Exception {
        HucAccounts hucAccounts = webuj.hucAccounts().send();
        assertNotNull(hucAccounts.getAccounts());
    }

    @Test
    public void testEthBlockNumber() throws Exception {
        HucBlockNumber hucBlockNumber = webuj.hucBlockNumber().send();
        assertTrue(hucBlockNumber.getBlockNumber().signum() == 1);
    }

    @Test
    public void testEthGetBalance() throws Exception {
        HucGetBalance hucGetBalance = webuj.hucGetBalance(config.validAccount(), DefaultBlockParameterName.valueOf("latest")).send();
        assertTrue(hucGetBalance.getBalance().signum() == 1);
    }

    @Test
    public void testEthGetStorageAt() throws Exception {
        HucGetStorageAt hucGetStorageAt = webuj.hucGetStorageAt(config.validContractAddress(), BigInteger.valueOf(0), DefaultBlockParameterName.valueOf("latest")).send();
        assertThat(hucGetStorageAt.getData(), is(config.validContractAddressPositionZero()));
    }

    @Test
    public void testEthGetTransactionCount() throws Exception {
        HucGetRepTransactionCount hucGetRepTransactionCount = webuj.hucGetTransactionCount(config.validAccount(), DefaultBlockParameterName.valueOf("latest")).send();
        assertTrue(hucGetRepTransactionCount.getTransactionCount().signum() == 1);
    }

    @Test
    public void testEthGetBlockTransactionCountByHash() throws Exception {
        HucGetBlockRepTransactionCountByHash hucGetBlockRepTransactionCountByHash = webuj.hucGetBlockTransactionCountByHash(config.validBlockHash()).send();
        assertThat(hucGetBlockRepTransactionCountByHash.getTransactionCount(), equalTo(config.validBlockTransactionCount()));
    }

    @Test
    public void testEthGetBlockTransactionCountByNumber() throws Exception {
        HucGetBlockRepTransactionCountByNumber hucGetBlockRepTransactionCountByNumber = webuj.hucGetBlockTransactionCountByNumber(DefaultBlockParameterNumber.valueOf(config.validBlock())).send();
        assertThat(hucGetBlockRepTransactionCountByNumber.getTransactionCount(), equalTo(config.validBlockTransactionCount()));
    }

    @Test
    public void testEthGetUncleCountByBlockHash() throws Exception {
        HucGetUncleCountByBlockHash hucGetUncleCountByBlockHash = webuj.hucGetUncleCountByBlockHash(config.validBlockHash()).send();
        assertThat(hucGetUncleCountByBlockHash.getUncleCount(), equalTo(config.validBlockUncleCount()));
    }

    @Test
    public void testEthGetUncleCountByBlockNumber() throws Exception {
        HucGetUncleCountByBlockNumber hucGetUncleCountByBlockNumber = webuj.hucGetUncleCountByBlockNumber(DefaultBlockParameterName.valueOf("latest")).send();
        assertThat(hucGetUncleCountByBlockNumber.getUncleCount(), equalTo(config.validBlockUncleCount()));
    }

    @Test
    public void testEthGetCode() throws Exception {
        HucGetCode hucGetCode = webuj.hucGetCode(config.validContractAddress(), DefaultBlockParameterNumber.valueOf(config.validBlock())).send();
        assertThat(hucGetCode.getCode(), is(config.validContractCode()));
    }

    @Ignore  // TODO: Once account unlock functionality is available
    @Test
    public void testEthSign() throws Exception {
        // HucSign hucSign = webuj.hucSign();
    }

    @Ignore  // TODO: Once account unlock functionality is available
    @Test
    public void testEthSendTransaction() throws Exception {
        HucSendRepTransaction hucSendRepTransaction = webuj.hucSendTransaction(config.buildTransaction()).send();
        assertFalse(hucSendRepTransaction.getTransactionHash().isEmpty());
    }

    @Ignore  // TODO: Once account unlock functionality is available
    @Test
    public void testEthSendRawTransaction() throws Exception {

    }

    @Test
    public void testEthCall() throws Exception {
        HucCall hucCall = webuj.hucCall(config.buildTransaction(), DefaultBlockParameterName.valueOf("latest")).send();

        assertThat(DefaultBlockParameterName.LATEST.getValue(), is("latest"));
        assertThat(hucCall.getValue(), is("0x"));
    }

    @Test
    public void testEthEstimateGas() throws Exception {
        HucEstimateGas hucEstimateGas = webuj.hucEstimateGas(config.buildTransaction()).send();
        assertTrue(hucEstimateGas.getAmountUsed().signum() == 1);
    }

    @Test
    public void testEthGetBlockByHashReturnHashObjects() throws Exception {
        HucBlock hucBlock = webuj.hucGetBlockByHash(config.validBlockHash(), false).send();

        HucBlock.Block block = hucBlock.getBlock();
        assertNotNull(hucBlock.getBlock());
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(), is(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetBlockByHashReturnFullTransactionObjects() throws Exception {
        HucBlock hucBlock = webuj.hucGetBlockByHash(config.validBlockHash(), true).send();

        HucBlock.Block block = hucBlock.getBlock();
        assertNotNull(hucBlock.getBlock());
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(), equalTo(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetBlockByNumberReturnHashObjects() throws Exception {
        HucBlock hucBlock = webuj.hucGetBlockByNumber(DefaultBlockParameterNumber.valueOf(config.validBlock()), false).send();

        HucBlock.Block block = hucBlock.getBlock();
        assertNotNull(hucBlock.getBlock());
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(), equalTo(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetBlockByNumberReturnTransactionObjects() throws Exception {
        HucBlock hucBlock = webuj.hucGetBlockByNumber(DefaultBlockParameterNumber.valueOf(config.validBlock()), true).send();

        HucBlock.Block block = hucBlock.getBlock();
        assertNotNull(hucBlock.getBlock());
        assertThat(block.getNumber(), equalTo(config.validBlock()));
        assertThat(block.getTransactions().size(), equalTo(config.validBlockTransactionCount().intValue()));
    }

    @Test
    public void testEthGetTransactionByHash() throws Exception {
        HucRepTransaction hucRepTransaction = webuj.hucGetTransactionByHash(config.validTransactionHash()).send();
        assertNotNull(hucRepTransaction.getTransaction());
        RepTransaction repTransaction = hucRepTransaction.getTransaction();
        assertThat(repTransaction.getBlockHash(), is(config.validBlockHash()));
    }

    @Test
    public void testEthGetTransactionByBlockHashAndIndex() throws Exception {
        BigInteger index = BigInteger.ONE;

        HucRepTransaction hucRepTransaction = webuj.hucGetTransactionByBlockHashAndIndex(config.validBlockHash(), index).send();
        assertNotNull(hucRepTransaction.getTransaction());
        RepTransaction repTransaction = hucRepTransaction.getTransaction();
        assertThat(repTransaction.getBlockHash(), is(config.validBlockHash()));
        assertThat(repTransaction.getTransactionIndex(), equalTo(index));
    }

    @Test
    public void testEthGetTransactionByBlockNumberAndIndex() throws Exception {
        BigInteger index = BigInteger.ONE;

        HucRepTransaction hucRepTransaction = webuj.hucGetTransactionByBlockNumberAndIndex(DefaultBlockParameterNumber.valueOf(config.validBlock()), index).send();
        RepTransaction repTransaction = hucRepTransaction.getTransaction();
        assertNotNull(repTransaction);
        assertThat(repTransaction.getBlockHash(), is(config.validBlockHash()));
        assertThat(repTransaction.getTransactionIndex(), equalTo(index));
    }

    @Test
    public void testEthGetTransactionReceipt() throws Exception {
        HucGetRepTransactionReceipt hucGetRepTransactionReceipt = webuj.hucGetTransactionReceipt(config.validTransactionHash()).send();
        RepTransactionReceipt repTransactionReceipt = hucGetRepTransactionReceipt.getTransactionReceipt();
        assertNotNull(repTransactionReceipt);
        assertThat(repTransactionReceipt.getTransactionHash(), is(config.validTransactionHash()));
    }

    @Test
    public void testEthGetUncleByBlockHashAndIndex() throws Exception {
        HucBlock hucBlock = webuj.hucGetUncleByBlockHashAndIndex(config.validUncleBlockHash(), BigInteger.ZERO).send();
        assertNotNull(hucBlock.getBlock());
    }

    @Test
    public void testEthGetUncleByBlockNumberAndIndex() throws Exception {
        HucBlock hucBlock = webuj.hucGetUncleByBlockNumberAndIndex(DefaultBlockParameterNumber.valueOf(config.validUncleBlock()), BigInteger.ZERO).send();
        assertNotNull(hucBlock.getBlock());
    }

    @Test
    public void testEthGetCompilers() throws Exception {
        HucGetCompilers hucGetCompilers = webuj.hucGetCompilers().send();
        assertNotNull(hucGetCompilers.getCompilers());
    }

    @Ignore  // The method huc_compileLLL does not exist/is not available
    @Test
    public void testEthCompileLLL() throws Exception {
        HucCompileLLL hucCompileLLL = webuj.hucCompileLLL("(returnlll (suicide (caller)))").send();
        assertFalse(hucCompileLLL.getCompiledSourceCode().isEmpty());
    }

    @Test
    public void testEthCompileSolidity() throws Exception {
        String sourceCode = "pragma solidity ^0.4.0;" + "\ncontract test { function multiply(uint a) returns(uint d) {" + "   return a * 7;   } }" + "\ncontract test2 { function multiply2(uint a) returns(uint d) {" + "   return a * 7;   } }";
        HucCompileSolidity hucCompileSolidity = webuj.hucCompileSolidity(sourceCode).send();
        assertNotNull(hucCompileSolidity.getCompiledSolidity());
        assertThat(hucCompileSolidity.getCompiledSolidity().get("test2").getInfo().getSource(), is(sourceCode));
    }

    @Ignore  // The method huc_compileSerpent does not exist/is not available
    @Test
    public void testEthCompileSerpent() throws Exception {
        HucCompileSerpent hucCompileSerpent = webuj.hucCompileSerpent("/* some serpent */").send();
        assertFalse(hucCompileSerpent.getCompiledSourceCode().isEmpty());
    }

    @Test
    public void testFiltersByFilterId() throws Exception {
        HucReqFilter hucReqFilter = new HucReqFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, config.validContractAddress());

        String eventSignature = config.encodedEvent();
        hucReqFilter.addSingleTopic(eventSignature);

        // huc_newFilter
        HucRepFilter ethNewFilter = webuj.hucNewFilter(hucReqFilter).send();
        BigInteger filterId = ethNewFilter.getFilterId();

        // huc_getFilterLogs
        HucLog ethFilterLogs = webuj.hucGetFilterLogs(filterId).send();
        List<HucLog.LogResult> filterLogs = ethFilterLogs.getLogs();
        assertFalse(filterLogs.isEmpty());

        // huc_getFilterChanges - nothing will have changed in this interval
        HucLog hucLog = webuj.hucGetFilterChanges(filterId).send();
        assertTrue(hucLog.getLogs().isEmpty());

        // huc_uninstallFilter
        HucUninstallFilter hucUninstallFilter = webuj.hucUninstallFilter(filterId).send();
        assertTrue(hucUninstallFilter.isUninstalled());
    }

    @Test
    public void testEthNewBlockFilter() throws Exception {
        HucRepFilter ethNewBlockFilter = webuj.hucNewBlockFilter().send();
        assertNotNull(ethNewBlockFilter.getFilterId());
    }

    @Test
    public void testEthNewPendingTransactionFilter() throws Exception {
        HucRepFilter ethNewPendingTransactionFilter = webuj.hucNewPendingTransactionFilter().send();
        assertNotNull(ethNewPendingTransactionFilter.getFilterId());
    }

    @Test
    public void testEthGetLogs() throws Exception {
        HucReqFilter hucReqFilter = new HucReqFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, config.validContractAddress());

        hucReqFilter.addSingleTopic(config.encodedEvent());

        HucLog hucLog = webuj.hucGetLogs(hucReqFilter).send();
        List<HucLog.LogResult> logs = hucLog.getLogs();
        assertFalse(logs.isEmpty());
    }

    // @Test
    // public void testEthGetWork() throws Exception {
    //     HucGetWork hucGetWork = requestFactory.hucGetWork();
    //     assertNotNull(hucGetWork.getResult());
    // }

    @Test
    public void testEthSubmitWork() throws Exception {

    }

    @Test
    public void testEthSubmitHashrate() throws Exception {

    }

    @Test
    public void testDbPutString() throws Exception {

    }

    @Test
    public void testDbGetString() throws Exception {

    }

    @Test
    public void testDbPutHex() throws Exception {

    }

    @Test
    public void testDbGetHex() throws Exception {

    }

    @Test
    public void testShhPost() throws Exception {

    }

    @Ignore // The method shh_version does not exist/is not available
    @Test
    public void testShhVersion() throws Exception {
        ShhVersion shhVersion = webuj.shhVersion().send();
        assertNotNull(shhVersion.getVersion());
    }

    @Ignore  // The method shh_newIdentity does not exist/is not available
    @Test
    public void testShhNewIdentity() throws Exception {
        ShhNewIdentity shhNewIdentity = webuj.shhNewIdentity().send();
        assertNotNull(shhNewIdentity.getAddress());
    }

    @Test
    public void testShhHasIdentity() throws Exception {

    }

    @Ignore  // The method shh_newIdentity does not exist/is not available
    @Test
    public void testShhNewGroup() throws Exception {
        ShhNewGroup shhNewGroup = webuj.shhNewGroup().send();
        assertNotNull(shhNewGroup.getAddress());
    }

    @Ignore  // The method shh_addToGroup does not exist/is not available
    @Test
    public void testShhAddToGroup() throws Exception {

    }

    @Test
    public void testShhNewFilter() throws Exception {

    }

    @Test
    public void testShhUninstallFilter() throws Exception {

    }

    @Test
    public void testShhGetFilterChanges() throws Exception {

    }

    @Test
    public void testShhGetMessages() throws Exception {

    }
}
