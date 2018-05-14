package org.happyuc.webuj.protocol.ghuc;

import org.happyuc.webuj.protocol.ghuc.response.PersonalEcRecover;
import org.happyuc.webuj.protocol.ghuc.response.PersonalImportRawKey;
import org.junit.Test;

import org.happyuc.webuj.protocol.ResponseTester;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ResponseTest extends ResponseTester {

    @Test
    public void testPersonalEcRecover() {
        buildResponse("{\n" + "    \"jsonrpc\": \"2.0\",\n" + "    \"id\": 1,\n" + "    \"result\": \"0xadfc0262bbed8c1f4bd24a4a763ac616803a8c54\"\n" + "}");

        PersonalEcRecover personalEcRecover = deserialiseResponse(PersonalEcRecover.class);
        assertThat(personalEcRecover.getRecoverAccountId(), is("0xadfc0262bbed8c1f4bd24a4a763ac616803a8c54"));
    }

    @Test
    public void testPersonalImportRawKey() {
        buildResponse("{\n" + "    \"jsonrpc\": \"2.0\",\n" + "    \"id\": 1,\n" + "    \"result\": \"0xadfc0262bbed8c1f4bd24a4a763ac616803a8c54\"\n" + "}");

        PersonalImportRawKey personalImportRawKey = deserialiseResponse(PersonalImportRawKey.class);
        assertThat(personalImportRawKey.getAccountId(), is("0xadfc0262bbed8c1f4bd24a4a763ac616803a8c54"));
    }

}
