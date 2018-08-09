package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * irc_compileLLL.
 */
public class HucCompileLLL extends Response<String> {
    public String getCompiledSourceCode() {
        return getResult();
    }
}
