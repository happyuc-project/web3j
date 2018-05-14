package org.happyuc.webuj.abi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.happyuc.webuj.abi.datatypes.*;
import org.happyuc.webuj.abi.datatypes.generated.Int64;
import org.happyuc.webuj.abi.datatypes.generated.Uint256;
import org.happyuc.webuj.abi.datatypes.generated.Uint64;
import org.junit.Test;

import org.happyuc.webuj.abi.datatypes.Bool;
import org.happyuc.webuj.abi.datatypes.DynamicArray;
import org.happyuc.webuj.abi.datatypes.DynamicBytes;
import org.happyuc.webuj.abi.datatypes.Fixed;
import org.happyuc.webuj.abi.datatypes.Int;
import org.happyuc.webuj.abi.datatypes.StaticArray;
import org.happyuc.webuj.abi.datatypes.Ufixed;
import org.happyuc.webuj.abi.datatypes.Uint;
import org.happyuc.webuj.abi.datatypes.Utf8String;
import org.happyuc.webuj.abi.datatypes.generated.Int64;
import org.happyuc.webuj.abi.datatypes.generated.Uint256;
import org.happyuc.webuj.abi.datatypes.generated.Uint64;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.happyuc.webuj.abi.Utils.typeMap;

public class UtilsTest {

    @Test
    public void testGetTypeName() throws ClassNotFoundException {
        assertThat(Utils.getTypeName(new TypeReference<Uint>() {}), is("uint256"));
        assertThat(Utils.getTypeName(new TypeReference<Int>() {}), is("int256"));
        assertThat(Utils.getTypeName(new TypeReference<Ufixed>() {}), is("ufixed256"));
        assertThat(Utils.getTypeName(new TypeReference<Fixed>() {}), is("fixed256"));

        assertThat(Utils.getTypeName(new TypeReference<Uint64>() {}), is("uint64"));
        assertThat(Utils.getTypeName(new TypeReference<Int64>() {}), is("int64"));
        assertThat(Utils.getTypeName(new TypeReference<Bool>() {}), is("bool"));
        assertThat(Utils.getTypeName(new TypeReference<Utf8String>() {}), is("string"));
        assertThat(Utils.getTypeName(new TypeReference<DynamicBytes>() {}), is("bytes"));

        assertThat(Utils.getTypeName(new TypeReference.StaticArrayTypeReference<StaticArray<Uint>>(5) {}), is("uint256[5]"));
        assertThat(Utils.getTypeName(new TypeReference<DynamicArray<Uint>>() {}), is("uint256[]"));
    }

    @Test
    public void testTypeMap() throws Exception {
        final List<BigInteger> input = Arrays.asList(BigInteger.ZERO, BigInteger.ONE, BigInteger.TEN);

        MatcherAssert.assertThat(Utils.typeMap(input, Uint256.class), equalTo(Arrays.asList(new Uint256(BigInteger.ZERO), new Uint256(BigInteger.ONE), new Uint256(BigInteger.TEN))));
    }

    @Test
    public void testTypeMapEmpty() {
        MatcherAssert.assertThat(Utils.typeMap(new ArrayList<BigInteger>(), Uint256.class), CoreMatchers.<List<Uint256>>equalTo(new ArrayList<Uint256>()));
    }
}
