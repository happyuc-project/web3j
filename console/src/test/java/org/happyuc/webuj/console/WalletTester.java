package org.happyuc.webuj.console;

import org.happyuc.webuj.TempFileProvider;

import org.junit.Before;

import static org.happyuc.webuj.crypto.SampleKeys.PASSWORD;
import static org.mockito.Mockito.mock;

public abstract class WalletTester extends TempFileProvider {

    static final char[] WALLET_PASSWORD = PASSWORD.toCharArray();

    IODevice console;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        console = mock(IODevice.class);
    }
}
