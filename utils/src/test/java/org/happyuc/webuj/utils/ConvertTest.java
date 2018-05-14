package org.happyuc.webuj.utils;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class ConvertTest {

    @Test
    public void testFromWei() {
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.WEI), is(new BigDecimal("21000000000000")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.KWEI), is(new BigDecimal("21000000000")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.MWEI), is(new BigDecimal("21000000")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.GWEI), is(new BigDecimal("21000")));
        assertThat(Convert.fromWei("21000000000000", Convert.Unit.HUC), is(new BigDecimal("0.000021")));
    }

    @Test
    public void testToWei() {
        assertThat(Convert.toWei("21", Convert.Unit.WEI), is(new BigDecimal("21")));
        assertThat(Convert.toWei("21", Convert.Unit.KWEI), is(new BigDecimal("21000")));
        assertThat(Convert.toWei("21", Convert.Unit.MWEI), is(new BigDecimal("21000000")));
        assertThat(Convert.toWei("21", Convert.Unit.GWEI), is(new BigDecimal("21000000000")));
        assertThat(Convert.toWei("21", Convert.Unit.HUC), is(new BigDecimal("21000000000000000000")));
    }

    @Test
    public void testUnit() {
        assertThat(Convert.Unit.fromString("huc"), is(Convert.Unit.HUC));
        assertThat(Convert.Unit.fromString("huc"), is(Convert.Unit.HUC));
        assertThat(Convert.Unit.fromString("wei"), is(Convert.Unit.WEI));
    }
}
