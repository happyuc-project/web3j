package org.happyuc.webuj.protocol.core;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

import rx.Observable;

import org.happyuc.webuj.protocol.webuj;
import org.happyuc.webuj.protocol.webujService;
import org.happyuc.webuj.protocol.core.methods.request.ShhFilter;
import org.happyuc.webuj.protocol.core.methods.request.ShhPost;
import org.happyuc.webuj.protocol.core.methods.request.Transaction;
import org.happyuc.webuj.protocol.core.methods.response.DbGetHex;
import org.happyuc.webuj.protocol.core.methods.response.DbGetString;
import org.happyuc.webuj.protocol.core.methods.response.DbPutHex;
import org.happyuc.webuj.protocol.core.methods.response.DbPutString;
import org.happyuc.webuj.protocol.core.methods.response.HucAccounts;
import org.happyuc.webuj.protocol.core.methods.response.HucBlock;
import org.happyuc.webuj.protocol.core.methods.response.HucBlockNumber;
import org.happyuc.webuj.protocol.core.methods.response.HucCoinbase;
import org.happyuc.webuj.protocol.core.methods.response.HucCompileLLL;
import org.happyuc.webuj.protocol.core.methods.response.HucCompileSerpent;
import org.happyuc.webuj.protocol.core.methods.response.HucCompileSolidity;
import org.happyuc.webuj.protocol.core.methods.response.HucEstimateGas;
import org.happyuc.webuj.protocol.core.methods.response.HucFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucGasPrice;
import org.happyuc.webuj.protocol.core.methods.response.HucGetBalance;
import org.happyuc.webuj.protocol.core.methods.response.HucGetBlockTransactionCountByHash;
import org.happyuc.webuj.protocol.core.methods.response.HucGetBlockTransactionCountByNumber;
import org.happyuc.webuj.protocol.core.methods.response.HucGetCode;
import org.happyuc.webuj.protocol.core.methods.response.HucGetCompilers;
import org.happyuc.webuj.protocol.core.methods.response.HucGetStorageAt;
import org.happyuc.webuj.protocol.core.methods.response.HucGetTransactionCount;
import org.happyuc.webuj.protocol.core.methods.response.HucGetTransactionReceipt;
import org.happyuc.webuj.protocol.core.methods.response.HucGetUncleCountByBlockHash;
import org.happyuc.webuj.protocol.core.methods.response.HucGetUncleCountByBlockNumber;
import org.happyuc.webuj.protocol.core.methods.response.HucGetWork;
import org.happyuc.webuj.protocol.core.methods.response.HucHashrate;
import org.happyuc.webuj.protocol.core.methods.response.HucLog;
import org.happyuc.webuj.protocol.core.methods.response.HucMining;
import org.happyuc.webuj.protocol.core.methods.response.HucProtocolVersion;
import org.happyuc.webuj.protocol.core.methods.response.HucSign;
import org.happyuc.webuj.protocol.core.methods.response.HucSubmitHashrate;
import org.happyuc.webuj.protocol.core.methods.response.HucSubmitWork;
import org.happyuc.webuj.protocol.core.methods.response.HucSyncing;
import org.happyuc.webuj.protocol.core.methods.response.HucTransaction;
import org.happyuc.webuj.protocol.core.methods.response.HucUninstallFilter;
import org.happyuc.webuj.protocol.core.methods.response.Log;
import org.happyuc.webuj.protocol.core.methods.response.NetListening;
import org.happyuc.webuj.protocol.core.methods.response.NetPeerCount;
import org.happyuc.webuj.protocol.core.methods.response.NetVersion;
import org.happyuc.webuj.protocol.core.methods.response.ShhAddToGroup;
import org.happyuc.webuj.protocol.core.methods.response.ShhHasIdentity;
import org.happyuc.webuj.protocol.core.methods.response.ShhMessages;
import org.happyuc.webuj.protocol.core.methods.response.ShhNewFilter;
import org.happyuc.webuj.protocol.core.methods.response.ShhNewGroup;
import org.happyuc.webuj.protocol.core.methods.response.ShhNewIdentity;
import org.happyuc.webuj.protocol.core.methods.response.ShhUninstallFilter;
import org.happyuc.webuj.protocol.core.methods.response.ShhVersion;
import org.happyuc.webuj.protocol.core.methods.response.Web3ClientVersion;
import org.happyuc.webuj.protocol.core.methods.response.Web3Sha3;
import org.happyuc.webuj.protocol.rx.JsonRpc2_0Rx;
import org.happyuc.webuj.utils.Async;
import org.happyuc.webuj.utils.Numeric;

/**
 * JSON-RPC 2.0 factory implementation.
 */
public class JsonRpc2_0webuj implements webuj {

    public static final int DEFAULT_BLOCK_TIME = 15 * 1000;

    protected final webujService webujService;
    private final JsonRpc2_0Rx webujRx;
    private final long blockTime;
    private final ScheduledExecutorService scheduledExecutorService;

    public JsonRpc2_0webuj(webujService webujService) {
        this(webujService, DEFAULT_BLOCK_TIME, Async.defaultExecutorService());
    }

    public JsonRpc2_0webuj(
            webujService webujService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        this.webujService = webujService;
        this.webujRx = new JsonRpc2_0Rx(this, scheduledExecutorService);
        this.blockTime = pollingInterval;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public Request<?, Web3ClientVersion> web3ClientVersion() {
        return new Request<>(
                "web3_clientVersion",
                Collections.<String>emptyList(),
                webujService,
                Web3ClientVersion.class);
    }

    @Override
    public Request<?, Web3Sha3> web3Sha3(String data) {
        return new Request<>(
                "web3_sha3",
                Arrays.asList(data),
                webujService,
                Web3Sha3.class);
    }

    @Override
    public Request<?, NetVersion> netVersion() {
        return new Request<>(
                "net_version",
                Collections.<String>emptyList(),
                webujService,
                NetVersion.class);
    }

    @Override
    public Request<?, NetListening> netListening() {
        return new Request<>(
                "net_listening",
                Collections.<String>emptyList(),
                webujService,
                NetListening.class);
    }

    @Override
    public Request<?, NetPeerCount> netPeerCount() {
        return new Request<>(
                "net_peerCount",
                Collections.<String>emptyList(),
                webujService,
                NetPeerCount.class);
    }

    @Override
    public Request<?, HucProtocolVersion> ethProtocolVersion() {
        return new Request<>(
                "eth_protocolVersion",
                Collections.<String>emptyList(),
                webujService,
                HucProtocolVersion.class);
    }

    @Override
    public Request<?, HucCoinbase> ethCoinbase() {
        return new Request<>(
                "eth_coinbase",
                Collections.<String>emptyList(),
                webujService,
                HucCoinbase.class);
    }

    @Override
    public Request<?, HucSyncing> ethSyncing() {
        return new Request<>(
                "eth_syncing",
                Collections.<String>emptyList(),
                webujService,
                HucSyncing.class);
    }

    @Override
    public Request<?, HucMining> ethMining() {
        return new Request<>(
                "eth_mining",
                Collections.<String>emptyList(),
                webujService,
                HucMining.class);
    }

    @Override
    public Request<?, HucHashrate> ethHashrate() {
        return new Request<>(
                "eth_hashrate",
                Collections.<String>emptyList(),
                webujService,
                HucHashrate.class);
    }

    @Override
    public Request<?, HucGasPrice> ethGasPrice() {
        return new Request<>(
                "eth_gasPrice",
                Collections.<String>emptyList(),
                webujService,
                HucGasPrice.class);
    }

    @Override
    public Request<?, HucAccounts> ethAccounts() {
        return new Request<>(
                "eth_accounts",
                Collections.<String>emptyList(),
                webujService,
                HucAccounts.class);
    }

    @Override
    public Request<?, HucBlockNumber> ethBlockNumber() {
        return new Request<>(
                "eth_blockNumber",
                Collections.<String>emptyList(),
                webujService,
                HucBlockNumber.class);
    }

    @Override
    public Request<?, HucGetBalance> ethGetBalance(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getBalance",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                webujService,
                HucGetBalance.class);
    }

    @Override
    public Request<?, HucGetStorageAt> ethGetStorageAt(
            String address, BigInteger position, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getStorageAt",
                Arrays.asList(
                        address,
                        Numeric.encodeQuantity(position),
                        defaultBlockParameter.getValue()),
                webujService,
                HucGetStorageAt.class);
    }

    @Override
    public Request<?, HucGetTransactionCount> ethGetTransactionCount(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getTransactionCount",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                webujService,
                HucGetTransactionCount.class);
    }

    @Override
    public Request<?, HucGetBlockTransactionCountByHash> ethGetBlockTransactionCountByHash(
            String blockHash) {
        return new Request<>(
                "eth_getBlockTransactionCountByHash",
                Arrays.asList(blockHash),
                webujService,
                HucGetBlockTransactionCountByHash.class);
    }

    @Override
    public Request<?, HucGetBlockTransactionCountByNumber> ethGetBlockTransactionCountByNumber(
            DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getBlockTransactionCountByNumber",
                Arrays.asList(defaultBlockParameter.getValue()),
                webujService,
                HucGetBlockTransactionCountByNumber.class);
    }

    @Override
    public Request<?, HucGetUncleCountByBlockHash> ethGetUncleCountByBlockHash(String blockHash) {
        return new Request<>(
                "eth_getUncleCountByBlockHash",
                Arrays.asList(blockHash),
                webujService,
                HucGetUncleCountByBlockHash.class);
    }

    @Override
    public Request<?, HucGetUncleCountByBlockNumber> ethGetUncleCountByBlockNumber(
            DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getUncleCountByBlockNumber",
                Arrays.asList(defaultBlockParameter.getValue()),
                webujService,
                HucGetUncleCountByBlockNumber.class);
    }

    @Override
    public Request<?, HucGetCode> ethGetCode(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_getCode",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                webujService,
                HucGetCode.class);
    }

    @Override
    public Request<?, HucSign> ethSign(String address, String sha3HashOfDataToSign) {
        return new Request<>(
                "eth_sign",
                Arrays.asList(address, sha3HashOfDataToSign),
                webujService,
                HucSign.class);
    }

    @Override
    public Request<?, org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction>
            ethSendTransaction(
            Transaction transaction) {
        return new Request<>(
                "eth_sendTransaction",
                Arrays.asList(transaction),
                webujService,
                org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction.class);
    }

    @Override
    public Request<?, org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction>
            ethSendRawTransaction(
            String signedTransactionData) {
        return new Request<>(
                "eth_sendRawTransaction",
                Arrays.asList(signedTransactionData),
                webujService,
                org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction.class);
    }

    @Override
    public Request<?, org.happyuc.webuj.protocol.core.methods.response.HucCall> ethCall(
            Transaction transaction, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "eth_call",
                Arrays.asList(transaction, defaultBlockParameter),
                webujService,
                org.happyuc.webuj.protocol.core.methods.response.HucCall.class);
    }

    @Override
    public Request<?, HucEstimateGas> ethEstimateGas(Transaction transaction) {
        return new Request<>(
                "eth_estimateGas",
                Arrays.asList(transaction),
                webujService,
                HucEstimateGas.class);
    }

    @Override
    public Request<?, HucBlock> ethGetBlockByHash(
            String blockHash, boolean returnFullTransactionObjects) {
        return new Request<>(
                "eth_getBlockByHash",
                Arrays.asList(
                        blockHash,
                        returnFullTransactionObjects),
                webujService,
                HucBlock.class);
    }

    @Override
    public Request<?, HucBlock> ethGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter,
            boolean returnFullTransactionObjects) {
        return new Request<>(
                "eth_getBlockByNumber",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        returnFullTransactionObjects),
                webujService,
                HucBlock.class);
    }

    @Override
    public Request<?, HucTransaction> ethGetTransactionByHash(String transactionHash) {
        return new Request<>(
                "eth_getTransactionByHash",
                Arrays.asList(transactionHash),
                webujService,
                HucTransaction.class);
    }

    @Override
    public Request<?, HucTransaction> ethGetTransactionByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) {
        return new Request<>(
                "eth_getTransactionByBlockHashAndIndex",
                Arrays.asList(
                        blockHash,
                        Numeric.encodeQuantity(transactionIndex)),
                webujService,
                HucTransaction.class);
    }

    @Override
    public Request<?, HucTransaction> ethGetTransactionByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex) {
        return new Request<>(
                "eth_getTransactionByBlockNumberAndIndex",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        Numeric.encodeQuantity(transactionIndex)),
                webujService,
                HucTransaction.class);
    }

    @Override
    public Request<?, HucGetTransactionReceipt> ethGetTransactionReceipt(String transactionHash) {
        return new Request<>(
                "eth_getTransactionReceipt",
                Arrays.asList(transactionHash),
                webujService,
                HucGetTransactionReceipt.class);
    }

    @Override
    public Request<?, HucBlock> ethGetUncleByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) {
        return new Request<>(
                "eth_getUncleByBlockHashAndIndex",
                Arrays.asList(
                        blockHash,
                        Numeric.encodeQuantity(transactionIndex)),
                webujService,
                HucBlock.class);
    }

    @Override
    public Request<?, HucBlock> ethGetUncleByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger uncleIndex) {
        return new Request<>(
                "eth_getUncleByBlockNumberAndIndex",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        Numeric.encodeQuantity(uncleIndex)),
                webujService,
                HucBlock.class);
    }

    @Override
    public Request<?, HucGetCompilers> ethGetCompilers() {
        return new Request<>(
                "eth_getCompilers",
                Collections.<String>emptyList(),
                webujService,
                HucGetCompilers.class);
    }

    @Override
    public Request<?, HucCompileLLL> ethCompileLLL(String sourceCode) {
        return new Request<>(
                "eth_compileLLL",
                Arrays.asList(sourceCode),
                webujService,
                HucCompileLLL.class);
    }

    @Override
    public Request<?, HucCompileSolidity> ethCompileSolidity(String sourceCode) {
        return new Request<>(
                "eth_compileSolidity",
                Arrays.asList(sourceCode),
                webujService,
                HucCompileSolidity.class);
    }

    @Override
    public Request<?, HucCompileSerpent> ethCompileSerpent(String sourceCode) {
        return new Request<>(
                "eth_compileSerpent",
                Arrays.asList(sourceCode),
                webujService,
                HucCompileSerpent.class);
    }

    @Override
    public Request<?, HucFilter> ethNewFilter(
            org.happyuc.webuj.protocol.core.methods.request.HucFilter ethFilter) {
        return new Request<>(
                "eth_newFilter",
                Arrays.asList(ethFilter),
                webujService,
                HucFilter.class);
    }

    @Override
    public Request<?, HucFilter> ethNewBlockFilter() {
        return new Request<>(
                "eth_newBlockFilter",
                Collections.<String>emptyList(),
                webujService,
                HucFilter.class);
    }

    @Override
    public Request<?, HucFilter> ethNewPendingTransactionFilter() {
        return new Request<>(
                "eth_newPendingTransactionFilter",
                Collections.<String>emptyList(),
                webujService,
                HucFilter.class);
    }

    @Override
    public Request<?, HucUninstallFilter> ethUninstallFilter(BigInteger filterId) {
        return new Request<>(
                "eth_uninstallFilter",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                webujService,
                HucUninstallFilter.class);
    }

    @Override
    public Request<?, HucLog> ethGetFilterChanges(BigInteger filterId) {
        return new Request<>(
                "eth_getFilterChanges",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                webujService,
                HucLog.class);
    }

    @Override
    public Request<?, HucLog> ethGetFilterLogs(BigInteger filterId) {
        return new Request<>(
                "eth_getFilterLogs",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                webujService,
                HucLog.class);
    }

    @Override
    public Request<?, HucLog> ethGetLogs(
            org.happyuc.webuj.protocol.core.methods.request.HucFilter ethFilter) {
        return new Request<>(
                "eth_getLogs",
                Arrays.asList(ethFilter),
                webujService,
                HucLog.class);
    }

    @Override
    public Request<?, HucGetWork> ethGetWork() {
        return new Request<>(
                "eth_getWork",
                Collections.<String>emptyList(),
                webujService,
                HucGetWork.class);
    }

    @Override
    public Request<?, HucSubmitWork> ethSubmitWork(
            String nonce, String headerPowHash, String mixDigest) {
        return new Request<>(
                "eth_submitWork",
                Arrays.asList(nonce, headerPowHash, mixDigest),
                webujService,
                HucSubmitWork.class);
    }

    @Override
    public Request<?, HucSubmitHashrate> ethSubmitHashrate(String hashrate, String clientId) {
        return new Request<>(
                "eth_submitHashrate",
                Arrays.asList(hashrate, clientId),
                webujService,
                HucSubmitHashrate.class);
    }

    @Override
    public Request<?, DbPutString> dbPutString(
            String databaseName, String keyName, String stringToStore) {
        return new Request<>(
                "db_putString",
                Arrays.asList(databaseName, keyName, stringToStore),
                webujService,
                DbPutString.class);
    }

    @Override
    public Request<?, DbGetString> dbGetString(String databaseName, String keyName) {
        return new Request<>(
                "db_getString",
                Arrays.asList(databaseName, keyName),
                webujService,
                DbGetString.class);
    }

    @Override
    public Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore) {
        return new Request<>(
                "db_putHex",
                Arrays.asList(databaseName, keyName, dataToStore),
                webujService,
                DbPutHex.class);
    }

    @Override
    public Request<?, DbGetHex> dbGetHex(String databaseName, String keyName) {
        return new Request<>(
                "db_getHex",
                Arrays.asList(databaseName, keyName),
                webujService,
                DbGetHex.class);
    }

    @Override
    public Request<?, org.happyuc.webuj.protocol.core.methods.response.ShhPost> shhPost(ShhPost shhPost) {
        return new Request<>(
                "shh_post",
                Arrays.asList(shhPost),
                webujService,
                org.happyuc.webuj.protocol.core.methods.response.ShhPost.class);
    }

    @Override
    public Request<?, ShhVersion> shhVersion() {
        return new Request<>(
                "shh_version",
                Collections.<String>emptyList(),
                webujService,
                ShhVersion.class);
    }

    @Override
    public Request<?, ShhNewIdentity> shhNewIdentity() {
        return new Request<>(
                "shh_newIdentity",
                Collections.<String>emptyList(),
                webujService,
                ShhNewIdentity.class);
    }

    @Override
    public Request<?, ShhHasIdentity> shhHasIdentity(String identityAddress) {
        return new Request<>(
                "shh_hasIdentity",
                Arrays.asList(identityAddress),
                webujService,
                ShhHasIdentity.class);
    }

    @Override
    public Request<?, ShhNewGroup> shhNewGroup() {
        return new Request<>(
                "shh_newGroup",
                Collections.<String>emptyList(),
                webujService,
                ShhNewGroup.class);
    }

    @Override
    public Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress) {
        return new Request<>(
                "shh_addToGroup",
                Arrays.asList(identityAddress),
                webujService,
                ShhAddToGroup.class);
    }

    @Override
    public Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter) {
        return new Request<>(
                "shh_newFilter",
                Arrays.asList(shhFilter),
                webujService,
                ShhNewFilter.class);
    }

    @Override
    public Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId) {
        return new Request<>(
                "shh_uninstallFilter",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                webujService,
                ShhUninstallFilter.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId) {
        return new Request<>(
                "shh_getFilterChanges",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                webujService,
                ShhMessages.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetMessages(BigInteger filterId) {
        return new Request<>(
                "shh_getMessages",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                webujService,
                ShhMessages.class);
    }

    @Override
    public Observable<String> ethBlockHashObservable() {
        return webujRx.hucBlockHashObservable(blockTime);
    }

    @Override
    public Observable<String> ethPendingTransactionHashObservable() {
        return webujRx.hucPendingTransactionHashObservable(blockTime);
    }

    @Override
    public Observable<Log> ethLogObservable(
            org.happyuc.webuj.protocol.core.methods.request.HucFilter ethFilter) {
        return webujRx.hucLogObservable(ethFilter, blockTime);
    }

    @Override
    public Observable<org.happyuc.webuj.protocol.core.methods.response.Transaction>
            transactionObservable() {
        return webujRx.transactionObservable(blockTime);
    }

    @Override
    public Observable<org.happyuc.webuj.protocol.core.methods.response.Transaction>
            pendingTransactionObservable() {
        return webujRx.pendingTransactionObservable(blockTime);
    }

    @Override
    public Observable<HucBlock> blockObservable(boolean fullTransactionObjects) {
        return webujRx.blockObservable(fullTransactionObjects, blockTime);
    }

    @Override
    public Observable<HucBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return webujRx.replayBlocksObservable(startBlock, endBlock, fullTransactionObjects);
    }

    @Override
    public Observable<HucBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {
        return webujRx.replayBlocksObservable(startBlock, endBlock,
                fullTransactionObjects, ascending);
    }

    @Override
    public Observable<org.happyuc.webuj.protocol.core.methods.response.Transaction>
            replayTransactionsObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return webujRx.replayTransactionsObservable(startBlock, endBlock);
    }

    @Override
    public Observable<HucBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<HucBlock> onCompleteObservable) {
        return webujRx.catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects, onCompleteObservable);
    }

    @Override
    public Observable<HucBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return webujRx.catchUpToLatestBlockObservable(startBlock, fullTransactionObjects);
    }

    @Override
    public Observable<org.happyuc.webuj.protocol.core.methods.response.Transaction>
            catchUpToLatestTransactionObservable(DefaultBlockParameter startBlock) {
        return webujRx.catchUpToLatestTransactionObservable(startBlock);
    }

    @Override
    public Observable<HucBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return webujRx.catchUpToLatestAndSubscribeToNewBlocksObservable(
                startBlock, fullTransactionObjects, blockTime);
    }

    @Override
    public Observable<org.happyuc.webuj.protocol.core.methods.response.Transaction>
            catchUpToLatestAndSubscribeToNewTransactionsObservable(
            DefaultBlockParameter startBlock) {
        return webujRx.catchUpToLatestAndSubscribeToNewTransactionsObservable(
                startBlock, blockTime);
    }

    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
    }
}
