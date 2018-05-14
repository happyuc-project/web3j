package org.happyuc.webuj.protocol.admin;

import org.happyuc.webuj.protocol.WebujService;

public class AdminFactory {
    public static Admin build(WebujService webujService) {
        return new JsonRpc2_0Admin(webujService);
    }
}
