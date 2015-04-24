package me.cytochro.green.builtin;

import me.cytochro.zson.Cons;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.T;

import me.cytochro.green.Green;
import me.cytochro.green.BuiltInFunction;

public class Atom extends BuiltInFunction {
    @Override
    public T apply(T[] arguments) {
        T arg = arguments[0];
        if (arg instanceof Cons) {
            return Nil.NIL;
        } else {
            return Green.getT();
        }
    }

    @Override
    public Symbol name() {
        return NAME;
    }

    public Atom() {
        super(1, 1);
    }

    private static final Symbol NAME = Symbol.intern("atom");
}
