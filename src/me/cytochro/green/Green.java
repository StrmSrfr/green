package me.cytochro.green;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import me.cytochro.zson.Cons;
import me.cytochro.zson.EOF;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.ZSON;

public class Green {
    protected final Symbol t = Symbol.intern("t");
    protected final LexicalEnvironment defaultLexicalEnvironment =
        new LexicalEnvironment(t, () -> t);

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
        return eval(expression, defaultLexicalEnvironment);
    }

    public Symbol getT() {
        return t;
    }

    public Objet eval(Objet expression, LexicalEnvironment lexenv) {
        if (expression instanceof Nil) {
            return expression;
        } else if (expression instanceof Symbol) {
            final Future val = lexenv.get((Symbol) expression);
            if (val == null) {
                return new Unbound((Symbol) expression);
            } else {
                return val.get();
            }
        } else if (expression instanceof Cons) {
            return evalCons((Cons) expression, lexenv);
        } else {
            throw new UnsupportedOperationException("TODO!!");
        }
    }

    public Objet evalCons(Cons expression, LexicalEnvironment lexenv) {
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
            return first;
        } else if (first instanceof Nil) {
            throw new UnsupportedOperationException("() is not meaningful in function position... at least not yet");
        } // TODO functions
        else {
            throw new UnsupportedOperationException("Value in functional position was not of an expected type: " + first);
        }
    }
}
