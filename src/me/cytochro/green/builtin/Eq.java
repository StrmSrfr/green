package me.cytochro.green.builtin;

import me.cytochro.zson.Cons;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;

import me.cytochro.green.Green;
import me.cytochro.green.BuiltInFunction;

public class Eq extends BuiltInFunction {
    @Override
    public Objet apply(Objet[] arguments) {
        if (0 == arguments.length) {
            return Green.getT();
        }

        Objet o = arguments[0];
        for (Objet a : arguments) {
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
