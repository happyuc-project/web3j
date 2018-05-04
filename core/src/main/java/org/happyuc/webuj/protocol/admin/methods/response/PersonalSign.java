package org.happyuc.webuj.protocol.admin.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * personal_sign
 * parity_signMessage.
 */
public class PersonalSign extends Response<String> {
    public String getSignedMessage() {
        return getResult();
    }
}
