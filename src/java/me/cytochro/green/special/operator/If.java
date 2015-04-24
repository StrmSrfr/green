package me.cytochro.green.special.operator;

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

public class If implements T, SpecialOperator {
    public Future eval(T expression, LexicalEnvironment lexenv) {
        final Cons expr = (Cons) expression;
        assert (IF.equals(expr.getCar()));
        final T[] body = ((List) expr.getCdr()).toArray();
        final int formCount = body.length;
        if (formCount < 2 || formCount > 3) {
            return () -> new ArityException(null, formCount, 2, 3);
        }
        return () -> {
            final T which = runtime.eval(body[0], lexenv);
            if (which instanceof Exception) {
                return which;
            } else if (which instanceof Nil) {
                if (formCount == 2) {
                    return Nil.NIL;
                } else {
                    return runtime.eval(body[2], lexenv);
                }
            } else {
                return runtime.eval(body[1], lexenv);
            }
        };
    }

    public Symbol name() {
        return IF;
    }

    public If(Green runtime) {
        this.runtime = runtime;
    }
    
    private final Symbol IF = Symbol.intern("if");
    private final Green runtime;
}
        
