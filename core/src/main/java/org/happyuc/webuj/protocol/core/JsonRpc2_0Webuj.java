package org.happyuc.webuj.protocol.core;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.WebujService;
import org.happyuc.webuj.protocol.core.methods.request.HucReqFilter;
import org.happyuc.webuj.protocol.core.methods.request.ReqTransaction;
import org.happyuc.webuj.protocol.core.methods.request.ShhFilter;
import org.happyuc.webuj.protocol.core.methods.request.ShhReqPost;
import org.happyuc.webuj.protocol.core.methods.response.DbGetHex;
import org.happyuc.webuj.protocol.core.methods.response.DbGetString;
import org.happyuc.webuj.protocol.core.methods.response.DbPutHex;
import org.happyuc.webuj.protocol.core.methods.response.DbPutString;
import org.happyuc.webuj.protocol.core.methods.response.HucBlock;
import org.happyuc.webuj.protocol.core.methods.response.HucBlockNumber;
import org.happyuc.webuj.protocol.core.methods.response.HucCall;
import org.happyuc.webuj.protocol.core.methods.response.HucCoinbase;
import org.happyuc.webuj.protocol.core.methods.response.HucCompileLLL;
import org.happyuc.webuj.protocol.core.methods.response.HucCompileSerpent;
import org.happyuc.webuj.protocol.core.methods.response.HucCompileSolidity;
import org.happyuc.webuj.protocol.core.methods.response.HucEstimateGas;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionCount;
import org.happyuc.webuj.protocol.core.methods.response.HucGetRepTransactionReceipt;
import org.happyuc.webuj.protocol.core.methods.response.HucGetWork;
import org.happyuc.webuj.protocol.core.methods.response.HucLog;
import org.happyuc.webuj.protocol.core.methods.response.HucRepFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucGasPrice;
import org.happyuc.webuj.protocol.core.methods.response.HucGetBalance;
import org.happyuc.webuj.protocol.core.methods.response.HucGetBlockRepTransactionCountByHash;
import org.happyuc.webuj.protocol.core.methods.response.HucGetBlockRepTransactionCountByNumber;
import org.happyuc.webuj.protocol.core.methods.response.HucGetCode;
import org.happyuc.webuj.protocol.core.methods.response.HucGetCompilers;
import org.happyuc.webuj.protocol.core.methods.response.HucGetStorageAt;
import org.happyuc.webuj.protocol.core.methods.response.HucGetUncleCountByBlockHash;
import org.happyuc.webuj.protocol.core.methods.response.HucGetUncleCountByBlockNumber;
import org.happyuc.webuj.protocol.core.methods.response.HucHashrate;
import org.happyuc.webuj.protocol.core.methods.response.HucMining;
import org.happyuc.webuj.protocol.core.methods.response.HucProtocolVersion;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;
import org.happyuc.webuj.protocol.core.methods.response.HucSign;
import org.happyuc.webuj.protocol.core.methods.response.HucSubmitHashrate;
import org.happyuc.webuj.protocol.core.methods.response.HucSubmitWork;
import org.happyuc.webuj.protocol.core.methods.response.HucSyncing;
import org.happyuc.webuj.protocol.core.methods.response.HucRepTransaction;
import org.happyuc.webuj.protocol.core.methods.response.HucUninstallFilter;
import org.happyuc.webuj.protocol.core.methods.response.HucAccounts;
import org.happyuc.webuj.protocol.core.methods.response.Log;
import org.happyuc.webuj.protocol.core.methods.response.NetListening;
import org.happyuc.webuj.protocol.core.methods.response.NetPeerCount;
import org.happyuc.webuj.protocol.core.methods.response.NetVersion;
import org.happyuc.webuj.protocol.core.methods.response.RepTransaction;
import org.happyuc.webuj.protocol.core.methods.response.ShhAddToGroup;
import org.happyuc.webuj.protocol.core.methods.response.ShhHasIdentity;
import org.happyuc.webuj.protocol.core.methods.response.ShhMessages;
import org.happyuc.webuj.protocol.core.methods.response.ShhNewFilter;
import org.happyuc.webuj.protocol.core.methods.response.ShhNewGroup;
import org.happyuc.webuj.protocol.core.methods.response.ShhNewIdentity;
import org.happyuc.webuj.protocol.core.methods.response.ShhRepPost;
import org.happyuc.webuj.protocol.core.methods.response.ShhUninstallFilter;
import org.happyuc.webuj.protocol.core.methods.response.ShhVersion;
import org.happyuc.webuj.protocol.core.methods.response.WebuClientVersion;
import org.happyuc.webuj.protocol.core.methods.response.WebuSha3;
import org.happyuc.webuj.protocol.rx.JsonRpc2_0Rx;
import org.happyuc.webuj.utils.Async;
import org.happyuc.webuj.utils.Numeric;
import rx.Observable;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

/**
 * JSON-RPC 2.0 factory implementation.
 */
public class JsonRpc2_0Webuj implements Webuj {

    public static final int DEFAULT_BLOCK_TIME = 15 * 1000;

    protected final WebujService webujService;
    private final JsonRpc2_0Rx webujRx;
    private final long blockTime;

    public JsonRpc2_0Webuj(WebujService webujService) {
        this(webujService, DEFAULT_BLOCK_TIME, Async.defaultExecutorService());
    }

    public JsonRpc2_0Webuj(WebujService webujService, long pollingInterval, ScheduledExecutorService scheduledExecutorService) {
        this.webujService = webujService;
        this.webujRx = new JsonRpc2_0Rx(this, scheduledExecutorService);
        this.blockTime = pollingInterval;
    }

    @Override
    public Request<?, WebuClientVersion> webuClientVersion() {
        return new Request<>("webu_clientVersion", Collections.<String>emptyList(), webujService, WebuClientVersion.class);
    }

    @Override
    public Request<?, WebuSha3> webuSha3(String data) {
        return new Request<>("webu_sha3", Collections.singletonList(data), webujService, WebuSha3.class);
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
    public Request<?, HucGetRepTransactionCount> hucGetTransactionCount(String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>("huc_getTransactionCount", Arrays.asList(address, defaultBlockParameter.getValue()), webujService, HucGetRepTransactionCount.class);
    }

    @Override
    public Request<?, HucGetBlockRepTransactionCountByHash> hucGetBlockTransactionCountByHash(String blockHash) {
        return new Request<>("huc_getBlockTransactionCountByHash", Collections.singletonList(blockHash), webujService, HucGetBlockRepTransactionCountByHash.class);
    }

    @Override
    public Request<?, HucGetBlockRepTransactionCountByNumber> hucGetBlockTransactionCountByNumber(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>("huc_getBlockTransactionCountByNumber", Collections.singletonList(defaultBlockParameter.getValue()), webujService, HucGetBlockRepTransactionCountByNumber.class);
    }

    @Override
    public Request<?, HucGetUncleCountByBlockHash> hucGetUncleCountByBlockHash(String blockHash) {
        return new Request<>("huc_getUncleCountByBlockHash", Collections.singletonList(blockHash), webujService, HucGetUncleCountByBlockHash.class);
    }

    @Override
    public Request<?, HucGetUncleCountByBlockNumber> hucGetUncleCountByBlockNumber(DefaultBlockParameter defaultBlockParameter) {
        return new Request<>("huc_getUncleCountByBlockNumber", Collections.singletonList(defaultBlockParameter.getValue()), webujService, HucGetUncleCountByBlockNumber.class);
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
    public Request<?, HucSendRepTransaction> hucSendTransaction(ReqTransaction reqTransaction) {
        return new Request<>("huc_sendTransaction", Collections.singletonList(reqTransaction), webujService, HucSendRepTransaction.class);
    }

    @Override
    public Request<?, HucSendRepTransaction> hucSendRawTransaction(String signedTransactionData) {
        return new Request<>("huc_sendRawTransaction", Collections.singletonList(signedTransactionData), webujService, HucSendRepTransaction.class);
    }

    @Override
    public Request<?, HucCall> hucCall(ReqTransaction reqTransaction, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>("huc_call", Arrays.asList(reqTransaction, defaultBlockParameter), webujService, HucCall.class);
    }

    @Override
    public Request<?, HucEstimateGas> hucEstimateGas(ReqTransaction reqTransaction) {
        return new Request<>("huc_estimateGas", Collections.singletonList(reqTransaction), webujService, HucEstimateGas.class);
    }

    @Override
    public Request<?, HucBlock> hucGetBlockByHash(String blockHash, boolean returnFullTransactionObjects) {
        return new Request<>("huc_getBlockByHash", Arrays.<Object>asList(blockHash, returnFullTransactionObjects), webujService, HucBlock.class);
    }

    @Override
    public Request<?, HucBlock> hucGetBlockByNumber(DefaultBlockParameter defaultBlockParameter, boolean returnFullTransactionObjects) {
        return new Request<>("huc_getBlockByNumber", Arrays.<Object>asList(defaultBlockParameter.getValue(), returnFullTransactionObjects), webujService, HucBlock.class);
    }

    @Override
    public Request<?, HucRepTransaction> hucGetTransactionByHash(String transactionHash) {
        return new Request<>("huc_getTransactionByHash", Collections.singletonList(transactionHash), webujService, HucRepTransaction.class);
    }

    @Override
    public Request<?, HucRepTransaction> hucGetTransactionByBlockHashAndIndex(String blockHash, BigInteger transactionIndex) {
        return new Request<>("huc_getTransactionByBlockHashAndIndex", Arrays.asList(blockHash, Numeric.encodeQuantity(transactionIndex)), webujService, HucRepTransaction.class);
    }

    @Override
    public Request<?, HucRepTransaction> hucGetTransactionByBlockNumberAndIndex(DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex) {
        return new Request<>("huc_getTransactionByBlockNumberAndIndex", Arrays.asList(defaultBlockParameter.getValue(), Numeric.encodeQuantity(transactionIndex)), webujService, HucRepTransaction.class);
    }

    @Override
    public Request<?, HucGetRepTransactionReceipt> hucGetTransactionReceipt(String transactionHash) {
        return new Request<>("huc_getTransactionReceipt", Collections.singletonList(transactionHash), webujService, HucGetRepTransactionReceipt.class);
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
        return new Request<>("huc_compileLLL", Collections.singletonList(sourceCode), webujService, HucCompileLLL.class);
    }

    @Override
    public Request<?, HucCompileSolidity> hucCompileSolidity(String sourceCode) {
        return new Request<>("huc_compileSolidity", Collections.singletonList(sourceCode), webujService, HucCompileSolidity.class);
    }

    @Override
    public Request<?, HucCompileSerpent> hucCompileSerpent(String sourceCode) {
        return new Request<>("huc_compileSerpent", Collections.singletonList(sourceCode), webujService, HucCompileSerpent.class);
    }

    @Override
    public Request<?, HucRepFilter> hucNewFilter(HucReqFilter hucReqFilter) {
        return new Request<>("huc_newFilter", Collections.singletonList(hucReqFilter), webujService, HucRepFilter.class);
    }

    @Override
    public Request<?, HucRepFilter> hucNewBlockFilter() {
        return new Request<>("huc_newBlockFilter", Collections.<String>emptyList(), webujService, HucRepFilter.class);
    }

    @Override
    public Request<?, HucRepFilter> hucNewPendingTransactionFilter() {
        return new Request<>("huc_newPendingTransactionFilter", Collections.<String>emptyList(), webujService, HucRepFilter.class);
    }

    @Override
    public Request<?, HucUninstallFilter> hucUninstallFilter(BigInteger filterId) {
        return new Request<>("huc_uninstallFilter", Collections.singletonList(Numeric.toHexStringWithPrefixSafe(filterId)), webujService, HucUninstallFilter.class);
    }

    @Override
    public Request<?, HucLog> hucGetFilterChanges(BigInteger filterId) {
        return new Request<>("huc_getFilterChanges", Collections.singletonList(Numeric.toHexStringWithPrefixSafe(filterId)), webujService, HucLog.class);
    }

    @Override
    public Request<?, HucLog> hucGetFilterLogs(BigInteger filterId) {
        return new Request<>("huc_getFilterLogs", Collections.singletonList(Numeric.toHexStringWithPrefixSafe(filterId)), webujService, HucLog.class);
    }

    @Override
    public Request<?, HucLog> hucGetLogs(HucReqFilter hucReqFilter) {
        return new Request<>("huc_getLogs", Collections.singletonList(hucReqFilter), webujService, HucLog.class);
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
    public Request<?, ShhRepPost> shhPost(ShhReqPost shhReqPost) {
        return new Request<>("shh_post", Collections.singletonList(shhReqPost), webujService, ShhRepPost.class);
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
        return new Request<>("shh_hasIdentity", Collections.singletonList(identityAddress), webujService, ShhHasIdentity.class);
    }

    @Override
    public Request<?, ShhNewGroup> shhNewGroup() {
        return new Request<>("shh_newGroup", Collections.<String>emptyList(), webujService, ShhNewGroup.class);
    }

    @Override
    public Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress) {
        return new Request<>("shh_addToGroup", Collections.singletonList(identityAddress), webujService, ShhAddToGroup.class);
    }

    @Override
    public Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter) {
        return new Request<>("shh_newFilter", Collections.singletonList(shhFilter), webujService, ShhNewFilter.class);
    }

    @Override
    public Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId) {
        return new Request<>("shh_uninstallFilter", Collections.singletonList(Numeric.toHexStringWithPrefixSafe(filterId)), webujService, ShhUninstallFilter.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId) {
        return new Request<>("shh_getFilterChanges", Collections.singletonList(Numeric.toHexStringWithPrefixSafe(filterId)), webujService, ShhMessages.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetMessages(BigInteger filterId) {
        return new Request<>("shh_getMessages", Collections.singletonList(Numeric.toHexStringWithPrefixSafe(filterId)), webujService, ShhMessages.class);
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
    public Observable<Log> hucLogObservable(HucReqFilter hucReqFilter) {
        return webujRx.hucLogObservable(hucReqFilter, blockTime);
    }

    @Override
    public Observable<RepTransaction> transactionObservable() {
        return webujRx.transactionObservable(blockTime);
    }

    @Override
    public Observable<RepTransaction> pendingTransactionObservable() {
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
    public Observable<RepTransaction> replayTransactionsObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
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
    public Observable<RepTransaction> catchUpToLatestTransactionObservable(DefaultBlockParameter startBlock) {
        return webujRx.catchUpToLatestTransactionObservable(startBlock);
    }

    @Override
    public Observable<HucBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return webujRx.catchUpToLatestAndSubscribeToNewBlocksObservable(startBlock, fullTransactionObjects, blockTime);
    }

    @Override
    public Observable<RepTransaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(DefaultBlockParameter startBlock) {
        return webujRx.catchUpToLatestAndSubscribeToNewTransactionsObservable(startBlock, blockTime);
    }
}
