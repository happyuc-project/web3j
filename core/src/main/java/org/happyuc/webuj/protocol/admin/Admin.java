package org.happyuc.webuj.protocol.admin;

import java.math.BigInteger;
import java.util.concurrent.ScheduledExecutorService;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.admin.methods.response.NewAccountIdentifier;
import org.happyuc.webuj.protocol.admin.methods.response.PersonalListAccounts;
import org.happyuc.webuj.protocol.admin.methods.response.PersonalUnlockAccount;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.request.Transaction;
import org.happyuc.webuj.protocol.core.methods.response.HucSendTransaction;
import org.happyuc.webuj.protocol.WebujService;

/**
 * JSON-RPC Request object building factory for common Parity and Ghuc.
 */
public interface Admin extends Webuj {

    static Admin build(WebujService webujService) {
        return new JsonRpc2_0Admin(webujService);
    }

    static Admin build(WebujService webujService, long pollingInterval, ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0Admin(webujService, pollingInterval, scheduledExecutorService);
    }

    public Request<?, PersonalListAccounts> personalListAccounts();

    public Request<?, NewAccountIdentifier> personalNewAccount(String password);

    public Request<?, PersonalUnlockAccount> personalUnlockAccount(String address, String passphrase, BigInteger duration);

    public Request<?, PersonalUnlockAccount> personalUnlockAccount(String address, String passphrase);

    public Request<?, HucSendTransaction> personalSendTransaction(Transaction transaction, String password);

}   
