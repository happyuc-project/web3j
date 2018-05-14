package org.happyuc.webuj.protocol.ghuc;

import org.happyuc.webuj.protocol.WebujService;
import org.happyuc.webuj.protocol.admin.JsonRpc2_0Admin;
import org.happyuc.webuj.protocol.admin.methods.response.BooleanResponse;
import org.happyuc.webuj.protocol.admin.methods.response.PersonalSign;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.ghuc.response.PersonalEcRecover;
import org.happyuc.webuj.protocol.ghuc.response.PersonalImportRawKey;

import java.util.Arrays;

/**
 * JSON-RPC 2.0 factory implementation for Ghuc.
 */
class JsonRpc2_0Ghuc extends JsonRpc2_0Admin implements Ghuc {

    public JsonRpc2_0Ghuc(WebujService webujService) {
        super(webujService);
    }

    @Override
    public Request<?, PersonalImportRawKey> personalImportRawKey(String keydata, String password) {
        return new Request<String, PersonalImportRawKey>("personal_importRawKey", Arrays.asList(keydata, password), webujService, PersonalImportRawKey.class);
    }

    @Override
    public Request<?, BooleanResponse> personalLockAccount(String accountId) {
        return new Request<String, BooleanResponse>("personal_lockAccount", Arrays.asList(accountId), webujService, BooleanResponse.class);
    }

    @Override
    public Request<?, PersonalSign> personalSign(String message, String accountId, String password) {
        return new Request<String, PersonalSign>("personal_sign", Arrays.asList(message, accountId, password), webujService, PersonalSign.class);
    }

    @Override
    public Request<?, PersonalEcRecover> personalEcRecover(String hexMessage, String signedMessage) {
        return new Request<String, PersonalEcRecover>("personal_ecRecover", Arrays.asList(hexMessage, signedMessage), webujService, PersonalEcRecover.class);
    }

}
