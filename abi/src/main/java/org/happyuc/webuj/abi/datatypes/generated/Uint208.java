package org.happyuc.webuj.abi.datatypes.generated;

import java.math.BigInteger;

import org.happyuc.webuj.abi.datatypes.Uint;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.happyuc.webuj.codegen.AbiTypesGenerator in the
 * <a href="https://github.com/happyuc-project/webu.java/tree/master/codegen">codegen module</a> to update.
 */
public class Uint208 extends Uint {
    public static final Uint208 DEFAULT = new Uint208(BigInteger.ZERO);

    public Uint208(BigInteger value) {
        super(208, value);
    }

    public Uint208(long value) {
        this(BigInteger.valueOf(value));
    }
}
