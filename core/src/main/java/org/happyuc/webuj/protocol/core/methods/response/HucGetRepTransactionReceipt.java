package org.happyuc.webuj.protocol.core.methods.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;
import org.happyuc.webuj.protocol.ObjectMapperFactory;
import org.happyuc.webuj.protocol.core.Response;

import java.io.IOException;

/**
 * huc_getTransactionReceipt.
 */
public class HucGetRepTransactionReceipt extends Response<RepTransactionReceipt> {

    public RepTransactionReceipt getTransactionReceipt() {
        return getResult();
    }

    public static class ResponseDeserialiser extends JsonDeserializer<RepTransactionReceipt> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public RepTransactionReceipt deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, RepTransactionReceipt.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
}
