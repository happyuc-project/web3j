package org.happyuc.webuj.protocol.core;

import java.math.BigInteger;

import org.happyuc.webuj.protocol.core.methods.request.Transaction;

/**
 * Common values used by integration tests.
 */
public interface IntegrationTestConfig {

    String validBlockHash();

    BigInteger validBlock();

    BigInteger validBlockTransactionCount();

    BigInteger validBlockUncleCount();

    String validAccount();

    String validContractAddress();

    String validContractAddressPositionZero();

    String validContractCode();

    Transaction buildTransaction();

    String validTransactionHash();

    String validUncleBlockHash();

    BigInteger validUncleBlock();

    String encodedEvent();
}
