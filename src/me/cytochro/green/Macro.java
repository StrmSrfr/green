package me.cytochro.green;

import java.util.Arrays;

import me.cytochro.zson.List;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.T;

public class Macro implements Evalable, Nameable {
    public Macro(Symbol name, Function expander, Green runtime) {
        this.name = name;
        this.expander = expander;
        this.runtime = runtime;
    }

    public Future eval(T expression, LexicalEnvironment lexenv) {
        return runtime.evalForFuture(expand(expression, lexenv), lexenv);
    }

    public T expand(T expression, LexicalEnvironment lexenv) {
        final List expr = (List) (((List) expression).getCdr());
        final T[] exp = expr.toArray();
        final Future expanded =
            expander.apply(Arrays.stream(exp)
                           .map(Future::of)
                           .toArray(Future[]::new));
        final T v = expanded.get();
        return v;
    }
    
    public Symbol getName() {
        return name;
    }

    public Function getExpander() {
        return expander;
    }

    private final Symbol name;
    private final Function expander;
    private final Green runtime;
}
    
