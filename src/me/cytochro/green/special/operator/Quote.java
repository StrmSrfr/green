package me.cytochro.green.special.operator;

import me.cytochro.zson.Cons;
import me.cytochro.zson.List;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Symbol;

import me.cytochro.green.Future;
import me.cytochro.green.SpecialOperator;

public class Quote implements Objet, SpecialOperator {
    public Future eval(Objet expression) {
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
        
