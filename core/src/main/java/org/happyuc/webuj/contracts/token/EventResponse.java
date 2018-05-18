package org.happyuc.webuj.contracts.token;

import org.happyuc.webuj.protocol.core.methods.response.Log;
import org.happyuc.webuj.tx.Contract;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

public abstract class EventResponse {
    public static <T extends EventResponse> T make(Class<T> clazz) {
        try {
            return clazz.getConstructor(new Class[]{}).newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class TransferEr extends EventResponse {
        public Log log;

        public String _from;

        public String _to;

        public BigInteger _value;

        public String _data;
    }

    public static class ApprovalEr extends EventResponse {
        public Log log;

        public String _owner;

        public String _spender;

        public BigInteger _value;

        public String _data;
    }

    public interface Rec<T> {
        T doLog(Contract.EventValuesWithLog eventValues);
    }

}
