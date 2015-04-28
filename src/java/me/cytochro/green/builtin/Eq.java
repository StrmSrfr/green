package me.cytochro.green.builtin;

import me.cytochro.zson.Cons;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.T;

import me.cytochro.green.Green;

public class Eq extends AbstractFunction {
    @Override
    public T apply(T[] arguments) {
        if (0 == arguments.length) {
            return Green.getT();
        }

        T o = arguments[0];
        for (T a : arguments) {
            if (a != o) {
                return Nil.NIL;
            }
        }
        
        return Green.getT();
    }

    @Override
    public Symbol name() {
        return Symbol.intern("eq");
    }

    public Eq() {
        super(0, null);
    }
}
