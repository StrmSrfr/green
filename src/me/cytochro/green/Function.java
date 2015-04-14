package me.cytochro.green;

import me.cytochro.zson.Nil;
import me.cytochro.zson.T;

@FunctionalInterface
public interface Function extends T, Nameable {
    public Future apply(Future[] arguments);
}
