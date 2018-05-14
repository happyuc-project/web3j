package org.happyuc.webuj.protocol.ghuc.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * personal_importRawKey.
 */
public class PersonalImportRawKey extends Response<String> {
    public String getAccountId() {
        return getResult();
    }
}
