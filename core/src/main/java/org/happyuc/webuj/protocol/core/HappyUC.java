package org.happyuc.webuj.protocol.core;

import java.math.BigInteger;

import org.happyuc.webuj.protocol.core.methods.request.ShhFilter;
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
import org.happyuc.webuj.protocol.core.methods.response.WebuClientVersion;
import org.happyuc.webuj.protocol.core.methods.response.WebuSha3;

/**
 * Core HappyUC JSON-RPC API.
 */
public interface HappyUC {
    Request<?, WebuClientVersion> webuClientVersion();

    Request<?, WebuSha3> webuSha3(String data);

    Request<?, NetVersion> netVersion();

    Request<?, NetListening> netListening();

    Request<?, NetPeerCount> netPeerCount();

    Request<?, HucProtocolVersion> hucProtocolVersion();

    Request<?, HucCoinbase> hucCoinbase();

    Request<?, HucSyncing> hucSyncing();

    Request<?, HucMining> hucMining();

    Request<?, HucHashrate> hucHashrate();

    Request<?, HucGasPrice> hucGasPrice();

    Request<?, HucAccounts> hucAccounts();

    Request<?, HucBlockNumber> hucBlockNumber();

    Request<?, HucGetBalance> hucGetBalance(String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, HucGetStorageAt> hucGetStorageAt(String address, BigInteger position, DefaultBlockParameter defaultBlockParameter);

    Request<?, HucGetTransactionCount> hucGetTransactionCount(String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, HucGetBlockTransactionCountByHash> hucGetBlockTransactionCountByHash(String blockHash);

    Request<?, HucGetBlockTransactionCountByNumber> hucGetBlockTransactionCountByNumber(DefaultBlockParameter defaultBlockParameter);

    Request<?, HucGetUncleCountByBlockHash> hucGetUncleCountByBlockHash(String blockHash);

    Request<?, HucGetUncleCountByBlockNumber> hucGetUncleCountByBlockNumber(DefaultBlockParameter defaultBlockParameter);

    Request<?, HucGetCode> hucGetCode(String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, HucSign> hucSign(String address, String sha3HashOfDataToSign);

    Request<?, org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction> hucSendTransaction(org.happyuc.webuj.protocol.core.methods.request.Transaction transaction);

    Request<?, org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction> hucSendRawTransaction(String signedTransactionData);

    Request<?, org.happyuc.webuj.protocol.core.methods.response.HucCall> hucCall(org.happyuc.webuj.protocol.core.methods.request.Transaction transaction, DefaultBlockParameter defaultBlockParameter);

    Request<?, HucEstimateGas> hucEstimateGas(org.happyuc.webuj.protocol.core.methods.request.Transaction transaction);

    Request<?, HucBlock> hucGetBlockByHash(String blockHash, boolean returnFullTransactionObjects);

    Request<?, HucBlock> hucGetBlockByNumber(DefaultBlockParameter defaultBlockParameter, boolean returnFullTransactionObjects);

    Request<?, HucTransaction> hucGetTransactionByHash(String transactionHash);

    Request<?, HucTransaction> hucGetTransactionByBlockHashAndIndex(String blockHash, BigInteger transactionIndex);

    Request<?, HucTransaction> hucGetTransactionByBlockNumberAndIndex(DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex);

    Request<?, HucGetTransactionReceipt> hucGetTransactionReceipt(String transactionHash);

    Request<?, HucBlock> hucGetUncleByBlockHashAndIndex(String blockHash, BigInteger transactionIndex);

    Request<?, HucBlock> hucGetUncleByBlockNumberAndIndex(DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex);

    Request<?, HucGetCompilers> hucGetCompilers();

    Request<?, HucCompileLLL> hucCompileLLL(String sourceCode);

    Request<?, HucCompileSolidity> hucCompileSolidity(String sourceCode);

    Request<?, HucCompileSerpent> hucCompileSerpent(String sourceCode);

    Request<?, HucFilter> hucNewFilter(org.happyuc.webuj.protocol.core.methods.request.HucFilter hucFilter);

    Request<?, HucFilter> hucNewBlockFilter();

    Request<?, HucFilter> hucNewPendingTransactionFilter();

    Request<?, HucUninstallFilter> hucUninstallFilter(BigInteger filterId);

    Request<?, HucLog> hucGetFilterChanges(BigInteger filterId);

    Request<?, HucLog> hucGetFilterLogs(BigInteger filterId);

    Request<?, HucLog> hucGetLogs(org.happyuc.webuj.protocol.core.methods.request.HucFilter hucFilter);

    Request<?, HucGetWork> hucGetWork();

    Request<?, HucSubmitWork> hucSubmitWork(String nonce, String headerPowHash, String mixDigest);

    Request<?, HucSubmitHashrate> hucSubmitHashrate(String hashrate, String clientId);

    Request<?, DbPutString> dbPutString(String databaseName, String keyName, String stringToStore);

    Request<?, DbGetString> dbGetString(String databaseName, String keyName);

    Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore);

    Request<?, DbGetHex> dbGetHex(String databaseName, String keyName);

    Request<?, org.happyuc.webuj.protocol.core.methods.response.ShhPost> shhPost(org.happyuc.webuj.protocol.core.methods.request.ShhPost shhPost);

    Request<?, ShhVersion> shhVersion();

    Request<?, ShhNewIdentity> shhNewIdentity();

    Request<?, ShhHasIdentity> shhHasIdentity(String identityAddress);

    Request<?, ShhNewGroup> shhNewGroup();

    Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress);

    Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter);

    Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId);

    Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId);

    Request<?, ShhMessages> shhGetMessages(BigInteger filterId);
}
