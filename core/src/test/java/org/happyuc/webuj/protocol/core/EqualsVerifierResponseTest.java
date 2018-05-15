package org.happyuc.webuj.protocol.core;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.happyuc.webuj.protocol.core.methods.response.RepTransactionReceipt;
import org.junit.Test;

import org.happyuc.webuj.protocol.core.methods.response.AbiDefinition;
import org.happyuc.webuj.protocol.core.methods.response.HucBlock;
import org.happyuc.webuj.protocol.core.methods.response.HucCompileSolidity;
import org.happyuc.webuj.protocol.core.methods.response.HucLog;
import org.happyuc.webuj.protocol.core.methods.response.HucSyncing;
import org.happyuc.webuj.protocol.core.methods.response.Log;
import org.happyuc.webuj.protocol.core.methods.response.ShhMessages;
import org.happyuc.webuj.protocol.core.methods.response.RepTransaction;

public class EqualsVerifierResponseTest {

    @Test
    public void testBlock() {
        EqualsVerifier.forClass(HucBlock.Block.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testTransaction() {
        EqualsVerifier.forClass(RepTransaction.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testTransactionReceipt() {
        EqualsVerifier.forClass(RepTransactionReceipt.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testLog() {
        EqualsVerifier.forClass(Log.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testSshMessage() {
        EqualsVerifier.forClass(ShhMessages.SshMessage.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testSolidityInfo() {
        EqualsVerifier.forClass(HucCompileSolidity.SolidityInfo.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testSyncing() {
        EqualsVerifier.forClass(HucSyncing.Syncing.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testAbiDefinition() {
        EqualsVerifier.forClass(AbiDefinition.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testAbiDefinitionNamedType() {
        EqualsVerifier.forClass(AbiDefinition.NamedType.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testHash() {
        EqualsVerifier.forClass(HucLog.Hash.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testCode() {
        EqualsVerifier.forClass(HucCompileSolidity.Code.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testTransactionHash() {
        EqualsVerifier.forClass(HucBlock.TransactionHash.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testCompiledSolidityCode() {
        EqualsVerifier.forClass(HucCompileSolidity.Code.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testDocumentation() {
        EqualsVerifier.forClass(HucCompileSolidity.Documentation.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @Test
    public void testError() {
        EqualsVerifier.forClass(Response.Error.class).suppress(Warning.NONFINAL_FIELDS).suppress(Warning.STRICT_INHERITANCE).verify();
    }
}
