package org.happyuc.webuj.protocol.ghuc;

import org.happyuc.webuj.protocol.ghuc.response.PersonalEcRecover;
import org.happyuc.webuj.protocol.ghuc.response.PersonalImportRawKey;
import org.happyuc.webuj.protocol.admin.Admin;
import org.happyuc.webuj.protocol.admin.methods.response.BooleanResponse;
import org.happyuc.webuj.protocol.admin.methods.response.PersonalSign;
import org.happyuc.webuj.protocol.core.Request;

/**
 * JSON-RPC Request object building factory for Ghuc.
 */
public interface Ghuc extends Admin {
    public Request<?, PersonalImportRawKey> personalImportRawKey(String keydata, String password);

    public Request<?, BooleanResponse> personalLockAccount(String accountId);

    public Request<?, PersonalSign> personalSign(String message, String accountId, String password);

    public Request<?, PersonalEcRecover> personalEcRecover(String message, String signiture);

}
