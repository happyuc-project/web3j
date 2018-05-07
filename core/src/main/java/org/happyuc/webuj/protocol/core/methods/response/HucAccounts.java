package org.happyuc.webuj.protocol.core.methods.response;

import java.util.List;

import org.happyuc.webuj.protocol.core.Response;

/**
 * huc_accounts.
 */
public class HucAccounts extends Response<List<String>> {
    public List<String> getAccounts() {
        return getResult();
    }
}
