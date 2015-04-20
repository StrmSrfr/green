package me.cytochro.green;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import java.util.Arrays;

import me.cytochro.zson.Cons;
import me.cytochro.zson.Integer;
import me.cytochro.zson.List;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.ZSON;
import me.cytochro.zson.T;

import me.cytochro.green.exception.Unbound;
import me.cytochro.green.special.operator.Repl;

public class Green {
    protected static final Symbol T = Symbol.intern("t");
    protected final LexicalEnvironment defaultLexicalEnvironment =
        new DefaultLexicalEnvironment(this);

    public final Reader in;
    public final Writer out;

    public Green() {
        InputStreamReader isr = new InputStreamReader(System.in);
        in = new BufferedReader(isr);
        out = new PrintWriter(System.out);
    }

    public static void main(String [] args) throws IOException {
        Green me = new Green();
        me.eval("(repl)");
    }

    public T eval(String expressionAsString) throws IOException {
        return eval(ZSON.read(expressionAsString));
    }

    public T eval(T expression) {
        return evalForFuture(expression, defaultLexicalEnvironment).get();
    }

    public static Symbol getT() { // TODO: static?
        return T;
    }

    public T eval(T expression, LexicalEnvironment lexenv) {
        return evalForFuture(expression, lexenv).get();
    }

    public Future evalForFuture(T expression, LexicalEnvironment lexenv) {
        if (expression instanceof Nil
            || expression instanceof Integer) {
            return () -> expression;
        } else if (expression instanceof Symbol) {
            Symbol symbol = (Symbol) expression;
            final Future val = lexenv.get(symbol);
            if (val == null) {
                return () -> new Unbound(symbol);
            } else {
                return val;
            }
        } else if (expression instanceof Cons) {
            return evalCons((Cons) expression, lexenv);
        } else {
            throw new UnsupportedOperationException("Don't know how to eval a "
                                                    + expression +
                                                    " yet.");
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
        T first = eval(expression.getCar(), lexenv);
        if (first instanceof Unbound) {
            return () -> first;
        } else if (first instanceof Nil) {
            throw new UnsupportedOperationException("() is not meaningful in function position... at least not yet");
        } else if (first instanceof Evalable) {
            Evalable op = (Evalable) first;
            return op.eval(expression, lexenv);
        } else if (first instanceof Function) {
            final Function f = (Function) first;
            final T[] argForms = ((List) expression.getCdr()).toArray();
            final Future[] arguments =
                Arrays.stream(argForms)
                .map((form) -> evalForFuture(form, lexenv))
                .toArray(Future[]::new);
            return f.apply(arguments);
        } else {
            throw new UnsupportedOperationException("Value in functional position was not of an expected type: " + first);
        }
    }
}
