package me.cytochro.green;

import me.cytochro.zson.Nil;
import me.cytochro.zson.T;

@FunctionalInterface
public interface SpecialOperator extends Nameable {
    public Future eval(T expression, LexicalEnvironment lexenv);
}
