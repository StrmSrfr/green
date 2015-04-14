package me.cytochro.green.builtin;

import me.cytochro.zson.Cons;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;

import me.cytochro.green.Green;
import me.cytochro.green.BuiltInFunction;

public class Eval extends BuiltInFunction {
    @Override
    public Objet apply(Objet[] arguments) {
        return runtime.eval(arguments[0]);
    }

    @Override
    public Symbol name() {
        return Symbol.intern("eval");
    }

    public Eval(Green green) {
        super(1, 1);
        this.runtime = green;
    }

    private final Green runtime;
}
