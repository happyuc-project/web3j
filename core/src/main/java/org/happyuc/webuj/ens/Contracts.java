package org.happyuc.webuj.ens;

import org.happyuc.webuj.tx.ChainId;

/**
 * ENS registry contract addresses.
 */
public class Contracts {
    public static final String MAINNET = "0xeaeb3f3277618955806fc76ae6b28b23c85ee974c3f0b9398ce3c7d6a4c6de8b";
    public static final String ROPSTEN = "0x0";
    public static final String RINKEBY = "0x0";

    public static String resolveRegistryContract(String chainId) {
        switch (Byte.valueOf(chainId)) {
            case ChainId.MAINNET:
                return MAINNET;
            case ChainId.ROPSTEN:
                return ROPSTEN;
            case ChainId.RINKEBY:
                return RINKEBY;
            default:
                throw new EnsResolutionException("Unable to resolve ENS registry contract for network id: " + chainId);
        }
    }
}
