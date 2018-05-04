package org.happyuc.webuj.ens;

import org.junit.Test;

import org.happyuc.webuj.protocol.webuj;
import org.happyuc.webuj.protocol.http.HttpService;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EnsIT {

    @Test
    public void testEns() throws Exception {

        webuj webuj = webuj.build(new HttpService());
        EnsResolver ensResolver = new EnsResolver(webuj);

        assertThat(ensResolver.resolve("webuj.test"),
                is("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
    }
}
