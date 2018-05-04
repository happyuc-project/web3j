package org.happyuc.webuj.protocol.parity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.happyuc.webuj.crypto.WalletFile;
import org.happyuc.webuj.protocol.webujService;
import org.happyuc.webuj.protocol.admin.JsonRpc2_0Admin;
import org.happyuc.webuj.protocol.admin.methods.response.BooleanResponse;
import org.happyuc.webuj.protocol.admin.methods.response.NewAccountIdentifier;
import org.happyuc.webuj.protocol.admin.methods.response.PersonalSign;
import org.happyuc.webuj.protocol.core.DefaultBlockParameter;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.request.Transaction;
import org.happyuc.webuj.protocol.parity.methods.request.Derivation;
import org.happyuc.webuj.protocol.parity.methods.request.TraceFilter;
import org.happyuc.webuj.protocol.parity.methods.response.ParityAddressesResponse;
import org.happyuc.webuj.protocol.parity.methods.response.ParityAllAccountsInfo;
import org.happyuc.webuj.protocol.parity.methods.response.ParityDefaultAddressResponse;
import org.happyuc.webuj.protocol.parity.methods.response.ParityDeriveAddress;
import org.happyuc.webuj.protocol.parity.methods.response.ParityExportAccount;
import org.happyuc.webuj.protocol.parity.methods.response.ParityFullTraceResponse;
import org.happyuc.webuj.protocol.parity.methods.response.ParityListRecentDapps;
import org.happyuc.webuj.protocol.parity.methods.response.ParityTraceGet;
import org.happyuc.webuj.protocol.parity.methods.response.ParityTracesResponse;
import org.happyuc.webuj.utils.Numeric;

/**
 * JSON-RPC 2.0 factory implementation for Parity.
 */
public class JsonRpc2_0Parity extends JsonRpc2_0Admin implements Parity {

    public JsonRpc2_0Parity(webujService webujService) {
        super(webujService);
    }

    @Override
    public Request<?, ParityAllAccountsInfo> parityAllAccountsInfo() {
        return new Request<>(
                "parity_allAccountsInfo",
                Collections.<String>emptyList(),
                webujService,
                ParityAllAccountsInfo.class);
    }

    @Override
    public Request<?, BooleanResponse> parityChangePassword(
            String accountId, String oldPassword, String newPassword) {
        return new Request<>(
                "parity_changePassword",
                Arrays.asList(accountId, oldPassword, newPassword),
                webujService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, ParityDeriveAddress> parityDeriveAddressHash(
            String accountId, String password, Derivation hashType, boolean toSave) {
        return new Request<>(
                "parity_deriveAddressHash",
                Arrays.asList(accountId, password, hashType, toSave),
                webujService,
                ParityDeriveAddress.class);
    }

    @Override
    public Request<?, ParityDeriveAddress> parityDeriveAddressIndex(
            String accountId, String password,
            List<Derivation> indicesType, boolean toSave) {
        return new Request<>(
                "parity_deriveAddressIndex",
                Arrays.asList(accountId, password, indicesType, toSave),
                webujService,
                ParityDeriveAddress.class);
    }

    @Override
    public Request<?, ParityExportAccount> parityExportAccount(
            String accountId, String password) {
        return new Request<>(
                "parity_exportAccount",
                Arrays.asList(accountId, password),
                webujService,
                ParityExportAccount.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityGetDappAddresses(String dAppId) {
        return new Request<>(
                "parity_getDappAddresses",
                Arrays.asList(dAppId),
                webujService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityDefaultAddressResponse> parityGetDappDefaultAddress(String dAppId) {
        return new Request<>(
                "parity_getDappDefaultAddress",
                Arrays.asList(dAppId),
                webujService,
                ParityDefaultAddressResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityGetNewDappsAddresses() {
        return new Request<>(
                "parity_getNewDappsAddresses",
                Collections.<String>emptyList(),
                webujService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityDefaultAddressResponse> parityGetNewDappsDefaultAddress() {
        return new Request<>(
                "parity_getNewDappsDefaultAddress",
                Collections.<String>emptyList(),
                webujService,
                ParityDefaultAddressResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityImportGhucAccounts(
            ArrayList<String> ghucAddresses) {
        return new Request<>(
                "parity_importGhucAccounts",
                ghucAddresses,
                webujService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> parityKillAccount(String accountId, String password) {
        return new Request<>(
                "parity_killAccount",
                Arrays.asList(accountId, password),
                webujService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityListAccounts(
            BigInteger quantity, String accountId, DefaultBlockParameter blockParameter) {
        if (blockParameter == null) {
            return new Request<>(
                    "parity_listAccounts",
                    Arrays.asList(quantity, accountId),
                    webujService,
                    ParityAddressesResponse.class);
        } else {
            return new Request<>(
                    "parity_listAccounts",
                    Arrays.asList(quantity, accountId, blockParameter),
                    webujService,
                    ParityAddressesResponse.class);
        }
    }

    @Override
    public Request<?, ParityAddressesResponse> parityListGhucAccounts() {
        return new Request<>(
                "parity_listGhucAccounts",
                Collections.<String>emptyList(),
                webujService,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityListRecentDapps> parityListRecentDapps() {
        return new Request<>(
                "parity_listRecentDapps",
                Collections.<String>emptyList(),
                webujService,
                ParityListRecentDapps.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromPhrase(
            String phrase, String password) {
        return new Request<>(
                "parity_newAccountFromPhrase",
                Arrays.asList(phrase, password),
                webujService,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromSecret(
            String secret, String password) {
        return new Request<>(
                "parity_newAccountFromSecret",
                Arrays.asList(secret, password),
                webujService,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromWallet(
            WalletFile walletFile, String password) {
        return new Request<>(
                "parity_newAccountFromWallet",
                Arrays.asList(walletFile, password),
                webujService,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, BooleanResponse> parityRemoveAddress(String accountId) {
        return new Request<>(
                "parity_RemoveAddress",
                Arrays.asList(accountId),
                webujService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetAccountMeta(
            String accountId, Map<String, Object> metadata) {
        return new Request<>(
                "parity_setAccountMeta",
                Arrays.asList(accountId, metadata),
                webujService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetAccountName(
            String address, String name) {
        return new Request<>(
                "parity_setAccountName",
                Arrays.asList(address, name),
                webujService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetDappAddresses(
            String dAppId, ArrayList<String> availableAccountIds) {
        return new Request<>(
                "parity_setDappAddresses",
                Arrays.asList(dAppId, availableAccountIds),
                webujService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetDappDefaultAddress(
            String dAppId, String defaultAddress) {
        return new Request<>(
                "parity_setDappDefaultAddress",
                Arrays.asList(dAppId, defaultAddress),
                webujService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetNewDappsAddresses(
            ArrayList<String> availableAccountIds) {
        return new Request<>(
                "parity_setNewDappsAddresses",
                Arrays.asList(availableAccountIds),
                webujService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetNewDappsDefaultAddress(String defaultAddress) {
        return new Request<>(
                "parity_setNewDappsDefaultAddress",
                Arrays.asList(defaultAddress),
                webujService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> parityTestPassword(String accountId, String password) {
        return new Request<>(
                "parity_testPassword",
                Arrays.asList(accountId, password),
                webujService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, PersonalSign> paritySignMessage(
            String accountId, String password, String hexMessage) {
        return new Request<>(
                "parity_signMessage",
                Arrays.asList(accountId,password,hexMessage),
                webujService,
                PersonalSign.class);
    }
    
    // TRACE API
    
    @Override
    public Request<?, ParityFullTraceResponse> traceCall(
            Transaction transaction, List<String> traces, DefaultBlockParameter blockParameter) {
        return new Request<>(
            "trace_call",
            Arrays.asList(transaction, traces, blockParameter),
            webujService,
            ParityFullTraceResponse.class);
    }
    
    @Override
    public Request<?, ParityFullTraceResponse> traceRawTransaction(
            String data, List<String> traceTypes) {
        return new Request<>(
            "trace_rawTransaction",
            Arrays.asList(data, traceTypes),
            webujService,
            ParityFullTraceResponse.class);
    }
    
    @Override
    public Request<?, ParityFullTraceResponse> traceReplayTransaction(
            String hash, List<String> traceTypes) {
        return new Request<>(
            "trace_replayTransaction",
            Arrays.asList(hash, traceTypes),
            webujService,
            ParityFullTraceResponse.class);
    }
    
    @Override
    public Request<?, ParityTracesResponse> traceBlock(DefaultBlockParameter blockParameter) {
        return new Request<>(
            "trace_block",
            Arrays.asList(blockParameter),
            webujService,
            ParityTracesResponse.class);
    }
    
    @Override
    public Request<?, ParityTracesResponse> traceFilter(TraceFilter traceFilter) {
        return new Request<>(
            "trace_filter",
            Arrays.asList(traceFilter),
            webujService,
            ParityTracesResponse.class);
    }
    
    @Override
    public Request<?, ParityTraceGet> traceGet(String hash, List<BigInteger> indices) {
        List<String> encodedIndices = indices.stream()
                .map(Numeric::encodeQuantity)
                .collect(Collectors.toList());
        return new Request<>(
            "trace_get",
            Arrays.asList(hash, encodedIndices),
            webujService,
            ParityTraceGet.class);
    }

    @Override
    public Request<?, ParityTracesResponse> traceTransaction(String hash) {
        return new Request<>(
            "trace_transaction",
            Arrays.asList(hash),
            webujService,
            ParityTracesResponse.class);
    }
}
