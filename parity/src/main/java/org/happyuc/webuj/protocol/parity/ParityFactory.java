package org.happyuc.webuj.protocol.parity;

import org.happyuc.webuj.protocol.WebujService;

/**
 * webuj Parity client factory.
 */
public class ParityFactory {

    public static Parity build(WebujService webujService) {
        return new JsonRpc2_0Parity(webujService);
    }
}
