package org.happyuc.webuj.protocol.parity.methods.response;

import org.happyuc.webuj.crypto.WalletFile;
import org.happyuc.webuj.protocol.core.Response;

/**
 * parity_ExportAccount.
 */
public class ParityExportAccount extends Response<WalletFile> {
    public WalletFile getWallet() {
        return getResult();
    }
}
