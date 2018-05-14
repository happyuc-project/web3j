package org.happyuc.webuj.protocol;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.ResponseBody;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.Response;
import org.happyuc.webuj.protocol.http.HttpService;
import org.junit.Before;

import java.io.IOException;

import static org.happyuc.webuj.protocol.http.HttpService.JSON_MEDIA_TYPE;
import static org.junit.Assert.fail;

/**
 * Protocol Response tests.
 */
public abstract class ResponseTester {

    private HttpService webujService;
    private OkHttpClient okHttpClient;
    private ResponseInterceptor responseInterceptor;

    @Before
    public void setUp() {
        responseInterceptor = new ResponseInterceptor();
        okHttpClient = new OkHttpClient.Builder().addInterceptor(responseInterceptor).build();
        configureWeb3Service(false);
    }

    protected void buildResponse(String data) {
        responseInterceptor.setJsonResponse(data);
    }

    protected void configureWeb3Service(boolean includeRawResponses) {
        webujService = new HttpService(okHttpClient, includeRawResponses);
    }

    protected <T extends Response> T deserialiseResponse(Class<T> type) {
        T response = null;
        try {
            response = webujService.send(new Request(), type);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        return response;
    }

    private class ResponseInterceptor implements Interceptor {

        private String jsonResponse;

        public void setJsonResponse(String jsonResponse) {
            this.jsonResponse = jsonResponse;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            if (jsonResponse == null) {
                throw new UnsupportedOperationException("Response has not been configured");
            }

            okhttp3.Response response = new okhttp3.Response.Builder().body(ResponseBody.create(JSON_MEDIA_TYPE, jsonResponse)).request(chain.request()).protocol(Protocol.HTTP_2).code(200).message("").build();

            return response;
        }
    }
}
