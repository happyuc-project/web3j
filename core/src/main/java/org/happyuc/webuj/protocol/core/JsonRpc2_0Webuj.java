package org.happyuc.webuj.protocol.core;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.core.methods.response.*;
import rx.Observable;

import org.happyuc.webuj.protocol.WebujService;
import org.happyuc.webuj.protocol.core.methods.request.ShhFilter;
import org.happyuc.webuj.protocol.core.methods.request.ShhPost;
import org.happyuc.webuj.protocol.core.methods.request.Transaction;
import org.happyuc.webuj.protocol.rx.JsonRpc2_0Rx;
import org.happyuc.webuj.utils.Async;
import org.happyuc.webuj.utils.Numeric;

/**
 * JSON-RPC 2.0 factory implementation.
 */
public class JsonRpc2_0Webuj implements Webuj {

    public static final int DEFAULT_BLOCK_TIME = 15 * 1000;

    protected final WebujService webujService;
    private final JsonRpc2_0Rx webujRx;
    private final long blockTime;
    private final ScheduledExecutorService scheduledExecutorService;

    public JsonRpc2_0Webuj(WebujService webujService) {
        this(webujService, DEFAULT_BLOCK_TIME, Async.defaultExecutorService());
    }

    public JsonRpc2_0Webuj(WebujService webujService, long pollingInterval, ScheduledExecutorService scheduledExecutorService) {
        this.webujService = webujService;
        this.webujRx = new JsonRpc2_0Rx(this, scheduledExecutorService);
        this.blockTime = pollingInterval;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public Request<?, WebuClientVersion> webuClientVersion() {
        return new Request<>("webu_clientVersion", Collections.<String>emptyList(), webujService, WebuClientVersion.class);
    }

    @Override
    public Request<?, WebuSha3> webuSha3(String data) {
        return new Request<>("webu_sha3", Arrays.asList(data), webujService, WebuSha3.class);
    }

    @Override
    public Request<?, NetVersion> netVersion() {
        return new Request<>("net_version", Collections.<String>emptyList(), webujService, NetVersion.class);
    }

    @Override
    public Request<?, NetListening> netListening() {
        return new Request<>("net_listening", Collections.<String>emptyList(), webujService, NetListening.class);
    }

    @Override
    public Request<?, NetPeerCount> netPeerCount() {
        return new Request<>("net_peerCount", Collections.<String>emptyList(), webujService, NetPeerCount.class);
    }

    @Override
    public Request<?, HucProtocolVersion> hucProtocolVersion() {
        return new Request<>("huc_protocolVersion", Collections.<String>emptyList(), webujService, HucProtocolVersion.class);
    }

    @Override
    public Request<?, HucCoinbase> hucCoinbase() {
        return new Request<>("huc_coinbase", Collections.<String>emptyList(), webujService, HucCoinbase.class);
    }

    @Override
    public Request<?, HucSyncing> hucSyncing() {
        return new Request<>("huc_syncing", Collections.<String>emptyList(), webujService, HucSyncing.class);
    }

    @Override
    public Request<?, HucMining> hucMining() {
        return new Request<>("huc_mining", Collections.<String>emptyList(), webujService, HucMining.class);
    }

    @Override
    public Request<?, HucHashrate> hucHashrate() {
        return new Request<>("huc_hashrate", Collections.<String>emptyList(), webujService, HucHashrate.class);
    }

    @Override
    public Request<?, HucGasPrice> hucGasPrice() {
        return new Request<>("huc_gasPrice", Collections.<String>emptyList(), webujService, HucGasPrice.class);
    }

    @Override
    public Request<?, HucAccounts> hucAccounts() {
        return new Request<>("huc_accounts", Collections.<String>emptyList(), webujService, HucAccounts.class);
    }

    @Override
    public Request<?, HucBlockNumber> hucBlockNumber() {
        return new Request<>("huc_blockNumber", Collections.<String>emptyList(), webujService, HucBlockNumber.class);
    }

    @Override
    public Request<?, HucGetBalance> hucGetBalance(String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>("huc_getBalance", Arrays.asList(address, defaultBlockParameter.getValue()), webujService, HucGetBalance.class);
    }

    @Override
    public Request<?, HucGetStorageAt> hucGetStorageAt(String address, BigInteger position, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>("huc_getStorageAt", Arrays.asList(address, Numeric.encodeQuantity(position), defaultBlockParameter.getValue()), webujService, HucGetStorageAt.class);
    }

    @Override
    public Request<?, HucGetTransactionCount> hucGetTransactionCount(String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>("huc_getTransactionCount", Arrays.asList(address, defaultBlockParameter.getValue()), webujService, HucGetTransactionCount.class);
    }

    @Override
    public Request<?, HucGetBlockTransactionCountByHash> hucGetBlockTransactionCountByHash(String blockHash) {
        return new Request<>("huc_getBlockTransactionCountByHash", Arrays.asList(blockHash), webujService, HucGetBlockTransactionCountByHash.class);
    }

    @Override
    public Request<?, HucGetBlockTransactionCountByNumber> hucGetBlockTransactionCountByNumber(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>("huc_getBlockTransactionCountByNumber", Arrays.asList(defaultBlockParameter.getValue()), webujService, HucGetBlockTransactionCountByNumber.class);
    }

    @Override
    public Request<?, HucGetUncleCountByBlockHash> hucGetUncleCountByBlockHash(String blockHash) {
        return new Request<>("huc_getUncleCountByBlockHash", Arrays.asList(blockHash), webujService, HucGetUncleCountByBlockHash.class);
    }

    @Override
    public Request<?, HucGetUncleCountByBlockNumber> hucGetUncleCountByBlockNumber(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>("huc_getUncleCountByBlockNumber", Arrays.asList(defaultBlockParameter.getValue()), webujService, HucGetUncleCountByBlockNumber.class);
    }

    @Override
    public Request<?, HucGetCode> hucGetCode(String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>("huc_getCode", Arrays.asList(address, defaultBlockParameter.getValue()), webujService, HucGetCode.class);
    }

    @Override
    public Request<?, HucSign> hucSign(String address, String sha3HashOfDataToSign) {
        return new Request<>("huc_sign", Arrays.asList(address, sha3HashOfDataToSign), webujService, HucSign.class);
    }

    @Override
    public Request<?, org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction> hucSendTransaction(Transaction transaction) {
        return new Request<>("huc_sendTransaction", Arrays.asList(transaction), webujService, org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction.class);
    }

    @Override
    public Request<?, org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction> hucSendRawTransaction(String signedTransactionData) {
        return new Request<>("huc_sendRawTransaction", Arrays.asList(signedTransactionData), webujService, org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction.class);
    }

    @Override
    public Request<?, org.happyuc.webuj.protocol.core.methods.response.HucCall> hucCall(Transaction transaction, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>("huc_call", Arrays.asList(transaction, defaultBlockParameter), webujService, org.happyuc.webuj.protocol.core.methods.response.HucCall.class);
    }

    @Override
    public Request<?, HucEstimateGas> hucEstimateGas(Transaction transaction) {
        return new Request<>("huc_estimateGas", Arrays.asList(transaction), webujService, HucEstimateGas.class);
    }

    @Override
    public Request<?, HucBlock> hucGetBlockByHash(String blockHash, boolean returnFullTransactionObjects) {
        return new Request<>("huc_getBlockByHash", Arrays.asList(blockHash, returnFullTransactionObjects), webujService, HucBlock.class);
    }

    @Override
    public Request<?, HucBlock> hucGetBlockByNumber(DefaultBlockParameter defaultBlockParameter, boolean returnFullTransactionObjects) {
        return new Request<>("huc_getBlockByNumber", Arrays.asList(defaultBlockParameter.getValue(), returnFullTransactionObjects), webujService, HucBlock.class);
    }

    @Override
    public Request<?, HucTransaction> hucGetTransactionByHash(String transactionHash) {
        return new Request<>("huc_getTransactionByHash", Arrays.asList(transactionHash), webujService, HucTransaction.class);
    }

    @Override
    public Request<?, HucTransaction> hucGetTransactionByBlockHashAndIndex(String blockHash, BigInteger transactionIndex) {
        return new Request<>("huc_getTransactionByBlockHashAndIndex", Arrays.asList(blockHash, Numeric.encodeQuantity(transactionIndex)), webujService, HucTransaction.class);
    }

    @Override
    public Request<?, HucTransaction> hucGetTransactionByBlockNumberAndIndex(DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex) {
        return new Request<>("huc_getTransactionByBlockNumberAndIndex", Arrays.asList(defaultBlockParameter.getValue(), Numeric.encodeQuantity(transactionIndex)), webujService, HucTransaction.class);
    }

    @Override
    public Request<?, HucGetTransactionReceipt> hucGetTransactionReceipt(String transactionHash) {
        return new Request<>("huc_getTransactionReceipt", Arrays.asList(transactionHash), webujService, HucGetTransactionReceipt.class);
    }

    @Override
    public Request<?, HucBlock> hucGetUncleByBlockHashAndIndex(String blockHash, BigInteger transactionIndex) {
        return new Request<>("huc_getUncleByBlockHashAndIndex", Arrays.asList(blockHash, Numeric.encodeQuantity(transactionIndex)), webujService, HucBlock.class);
    }

    @Override
    public Request<?, HucBlock> hucGetUncleByBlockNumberAndIndex(DefaultBlockParameter defaultBlockParameter, BigInteger uncleIndex) {
        return new Request<>("huc_getUncleByBlockNumberAndIndex", Arrays.asList(defaultBlockParameter.getValue(), Numeric.encodeQuantity(uncleIndex)), webujService, HucBlock.class);
    }

    @Override
    public Request<?, HucGetCompilers> hucGetCompilers() {
        return new Request<>("huc_getCompilers", Collections.<String>emptyList(), webujService, HucGetCompilers.class);
    }

    @Override
    public Request<?, HucCompileLLL> hucCompileLLL(String sourceCode) {
        return new Request<>("huc_compileLLL", Arrays.asList(sourceCode), webujService, HucCompileLLL.class);
    }

    @Override
    public Request<?, HucCompileSolidity> hucCompileSolidity(String sourceCode) {
        return new Request<>("huc_compileSolidity", Arrays.asList(sourceCode), webujService, HucCompileSolidity.class);
    }

    @Override
    public Request<?, HucCompileSerpent> hucCompileSerpent(String sourceCode) {
        return new Request<>("huc_compileSerpent", Arrays.asList(sourceCode), webujService, HucCompileSerpent.class);
    }

    @Override
    public Request<?, HucFilter> hucNewFilter(org.happyuc.webuj.protocol.core.methods.request.HucFilter hucFilter) {
        return new Request<>("huc_newFilter", Arrays.asList(hucFilter), webujService, HucFilter.class);
    }

    @Override
    public Request<?, HucFilter> hucNewBlockFilter() {
        return new Request<>("huc_newBlockFilter", Collections.<String>emptyList(), webujService, HucFilter.class);
    }

    @Override
    public Request<?, HucFilter> hucNewPendingTransactionFilter() {
        return new Request<>("huc_newPendingTransactionFilter", Collections.<String>emptyList(), webujService, HucFilter.class);
    }

    @Override
    public Request<?, HucUninstallFilter> hucUninstallFilter(BigInteger filterId) {
        return new Request<>("huc_uninstallFilter", Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)), webujService, HucUninstallFilter.class);
    }

    @Override
    public Request<?, HucLog> hucGetFilterChanges(BigInteger filterId) {
        return new Request<>("huc_getFilterChanges", Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)), webujService, HucLog.class);
    }

    @Override
    public Request<?, HucLog> hucGetFilterLogs(BigInteger filterId) {
        return new Request<>("huc_getFilterLogs", Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)), webujService, HucLog.class);
    }

    @Override
    public Request<?, HucLog> hucGetLogs(org.happyuc.webuj.protocol.core.methods.request.HucFilter hucFilter) {
        return new Request<>("huc_getLogs", Arrays.asList(hucFilter), webujService, HucLog.class);
    }

    @Override
    public Request<?, HucGetWork> hucGetWork() {
        return new Request<>("huc_getWork", Collections.<String>emptyList(), webujService, HucGetWork.class);
    }

    @Override
    public Request<?, HucSubmitWork> hucSubmitWork(String nonce, String headerPowHash, String mixDigest) {
        return new Request<>("huc_submitWork", Arrays.asList(nonce, headerPowHash, mixDigest), webujService, HucSubmitWork.class);
    }

    @Override
    public Request<?, HucSubmitHashrate> hucSubmitHashrate(String hashrate, String clientId) {
        return new Request<>("huc_submitHashrate", Arrays.asList(hashrate, clientId), webujService, HucSubmitHashrate.class);
    }

    @Override
    public Request<?, DbPutString> dbPutString(String databaseName, String keyName, String stringToStore) {
        return new Request<>("db_putString", Arrays.asList(databaseName, keyName, stringToStore), webujService, DbPutString.class);
    }

    @Override
    public Request<?, DbGetString> dbGetString(String databaseName, String keyName) {
        return new Request<>("db_getString", Arrays.asList(databaseName, keyName), webujService, DbGetString.class);
    }

    @Override
    public Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore) {
        return new Request<>("db_putHex", Arrays.asList(databaseName, keyName, dataToStore), webujService, DbPutHex.class);
    }

    @Override
    public Request<?, DbGetHex> dbGetHex(String databaseName, String keyName) {
        return new Request<>("db_getHex", Arrays.asList(databaseName, keyName), webujService, DbGetHex.class);
    }

    @Override
    public Request<?, org.happyuc.webuj.protocol.core.methods.response.ShhPost> shhPost(ShhPost shhPost) {
        return new Request<>("shh_post", Arrays.asList(shhPost), webujService, org.happyuc.webuj.protocol.core.methods.response.ShhPost.class);
    }

    @Override
    public Request<?, ShhVersion> shhVersion() {
        return new Request<>("shh_version", Collections.<String>emptyList(), webujService, ShhVersion.class);
    }

    @Override
    public Request<?, ShhNewIdentity> shhNewIdentity() {
        return new Request<>("shh_newIdentity", Collections.<String>emptyList(), webujService, ShhNewIdentity.class);
    }

    @Override
    public Request<?, ShhHasIdentity> shhHasIdentity(String identityAddress) {
        return new Request<>("shh_hasIdentity", Arrays.asList(identityAddress), webujService, ShhHasIdentity.class);
    }

    @Override
    public Request<?, ShhNewGroup> shhNewGroup() {
        return new Request<>("shh_newGroup", Collections.<String>emptyList(), webujService, ShhNewGroup.class);
    }

    @Override
    public Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress) {
        return new Request<>("shh_addToGroup", Arrays.asList(identityAddress), webujService, ShhAddToGroup.class);
    }

    @Override
    public Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter) {
        return new Request<>("shh_newFilter", Arrays.asList(shhFilter), webujService, ShhNewFilter.class);
    }

    @Override
    public Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId) {
        return new Request<>("shh_uninstallFilter", Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)), webujService, ShhUninstallFilter.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId) {
        return new Request<>("shh_getFilterChanges", Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)), webujService, ShhMessages.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetMessages(BigInteger filterId) {
        return new Request<>("shh_getMessages", Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)), webujService, ShhMessages.class);
    }

    @Override
    public Observable<String> hucBlockHashObservable() {
        return webujRx.hucBlockHashObservable(blockTime);
    }

    @Override
    public Observable<String> hucPendingTransactionHashObservable() {
        return webujRx.hucPendingTransactionHashObservable(blockTime);
    }

    @Override
    public Observable<Log> hucLogObservable(org.happyuc.webuj.protocol.core.methods.request.HucFilter hucFilter) {
        return webujRx.hucLogObservable(hucFilter, blockTime);
    }

    @Override
    public Observable<org.happyuc.webuj.protocol.core.methods.response.Transaction> transactionObservable() {
        return webujRx.transactionObservable(blockTime);
    }

    @Override
    public Observable<org.happyuc.webuj.protocol.core.methods.response.Transaction> pendingTransactionObservable() {
        return webujRx.pendingTransactionObservable(blockTime);
    }

    @Override
    public Observable<HucBlock> blockObservable(boolean fullTransactionObjects) {
        return webujRx.blockObservable(fullTransactionObjects, blockTime);
    }

    @Override
    public Observable<HucBlock> replayBlocksObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, boolean fullTransactionObjects) {
        return webujRx.replayBlocksObservable(startBlock, endBlock, fullTransactionObjects);
    }

    @Override
    public Observable<HucBlock> replayBlocksObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock, boolean fullTransactionObjects, boolean ascending) {
        return webujRx.replayBlocksObservable(startBlock, endBlock, fullTransactionObjects, ascending);
    }

    @Override
    public Observable<org.happyuc.webuj.protocol.core.methods.response.Transaction> replayTransactionsObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return webujRx.replayTransactionsObservable(startBlock, endBlock);
    }

    @Override
    public Observable<HucBlock> catchUpToLatestBlockObservable(DefaultBlockParameter startBlock, boolean fullTransactionObjects, Observable<HucBlock> onCompleteObservable) {
        return webujRx.catchUpToLatestBlockObservable(startBlock, fullTransactionObjects, onCompleteObservable);
    }

    @Override
    public Observable<HucBlock> catchUpToLatestBlockObservable(DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return webujRx.catchUpToLatestBlockObservable(startBlock, fullTransactionObjects);
    }

    @Override
    public Observable<org.happyuc.webuj.protocol.core.methods.response.Transaction> catchUpToLatestTransactionObservable(DefaultBlockParameter startBlock) {
        return webujRx.catchUpToLatestTransactionObservable(startBlock);
    }

    @Override
    public Observable<HucBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return webujRx.catchUpToLatestAndSubscribeToNewBlocksObservable(startBlock, fullTransactionObjects, blockTime);
    }

    @Override
    public Observable<org.happyuc.webuj.protocol.core.methods.response.Transaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(DefaultBlockParameter startBlock) {
        return webujRx.catchUpToLatestAndSubscribeToNewTransactionsObservable(startBlock, blockTime);
    }

    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
    }
}
