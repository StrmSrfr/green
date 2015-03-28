package me.cytochro.green.builtin;

import java.util.Arrays;

import me.cytochro.zson.Cons;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;

import me.cytochro.green.Green;
import me.cytochro.green.BuiltInFunction;
import me.cytochro.green.Exception;
import me.cytochro.green.Future;

public class Eq implements BuiltInFunction {
    @Override
    public Future apply(Future[] arguments) {
        return () -> {
            Objet[] args =
                Arrays.stream(arguments)
                .map(Future::get)
                .toArray(Objet[]::new);

            for (Objet a : args) {
                if (a instanceof Exception) {
                    return a;
                }
            }
                    
            if (arguments.length < 2) {
                return Green.getT();
            }
            
            Objet o = args[0];
            for (Objet a : args) {
                if (a != o) {
                    return Nil.NIL;
                }
            }

            return Green.getT();
        };
    }

    @Override
    public Symbol name() {
        return Symbol.intern("eq");
    }
}
