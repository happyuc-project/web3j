package org.happyuc.webuj.utils;

import java.math.BigDecimal;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class ConvertTest {

    @Test
    public void testFromWei() {
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.WEI), is(new BigDecimal("21000000000000")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.KWEI), is(new BigDecimal("21000000000")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.MWEI), is(new BigDecimal("21000000")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.GWEI), is(new BigDecimal("21000")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.TWEI), is(new BigDecimal("21")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.PWEI), is(new BigDecimal("0.021")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.HUC), is(new BigDecimal("0.000021")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.KHUC), is(new BigDecimal("0.000000021")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.MHUC), is(new BigDecimal("0.000000000021")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.GHUC), is(new BigDecimal("0.000000000000021")));
    }

    @Test
    public void testToWei() {
        assertThat(Convert.toWei("21", Convert.Unit.WEI), is(new BigDecimal("21")));
        assertThat(Convert.toWei("21", Convert.Unit.KWEI), is(new BigDecimal("21000")));
        assertThat(Convert.toWei("21", Convert.Unit.MWEI), is(new BigDecimal("21000000")));
        assertThat(Convert.toWei("21", Convert.Unit.GWEI), is(new BigDecimal("21000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.TWEI), is(new BigDecimal("21000000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.PWEI), is(new BigDecimal("21000000000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.HUC), is(new BigDecimal("21000000000000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.KHUC), is(new BigDecimal("21000000000000000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.MHUC), is(new BigDecimal("21000000000000000000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.GHUC), is(new BigDecimal("21000000000000000000000000000")));
    }

    @Test
    public void testUnit() {
        assertThat(Convert.Unit.fromString("huc"), is(Convert.Unit.HUC));
        assertThat(Convert.Unit.fromString("wei"), is(Convert.Unit.WEI));
    }
}
