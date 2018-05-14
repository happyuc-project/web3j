package org.happyuc.webuj.protocol.core.methods.response;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectReader;

import org.happyuc.webuj.protocol.ObjectMapperFactory;
import org.happyuc.webuj.protocol.core.Response;

/**
 * ReqTransaction object returned by:
 * <ul>
 * <li>huc_getTransactionByHash</li>
 * <li>huc_getTransactionByBlockHashAndIndex</li>
 * <li>huc_getTransactionByBlockNumberAndIndex</li>
 * </ul>
 *
 * <p>This differs slightly from the request {@link HucSendRepTransaction} ReqTransaction object.</p>
 *
 * <p>See
 * <a href="https://github.com/ethereum/wiki/wiki/JSON-RPC#huc_gettransactionbyhash">docs</a>
 * for further details.</p>
 */
public class HucRepTransaction extends Response<RepTransaction> {

    public RepTransaction getTransaction() {
        return getResult();
    }

    public static class ResponseDeserialiser extends JsonDeserializer<RepTransaction> {

        private ObjectReader objectReader = ObjectMapperFactory.getObjectReader();

        @Override
        public RepTransaction deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            if (jsonParser.getCurrentToken() != JsonToken.VALUE_NULL) {
                return objectReader.readValue(jsonParser, RepTransaction.class);
            } else {
                return null;  // null is wrapped by Optional in above getter
            }
        }
    }
}
