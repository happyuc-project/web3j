package org.happyuc.webuj.abi.datatypes.generated;

import java.math.BigInteger;
import org.happyuc.webuj.abi.datatypes.Uint;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.happyuc.webuj.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/happyuc-project/webu.java/tree/master/codegen">codegen module</a> to update.
 */
public class Uint160 extends Uint {
    public static final Uint160 DEFAULT = new Uint160(BigInteger.ZERO);

    public Uint160(BigInteger value) {
        super(160, value);
    }

    public Uint160(long value) {
        this(BigInteger.valueOf(value));
    }
}
