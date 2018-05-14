package org.happyuc.webuj.protocol.admin.methods.response;

import org.web3j.protocol.core.Response;

/**
 * personal_unlockAccount.
 */
public class PersonalUnlockAccount extends Response<Boolean> {
    public Boolean accountUnlocked() {
        return getResult();
    }
}