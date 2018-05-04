package org.happyuc.webuj.abi.datatypes.generated;

import java.math.BigInteger;
import org.happyuc.webuj.abi.datatypes.Uint;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.happyuc.webuj.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/happyuc-project/webu.java/tree/master/codegen">codegen module</a> to update.
 */
public class Uint96 extends Uint {
    public static final Uint96 DEFAULT = new Uint96(BigInteger.ZERO);

    public Uint96(BigInteger value) {
        super(96, value);
    }

    public Uint96(long value) {
        this(BigInteger.valueOf(value));
    }
}
