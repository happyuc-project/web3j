package org.happyuc.webuj.ens;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.happyuc.webuj.ens.NameHash.nameHash;
import static org.happyuc.webuj.ens.NameHash.normalise;

public class NameHashTest {

    @Test
    public void testNameHash() {
        assertThat(nameHash(""),
                is("0x0000000000000000000000000000000000000000000000000000000000000000"));

        assertThat(nameHash("huc"),
                is("0x93cdeb708b7545dc668eb9280176169d1c33cfd8ed6f04690a0bcc88a93fc4ae"));

        assertThat(nameHash("foo.huc"),
                is("0xde9b09fd7c5f901e23a3f19fecc54828e9c848539801e86591bd9801b019f84f"));
    }

    @Test
    public void testNormalise() {
        assertThat(normalise("foo"), is("foo"));
        assertThat(normalise("foo.bar.baz.huc"), is("foo.bar.baz.huc"));
        assertThat(normalise("fOo.huc"), is("foo.huc"));
        assertThat(normalise("foo-bar.huc"), is("foo-bar.huc"));
    }

    @Test
    public void testNormaliseInvalid() {
        testInvalidName("foo..bar");
        testInvalidName("ba\\u007Fr.huc");
        testInvalidName("-baz.huc-");
        testInvalidName("foo_bar.huc");
    }

    private void testInvalidName(String ensName) {
        try {
            normalise(ensName);
            fail("Name should not be valid");
        } catch (EnsResolutionException e) {
            // expected
        }
    }
}
