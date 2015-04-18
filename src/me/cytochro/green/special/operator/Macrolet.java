package me.cytochro.green.special.operator;

import java.io.IOException;
import java.util.Arrays;

import com.google.common.collect.ImmutableMap;

import me.cytochro.zson.Cons;
import me.cytochro.zson.List;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.T;

import me.cytochro.green.Exception;
import me.cytochro.green.Function;
import me.cytochro.green.Future;
import me.cytochro.green.Green;
import me.cytochro.green.LexicalEnvironment;
import me.cytochro.green.Macro;
import me.cytochro.green.SpecialOperator;

import me.cytochro.green.exception.ArityException;
import me.cytochro.green.exception.Unbound;

public class Macrolet implements T, SpecialOperator {
    @Override
    public Future eval(T expression, LexicalEnvironment lexenv) {
        assert (expression instanceof Cons);
        final Cons expr = (Cons) expression;
        final T macrolet = expr.getCar();
        assert (MACROLET.equals(macrolet));
        final T restO = expr.getCdr();
        assert (restO instanceof Cons);
        final Cons rest = (Cons) restO;
        final Cons[] macros = Arrays.stream(((Cons) (rest.getCar())).toArray())
            .map(t -> (Cons) t)
            .toArray(Cons[]::new);
        final List body = (List) rest.getCdr();
        final Symbol[] names =
            Arrays.stream(macros)
            .map(Cons::getCar)
            .toArray(Symbol[]::new);
        final Lambda.Guts[] guts =
            Arrays.stream(macros)
            .map(Cons::getCdr)
            .map(g -> Lambda.Guts.newInstance((Cons) g, lexenv, runtime))
            .toArray(Lambda.Guts[]::new);

        ImmutableMap.Builder<Symbol, Future> b = new ImmutableMap.Builder<>();

        assert (names.length == guts.length);
        for (int i = 0; i < names.length; i++) {
            b.put(names[i],
                  Future.of(new Macro(names[i], guts[i], runtime)));
        }

        LexicalEnvironment inner =
            new LexicalEnvironment(lexenv, b.build());

        assert (body.getCdr() instanceof Nil);
        return runtime.evalForFuture(body.getCar(), inner);
    }

    public Symbol name() {
        return MACROLET;
    }

    public Macrolet(Green runtime) {
        this.runtime = runtime;
    }
    
    private final Symbol MACROLET = Symbol.intern("macrolet");
    private final Green runtime;
}
