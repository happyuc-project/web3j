package org.happyuc.webuj.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.happyuc.webuj.utils.Bytes.trimLeadingZeroes;

public class BytesTest {

    @Test
    public void testTrimLeadingZeroes() {
        assertThat(Bytes.trimLeadingZeroes(new byte[]{}), is(new byte[]{}));
        assertThat(Bytes.trimLeadingZeroes(new byte[]{0}), is(new byte[]{0}));
        assertThat(Bytes.trimLeadingZeroes(new byte[]{1}), is(new byte[]{1}));
        assertThat(Bytes.trimLeadingZeroes(new byte[]{0, 1}), is(new byte[]{1}));
        assertThat(Bytes.trimLeadingZeroes(new byte[]{0, 0, 1}), is(new byte[]{1}));
        assertThat(Bytes.trimLeadingZeroes(new byte[]{0, 0, 1, 0}), is(new byte[]{1, 0}));
    }
}
