package org.happyuc.webuj.utils;

import org.junit.Test;

import static org.web3j.utils.Assertions.verifyPrecondition;

public class AssertionsTest {

    @Test
    public void testVerifyPrecondition() {
        Assertions.verifyPrecondition(true, "");
    }

    @Test(expected = RuntimeException.class)
    public void testVerifyPreconditionFailure() {
        Assertions.verifyPrecondition(false, "");
    }
}
