package org.happyuc.webuj.abi.datatypes.generated;

import java.math.BigInteger;

import org.happyuc.webuj.abi.datatypes.Int;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.happyuc.webuj.codegen.AbiTypesGenerator in the
 * <a href="https://github.com/happyuc-project/webu.java/tree/master/codegen">codegen module</a> to update.
 */
public class Int160 extends Int {
    public static final Int160 DEFAULT = new Int160(BigInteger.ZERO);

    public Int160(BigInteger value) {
        super(160, value);
    }

    public Int160(long value) {
        this(BigInteger.valueOf(value));
    }
}
