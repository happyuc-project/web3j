package org.happyuc.webuj.protocol.ghuc;

import org.happyuc.webuj.protocol.ghuc.response.PersonalEcRecover;
import org.happyuc.webuj.protocol.ghuc.response.PersonalImportRawKey;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.BooleanResponse;
import org.web3j.protocol.admin.methods.response.PersonalSign;
import org.web3j.protocol.core.Request;

/**
 * JSON-RPC Request object building factory for Geth. 
 */
public interface Geth extends Admin {
    public Request<?, PersonalImportRawKey> personalImportRawKey(String keydata, String password);
    
    public Request<?, BooleanResponse> personalLockAccount(String accountId);
    
    public Request<?, PersonalSign> personalSign(String message, String accountId, String password);
    
    public Request<?, PersonalEcRecover> personalEcRecover(String message, String signiture);
    
}
