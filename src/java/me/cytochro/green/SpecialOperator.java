package me.cytochro.green;

import me.cytochro.zson.T;

@FunctionalInterface
public interface SpecialOperator extends Evalable, Nameable {
    public Future eval(T expression, LexicalEnvironment lexenv);
}
