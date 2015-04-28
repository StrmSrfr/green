package me.cytochro.green.builtin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;

import me.cytochro.zson.EOF;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.T;
import me.cytochro.zson.ZSON;

import me.cytochro.green.Future;
import me.cytochro.green.Green;

import me.cytochro.green.exception.FileNotFoundException;
import me.cytochro.green.exception.TypeException;

public class Load extends AbstractFunction {
    @Override
    public T apply(T[] arguments) {
        final T arg = arguments[0];
        if (!(arg instanceof Symbol)) {
            return new TypeException("symbol", arg);
        }

        final Symbol sym = (Symbol) arg;
        final String name = sym.getName();
        final ClassLoader cl = this.getClass().getClassLoader();
        try (InputStream is = cl.getResourceAsStream(name + runtime.SRC_EXTENSION)) {
            if (is == null) {
                return new FileNotFoundException(sym);
            }

            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader in = new BufferedReader(isr)) {
                Future result = () -> Nil.NIL;
                T input;
                while (!((input = ZSON.read(in)) instanceof EOF)) {
                    result = runtime.evalForFuture(input);
                }
                return result.get();
            }
        } catch (IOException ioe) {
            return new me.cytochro.green.exception.IOException(ioe);
        }
    }

    @Override
    public Symbol name() {
        return NAME;
    }

    public Load(Green runtime) {
        super(1, 1);
        this.runtime = runtime;
    }

    private static final Symbol NAME = Symbol.intern("load");
    private final Green runtime;
}
