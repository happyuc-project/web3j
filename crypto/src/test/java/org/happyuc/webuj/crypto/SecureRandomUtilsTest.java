package org.happyuc.webuj.crypto;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.happyuc.webuj.crypto.SecureRandomUtils.isAndroidRuntime;
import static org.happyuc.webuj.crypto.SecureRandomUtils.secureRandom;

public class SecureRandomUtilsTest {

    @Test
    public void testSecureRandom() {
        SecureRandomUtils.secureRandom().nextInt();
    }

    @Test
    public void testIsNotAndroidRuntime() {
        assertFalse(SecureRandomUtils.isAndroidRuntime());
    }
}
