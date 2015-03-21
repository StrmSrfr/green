package me.cytochro.green;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import me.cytochro.zson.EOF;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.ZSON;

public class Green {
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
        if (expression instanceof Nil) {
            return expression;
        } else if (expression instanceof Symbol) {
            return new Unbound((Symbol) expression);
        } else {
            throw new UnsupportedOperationException("TODO!!");
        }
    }
}
