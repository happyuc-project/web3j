package org.happyuc.webuj.protocol;

import org.happyuc.webuj.protocol.core.Ethereum;
import org.happyuc.webuj.protocol.rx.Web3jRx;
import org.web3j.protocol.core.Ethereum;
import org.web3j.protocol.rx.Web3jRx;

/**
 * JSON-RPC Request object building factory.
 */
public interface Web3j extends Ethereum, Web3jRx {

}
