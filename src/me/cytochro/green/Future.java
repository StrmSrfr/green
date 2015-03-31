package me.cytochro.green;

import java.util.Collections;
import java.util.Set;

import me.cytochro.zson.Objet;

public interface Future extends java.util.function.Supplier<Objet> {
    public default Set<Future> getDependencies() {
        return Collections.EMPTY_SET;
    }

    public static Future of(Objet o) {
        return () -> o;
    }
}
