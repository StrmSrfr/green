package me.cytochro.green.special.operator;

import java.util.Arrays;

import me.cytochro.zson.Cons;
import me.cytochro.zson.List;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.T;

import me.cytochro.green.Exception;
import me.cytochro.green.Future;
import me.cytochro.green.Green;
import me.cytochro.green.LexicalEnvironment;
import me.cytochro.green.SpecialOperator;

import me.cytochro.green.exception.ArityException;

public class Async implements T, SpecialOperator {
    public Future eval(T expression, LexicalEnvironment lexenv) {
        final Cons expr = (Cons) expression;
        assert (NAME.equals(expr.getCar()));
        final T[] body = ((List) expr.getCdr()).toArray();
        final int formCount = body.length;
        if (formCount == 0) {
            return Future.of(Nil.NIL); // TODO: should maybe be (values)?
        }

        return () -> {
            final T ex =
                Arrays.stream(body, 0, formCount - 1)
                .parallel()
                .map(e -> runtime.eval(e, lexenv))
                .reduce(null,
                        /* not actually associative, but I don't think it matters */
                        Green::propogateException);

            final T lastExpr = body[formCount - 1];
            final T lastResult = runtime.eval(lastExpr, lexenv);
            return Green.propogateException(ex, lastResult);
        };
    }

    public Symbol name() {
        return NAME;
    }

    public Async(Green runtime) {
        this.runtime = runtime;
    }

    private final Symbol NAME = Symbol.intern("async");
    private final Green runtime;
}
