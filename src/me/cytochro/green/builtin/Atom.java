package me.cytochro.green.builtin;

import me.cytochro.zson.Cons;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;

import me.cytochro.green.Green;
import me.cytochro.green.BuiltInFunction;
import me.cytochro.green.Exception;
import me.cytochro.green.Future;

import me.cytochro.green.exception.ArityException;

public class Atom implements BuiltInFunction {
    @Override
    public Future apply(Future[] arguments) {
        return () -> {
            if (arguments.length != 1) {
                return new ArityException(this, arguments.length, 1, 1);
            }
            Objet arg = arguments[0].get();
            if (arg instanceof Cons) {
                return Nil.NIL;
            } else if (arg instanceof Exception) {
                return arg;
            } else {
                return Green.getT();
            }
        };
    }

    @Override
    public Symbol name() {
        return NAME;
    }

    private static final Symbol NAME = Symbol.intern("atom");
}
