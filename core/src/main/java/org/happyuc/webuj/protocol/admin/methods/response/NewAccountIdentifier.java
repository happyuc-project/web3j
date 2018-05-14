package org.happyuc.webuj.protocol.admin.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * personal_newAccount
 * parity_newAccountFromPhrase
 * parity_newAccountFromSecret
 * parity_newAccountFromWallet.
 */
public class NewAccountIdentifier extends Response<String> {
    public String getAccountId() {
        return getResult();
    }
}
