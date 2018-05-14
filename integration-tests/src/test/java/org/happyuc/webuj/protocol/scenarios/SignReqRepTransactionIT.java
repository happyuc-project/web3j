package org.happyuc.webuj.protocol.scenarios;

import java.math.BigInteger;

import org.happyuc.webuj.crypto.Hash;
import org.happyuc.webuj.utils.Convert;
import org.happyuc.webuj.utils.Numeric;
import org.junit.Test;

import org.happyuc.webuj.crypto.RawTransaction;
import org.happyuc.webuj.crypto.TransactionEncoder;
import org.happyuc.webuj.protocol.core.methods.response.HucSign;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Sign transaction using Happyuc node.
 */
public class SignReqRepTransactionIT extends Scenario {

    @Test
    public void testSignTransaction() throws Exception {
        boolean accountUnlocked = unlockAccount();
        assertTrue(accountUnlocked);

        RawTransaction rawTransaction = createTransaction();

        byte[] encoded = TransactionEncoder.encode(rawTransaction);
        byte[] hashed = Hash.sha3(encoded);

        HucSign hucSign = webuj.hucSign(ALICE.getAddress(), Numeric.toHexString(hashed)).sendAsync().get();

        String signature = hucSign.getSignature();
        assertNotNull(signature);
        assertFalse(signature.isEmpty());
    }

    private static RawTransaction createTransaction() {
        BigInteger value = Convert.toWei("1", Convert.Unit.ETHER).toBigInteger();

        return RawTransaction.createHucerTransaction(BigInteger.valueOf(1048587), BigInteger.valueOf(500000), BigInteger.valueOf(500000), "0x9C98E381Edc5Fe1Ac514935F3Cc3eDAA764cf004", value);
    }
}
