package me.cytochro.green;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import java.util.Arrays;

import com.google.common.collect.ImmutableMap;

import me.cytochro.zson.Cons;
import me.cytochro.zson.EOF;
import me.cytochro.zson.List;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.ZSON;

import me.cytochro.green.builtin.Atom;
import me.cytochro.green.exception.Unbound;
import me.cytochro.green.special.operator.Quote;

public class Green {
    protected static final Symbol T = Symbol.intern("t");
    protected final LexicalEnvironment defaultLexicalEnvironment =
        new LexicalEnvironment(null,
                               ImmutableMap.of(T, () -> T,
                                               Quote.QUOTE, () -> new Quote(),
                                               Symbol.intern("atom"), () -> new Atom()));

    public static void main(String [] args) throws IOException {
        Green me = new Green();
        try (InputStreamReader isr = new InputStreamReader(System.in);
             BufferedReader in = new BufferedReader(isr);
             PrintWriter out = new PrintWriter(System.out)) {
            for (Objet o = ZSON.read(in); !(o instanceof EOF); o = ZSON.read(in)) {
                ZSON.write(me.eval(o), out);
                out.flush();
            }
        }
    }

    public Objet eval(String expressionAsString) throws IOException {
        return eval(ZSON.read(expressionAsString));
    }

    public Objet eval(Objet expression) {
        return evalForFuture(expression, defaultLexicalEnvironment).get();
    }

    public static Symbol getT() { // TODO: static?
        return T;
    }

    public Objet eval(Objet expression, LexicalEnvironment lexenv) {
        return evalForFuture(expression, lexenv).get();
    }

    public Future evalForFuture(Objet expression, LexicalEnvironment lexenv) {
        if (expression instanceof Nil) {
            return () -> expression;
        } else if (expression instanceof Symbol) {
            Symbol symbol = (Symbol) expression;
            final Future val = lexenv.get(symbol);
            if (val == null) {
                return new Unbound(symbol);
            } else {
                return val;
            }
        } else if (expression instanceof Cons) {
            return evalCons((Cons) expression, lexenv);
        } else {
            throw new UnsupportedOperationException("TODO!!");
        }
    }

    public Future evalCons(Cons expression, LexicalEnvironment lexenv) {
        /* CL doesn't define order of function designator evaluation
           relative to the arguments.
           TODO: do I want to define it?
        */
        /* TODO consider if we want to define special operators in the
           evaluator (and check for them now) or just have them bound
           to a special value.  Will the second option (which I'm
           taking for now) even work?  How will this interact with
           macros and lexical scope?
        */
        Objet first = eval(expression.getCar(), lexenv);
        if (first instanceof Unbound) {
            return () -> first;
        } else if (first instanceof Nil) {
            throw new UnsupportedOperationException("() is not meaningful in function position... at least not yet");
        } else if (first instanceof SpecialOperator) {
            SpecialOperator op = (SpecialOperator) first;
            return op.eval(expression);
        } else if (first instanceof Function) {
            Function f = (Function) first;
            Objet[] argForms = ((List) expression.getCdr()).toArray();
            Future[] arguments =
                Arrays.stream(argForms)
                .map((form) -> evalForFuture(form, lexenv))
                .toArray(Future[]::new);
            return f.apply(arguments);
        } else {
            throw new UnsupportedOperationException("Value in functional position was not of an expected type: " + first);
        }
    }
}
