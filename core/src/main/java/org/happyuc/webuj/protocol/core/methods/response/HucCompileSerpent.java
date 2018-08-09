package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * irc_compileSerpent.
 */
public class HucCompileSerpent extends Response<String> {
    public String getCompiledSourceCode() {
        return getResult();
    }
}
