package org.happyuc.webuj.protocol.admin;

import java.math.BigInteger;

import org.happyuc.webuj.protocol.Webuj;
import org.happyuc.webuj.protocol.admin.methods.response.NewAccountIdentifier;
import org.happyuc.webuj.protocol.admin.methods.response.PersonalListAccounts;
import org.happyuc.webuj.protocol.admin.methods.response.PersonalUnlockAccount;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.request.ReqTransaction;
import org.happyuc.webuj.protocol.core.methods.response.HucSendRepTransaction;

/**
 * JSON-RPC Request object building factory for common Parity and Geth.
 */
public interface Admin extends Webuj {
    public Request<?, PersonalListAccounts> personalListAccounts();

    public Request<?, NewAccountIdentifier> personalNewAccount(String password);

    public Request<?, PersonalUnlockAccount> personalUnlockAccount(String address, String passphrase, BigInteger duration);

    public Request<?, PersonalUnlockAccount> personalUnlockAccount(String address, String passphrase);

    public Request<?, HucSendRepTransaction> personalSendTransaction(ReqTransaction reqTransaction, String password);

}   
