package org.happyuc.webuj.abi.datatypes.generated;

import java.math.BigInteger;
import org.happyuc.webuj.abi.datatypes.Int;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.happyuc.webuj.codegen.AbiTypesGenerator in the 
 * <a href="https://github.com/happyuc-project/webu.java/tree/master/codegen">codegen module</a> to update.
 */
public class Int72 extends Int {
    public static final Int72 DEFAULT = new Int72(BigInteger.ZERO);

    public Int72(BigInteger value) {
        super(72, value);
    }

    public Int72(long value) {
        this(BigInteger.valueOf(value));
    }
}
