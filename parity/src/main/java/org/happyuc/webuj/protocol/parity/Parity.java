package org.happyuc.webuj.protocol.parity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.happyuc.webuj.crypto.WalletFile;
import org.happyuc.webuj.protocol.admin.Admin;
import org.happyuc.webuj.protocol.admin.methods.response.BooleanResponse;
import org.happyuc.webuj.protocol.admin.methods.response.NewAccountIdentifier;
import org.happyuc.webuj.protocol.admin.methods.response.PersonalSign;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.parity.methods.request.Derivation;
import org.happyuc.webuj.protocol.parity.methods.response.ParityAddressesResponse;
import org.happyuc.webuj.protocol.parity.methods.response.ParityAllAccountsInfo;
import org.happyuc.webuj.protocol.parity.methods.response.ParityDefaultAddressResponse;
import org.happyuc.webuj.protocol.parity.methods.response.ParityDeriveAddress;
import org.happyuc.webuj.protocol.parity.methods.response.ParityExportAccount;
import org.happyuc.webuj.protocol.parity.methods.response.ParityListRecentDapps;

/**
 * JSON-RPC Request object building factory for Parity.
 */
public interface Parity extends Admin, Trace {
    Request<?, ParityAllAccountsInfo> parityAllAccountsInfo();

    Request<?, BooleanResponse> parityChangePassword(String accountId, String oldPassword, String newPassword);

    Request<?, ParityDeriveAddress> parityDeriveAddressHash(String accountId, String password, Derivation hashType, boolean toSave);

    Request<?, ParityDeriveAddress> parityDeriveAddressIndex(String accountId, String password, List<Derivation> indicesType, boolean toSave);

    Request<?, ParityExportAccount> parityExportAccount(String accountId, String password);

    Request<?, ParityAddressesResponse> parityGetDappAddresses(String dAppId);

    Request<?, ParityDefaultAddressResponse> parityGetDappDefaultAddress(String dAppId);

    Request<?, ParityAddressesResponse> parityGetNewDappsAddresses();

    Request<?, ParityDefaultAddressResponse> parityGetNewDappsDefaultAddress();

    Request<?, ParityAddressesResponse> parityImportGethAccounts(ArrayList<String> gethAddresses);

    Request<?, BooleanResponse> parityKillAccount(String accountId, String password);

    Request<?, ParityAddressesResponse> parityListGethAccounts();

    Request<?, ParityListRecentDapps> parityListRecentDapps();

    Request<?, NewAccountIdentifier> parityNewAccountFromPhrase(String phrase, String password);

    Request<?, NewAccountIdentifier> parityNewAccountFromSecret(String secret, String password);

    Request<?, NewAccountIdentifier> parityNewAccountFromWallet(WalletFile walletFile, String password);

    Request<?, BooleanResponse> parityRemoveAddress(String accountId);

    Request<?, BooleanResponse> paritySetAccountMeta(String accountId, Map<String, Object> metadata);

    Request<?, BooleanResponse> paritySetAccountName(String address, String name);

    Request<?, BooleanResponse> paritySetDappAddresses(String dAppId, ArrayList<String> availableAccountIds);

    Request<?, BooleanResponse> paritySetDappDefaultAddress(String dAppId, String defaultAddress);

    Request<?, BooleanResponse> paritySetNewDappsAddresses(ArrayList<String> availableAccountIds);

    Request<?, BooleanResponse> paritySetNewDappsDefaultAddress(String defaultAddress);

    Request<?, BooleanResponse> parityTestPassword(String accountId, String password);

    Request<?, PersonalSign> paritySignMessage(String accountId, String password, String hexMessage);
}
