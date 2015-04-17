package me.cytochro.green;

import me.cytochro.zson.T;

@FunctionalInterface
public interface Evalable {
    public Future eval(T expression, LexicalEnvironment lexenv);
}
