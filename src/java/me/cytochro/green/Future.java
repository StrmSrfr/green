package me.cytochro.green;

import java.util.Collections;
import java.util.Set;

import me.cytochro.zson.T;

public interface Future extends java.util.function.Supplier<T> {
    public default Set<Future> getDependencies() {
        return Collections.EMPTY_SET;
    }

    public static Future of(T o) {
        return () -> o;
    }
}
