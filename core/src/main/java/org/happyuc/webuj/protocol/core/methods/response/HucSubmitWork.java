package org.happyuc.webuj.protocol.core.methods.response;

import org.happyuc.webuj.protocol.core.Response;

/**
 * huc_submitWork.
 */
public class HucSubmitWork extends Response<Boolean> {

    public boolean solutionValid() {
        return getResult();
    }
}
