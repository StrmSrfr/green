package me.cytochro.green.builtin;

import me.cytochro.zson.Cons;
import me.cytochro.zson.List;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;

import me.cytochro.green.Green;
import me.cytochro.green.BuiltInFunction;
import me.cytochro.green.Future;
import me.cytochro.green.exception.TypeException;

public class Cdr extends BuiltInFunction {
    @Override
    public Objet apply(Objet[] arguments) {
        Objet o = arguments[0];
        if (!(o instanceof List)) {
            return new TypeException(Symbol.intern("list"), o); // TODO should be type?
        } else {
            return ((List) o).getCdr();
        }
    }

    @Override
    public Symbol name() {
        return Symbol.intern("cdr");
    }

    public Cdr() {
        super(1, 1);
    }
}
