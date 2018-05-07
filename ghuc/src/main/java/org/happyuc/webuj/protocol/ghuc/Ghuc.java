package org.happyuc.webuj.protocol.ghuc;

import org.happyuc.webuj.protocol.WebujService;
import org.happyuc.webuj.protocol.admin.Admin;
import org.happyuc.webuj.protocol.admin.methods.response.BooleanResponse;
import org.happyuc.webuj.protocol.admin.methods.response.PersonalSign;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.methods.response.MinerStartResponse;
import org.happyuc.webuj.protocol.ghuc.response.PersonalEcRecover;
import org.happyuc.webuj.protocol.ghuc.response.PersonalImportRawKey;

/**
 * JSON-RPC Request object building factory for Ghuc. 
 */
public interface Ghuc extends Admin {
    static Ghuc build(WebujService webujService) {
        return new JsonRpc2_0Ghuc(webujService);
    }
        
    Request<?, PersonalImportRawKey> personalImportRawKey(String keydata, String password);
    
    Request<?, BooleanResponse> personalLockAccount(String accountId);
    
    Request<?, PersonalSign> personalSign(String message, String accountId, String password);
    
    Request<?, PersonalEcRecover> personalEcRecover(String message, String signiture);

    Request<?, MinerStartResponse> minerStart(int threadCount);

    Request<?, BooleanResponse> minerStop();

}
