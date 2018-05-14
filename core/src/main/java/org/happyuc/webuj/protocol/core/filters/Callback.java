package org.happyuc.webuj.protocol.core.filters;

/**
 * Filter callback interface.
 */
public interface Callback<T> {
    void onEvent(T value);
}
