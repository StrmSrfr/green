package me.cytochro.green.builtin;

import java.util.Arrays;

import me.cytochro.zson.List;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.T;

import me.cytochro.green.BuiltInFunction;
import me.cytochro.green.Function;
import me.cytochro.green.Future;
import me.cytochro.green.exception.ArityException;
import me.cytochro.green.exception.TypeException;

public class Apply implements BuiltInFunction {
    @Override
    public Future apply(Future[] ourArguments) {
        final int cnt = ourArguments.length;
        if (cnt != 2) {
            return Future.of(new ArityException(this, cnt,
                                                2, 2));
        }
            
        final T arg1 = ourArguments[0].get();
        final T arg2 = ourArguments[1].get();
        final TypeException te = checkTypes(arg1, arg2);
        if (te != null) {
            return Future.of(te);
        }

        final Function f = (Function) arg1;
        final List args = (List) arg2;

        final Future[] a =
            Arrays.stream(args.toArray())
            .parallel()
            .map(Future::of)
            .toArray(Future[]::new);

        /* exception checking is deferred to the target */

        return f.apply(a);
    }

    protected TypeException checkTypes(T arg1, T arg2) {
        if (!(arg1 instanceof Function)) {
            return new TypeException(Symbol.intern("function"),
                                     Symbol.intern(arg1.getClass().getName()));
        }
        if (!(arg2 instanceof List)) {
            return new TypeException(Symbol.intern("list"),
                                     Symbol.intern(arg2.getClass().getName()));
        }
        return null;
    }

    @Override
    public Symbol name() {
        return NAME;
    }

    private static final Symbol NAME = Symbol.intern("apply");
}
