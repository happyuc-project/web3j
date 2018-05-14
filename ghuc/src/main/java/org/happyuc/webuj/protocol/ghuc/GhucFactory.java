package org.happyuc.webuj.protocol.ghuc;

import org.happyuc.webuj.protocol.WebujService;

/**
 * webuj Ghuc client factory.
 */
public class GhucFactory {

    public static Ghuc build(WebujService webujService) {
        return new JsonRpc2_0Ghuc(webujService);
    }
}
