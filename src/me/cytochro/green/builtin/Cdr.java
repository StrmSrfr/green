package me.cytochro.green.builtin;

import java.util.Arrays;

import me.cytochro.zson.Cons;
import me.cytochro.zson.List;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;

import me.cytochro.green.Green;
import me.cytochro.green.BuiltInFunction;
import me.cytochro.green.Future;
import me.cytochro.green.exception.ArityException;
import me.cytochro.green.exception.TypeException;

public class Cdr implements BuiltInFunction {
    @Override
    public Future apply(Future[] arguments) {
        return () -> {
            if (arguments.length != 1) {
                return new ArityException(this, arguments.length,
                                          1, 1);
            }
            
            Objet o = arguments[0].get();
            if (o instanceof Exception) {
                return o;
            } else if (!(o instanceof List)) {
                return new TypeException(Symbol.intern("list"), o); // TODO should be type?
            } else {
                return ((List) o).getCdr();
            }
        };
    }

    @Override
    public Symbol name() {
        return Symbol.intern("cdr");
    }
}
