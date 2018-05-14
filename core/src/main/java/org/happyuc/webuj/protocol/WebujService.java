package org.happyuc.webuj.protocol;

import java.io.IOException;
import java.util.concurrent.Future;

import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.Response;
import org.happyuc.webuj.protocol.core.Request;
import org.happyuc.webuj.protocol.core.Response;

/**
 * Services API.
 */
public interface WebujService {
    <T extends Response> T send(Request request, Class<T> responseType) throws IOException;

    <T extends Response> Future<T> sendAsync(Request request, Class<T> responseType);
}
