package me.cytochro.green.special.operator;

import me.cytochro.zson.Cons;
import me.cytochro.zson.List;
import me.cytochro.zson.T;
import me.cytochro.zson.Symbol;

import me.cytochro.green.Future;
import me.cytochro.green.LexicalEnvironment;
import me.cytochro.green.SpecialOperator;

public class Quote implements T, SpecialOperator {
    public Future eval(T expression,
                       @SuppressWarnings("unused")
                       LexicalEnvironment lexenv) {
        Cons expr = (Cons) expression;
        assert (QUOTE.equals(expr.getCar()));
        List cdr = (List) expr.getCdr();
        return () -> cdr.getCar();
    }

    public Symbol name() {
        return QUOTE;
    }

    private final Symbol QUOTE = Symbol.intern("quote");
}
        
