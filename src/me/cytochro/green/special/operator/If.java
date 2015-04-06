package me.cytochro.green.special.operator;

import me.cytochro.zson.Cons;
import me.cytochro.zson.List;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Symbol;

import me.cytochro.green.Exception;
import me.cytochro.green.Future;
import me.cytochro.green.Green;
import me.cytochro.green.SpecialOperator;

import me.cytochro.green.exception.ArityException;

public class If implements Objet, SpecialOperator {
    public Future eval(Objet expression) {
        final Cons expr = (Cons) expression;
        assert (IF.equals(expr.getCar()));
        final Objet[] body = ((List) expr.getCdr()).toArray();
        final int formCount = body.length;
        if (formCount < 2 || formCount > 3) {
            return () -> new ArityException(null, formCount, 2, 3);
        }
        return () -> {
            final Objet which = runtime.eval(body[0]);
            if (which instanceof Exception) {
                return which;
            } else if (which instanceof Nil) {
                if (formCount == 2) {
                    return Nil.NIL;
                } else {
                    return runtime.eval(body[2]);
                }
            } else {
                return runtime.eval(body[1]);
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
        
