package me.cytochro.green.special.operator;

import java.io.IOError;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.google.common.collect.ImmutableMap;

import me.cytochro.zson.Cons;
import me.cytochro.zson.EOF;
import me.cytochro.zson.List;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.ZSON;

import me.cytochro.green.Exception;
import me.cytochro.green.Function;
import me.cytochro.green.Future;
import me.cytochro.green.Green;
import me.cytochro.green.LexicalEnvironment;
import me.cytochro.green.SpecialOperator;

import me.cytochro.green.exception.ArityException;
import me.cytochro.green.exception.Unbound;

public class Repl implements Objet, SpecialOperator {
    @Override
    public Future eval(Objet expression, LexicalEnvironment lexenv) {
        return () -> {
            try {
                assert (expression instanceof Nil);
                final Reader in = runtime.in;
                final Writer out = runtime.out;
                out.write("> ");
                out.flush();
                Objet star = new EOF();
                Objet input;
                while (!((input = ZSON.read(in)) instanceof EOF)) {
                    star = runtime.eval(input);
                    ZSON.write(star, out);
                    out.write("\n> ");
                    out.flush();
                }
                return star;
            } catch (IOException ioe) {
                throw new IOError(ioe);
            }
        };
    }

    public Symbol name() {
        return REPL;
    }

    public Repl(Green runtime) {
        this.runtime = runtime;
    }
    
    private final Symbol REPL = Symbol.intern("repl");
    private final Green runtime;
}
        
