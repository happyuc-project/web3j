package org.happyuc.webuj.protocol.admin;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.happyuc.webuj.protocol.admin.methods.response.NewAccountIdentifier;
import org.happyuc.webuj.protocol.admin.methods.response.PersonalListAccounts;
import org.happyuc.webuj.protocol.admin.methods.response.PersonalUnlockAccount;
import org.happyuc.webuj.protocol.WebujService;
import org.happyuc.webuj.protocol.core.JsonRpc2_0Webuj;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.request.ReqTransaction;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;

/**
 * JSON-RPC 2.0 factory implementation for common Parity and Geth.
 */
public class JsonRpc2_0Admin extends JsonRpc2_0Webuj implements Admin {

    public JsonRpc2_0Admin(WebujService webujService) {
        super(webujService);
    }

    @Override
    public Request<?, PersonalListAccounts> personalListAccounts() {
        return new Request<String, PersonalListAccounts>("personal_listAccounts", Collections.<String>emptyList(), webujService, PersonalListAccounts.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> personalNewAccount(String password) {
        return new Request<String, NewAccountIdentifier>("personal_newAccount", Arrays.asList(password), webujService, NewAccountIdentifier.class);
    }

    @Override
    public Request<?, PersonalUnlockAccount> personalUnlockAccount(String accountId, String password, BigInteger duration) {
        List<Object> attributes = new ArrayList<Object>(3);
        attributes.add(accountId);
        attributes.add(password);

        if (duration != null) {
            // Parity has a bug where it won't support a duration
            // See https://github.com/ethcore/parity/issues/1215
            attributes.add(duration.longValue());
        } else {
            // we still need to include the null value, otherwise Parity rejects request
            attributes.add(null);
        }

        return new Request<Object, PersonalUnlockAccount>("personal_unlockAccount", attributes, webujService, PersonalUnlockAccount.class);
    }

    @Override
    public Request<?, PersonalUnlockAccount> personalUnlockAccount(String accountId, String password) {

        return personalUnlockAccount(accountId, password, null);
    }

    @Override
    public Request<?, HucSendRepTransaction> personalSendTransaction(ReqTransaction reqTransaction, String passphrase) {
        return new Request<Object, HucSendRepTransaction>("personal_sendTransaction", Arrays.asList(reqTransaction, passphrase), webujService, HucSendRepTransaction.class);
    }

}
