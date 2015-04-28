package me.cytochro.green.builtin;

import me.cytochro.zson.List;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.T;

import me.cytochro.green.Green;

public class Cons extends AbstractFunction {
    @Override
    public synchronized me.cytochro.zson.Cons apply(T[] arguments) {
        final T car = arguments[0];
        final T cdr = arguments[1];
        return me.cytochro.zson.Cons.intern(car, cdr);
    }

    @Override
    public Symbol name() {
        return Symbol.intern("cons");
    }

    public Cons() {
        super(2, 2);
    }
}
