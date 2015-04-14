package me.cytochro.green.special.operator;

import java.io.IOException;

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
import me.cytochro.green.SpecialOperator;

import me.cytochro.green.exception.ArityException;
import me.cytochro.green.exception.Unbound;

public class Lambda implements T, SpecialOperator {
    @Override
    public Future eval(T expression, LexicalEnvironment lexenv) {
        return () -> {
            assert (expression instanceof Cons);
            final Cons expr = (Cons) expression;
            final T lambda = expr.getCar();
            assert (LAMBDA.equals(lambda));
            final T restO = expr.getCdr();
            assert (restO instanceof Cons);
            final Cons rest = (Cons) restO;
            final T parmsO = rest.getCar();
            assert (parmsO instanceof List);
            final List parmsList = (List) parmsO;
            final T[] parameters = parmsList.toArray();
            final T bodyO = rest.getCdr();
            assert (bodyO instanceof List);
            final List body = (List) bodyO;
            return new Function() {
                public Future apply(Future [] arguments) {
                    if (arguments.length != parameters.length) {
                        return () ->
                            new ArityException(this,
                                               arguments.length,
                                               parameters.length,
                                               parameters.length);
                    }

                    final ImmutableMap.Builder<Symbol, Future> b =
                        new ImmutableMap.Builder<>();
                    for (int i = 0; i < arguments.length; i++) {
                        Symbol name = (Symbol) parameters[i]; // TODO check type
                        Future value = arguments[i];
                        if (value.get() instanceof Exception) {
                            return value;
                        }
                        b.put((Symbol) name, arguments[i]);
                    }

                    final LexicalEnvironment inner =
                        new LexicalEnvironment(lexenv, b.build());
                    
                    T expression = body.getCar();
                    return runtime.evalForFuture(expression, inner);
                }
            };
        };
    }

    public Symbol name() {
        return LAMBDA;
    }

    public Lambda(Green runtime) {
        this.runtime = runtime;
    }
    
    private final Symbol LAMBDA = Symbol.intern("lambda");
    private final Green runtime;
}
        
