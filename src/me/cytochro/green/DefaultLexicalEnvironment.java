package me.cytochro.green;

import com.google.common.collect.ImmutableMap;

import me.cytochro.zson.Symbol;

import me.cytochro.green.builtin.Atom;
import me.cytochro.green.builtin.Eq;
import me.cytochro.green.builtin.Eval;
import me.cytochro.green.builtin.Car;
import me.cytochro.green.builtin.Cdr;
import me.cytochro.green.builtin.Cons;

import me.cytochro.green.special.operator.If;
import me.cytochro.green.special.operator.Lambda;
import me.cytochro.green.special.operator.Quote;

public class DefaultLexicalEnvironment extends LexicalEnvironment {
    public DefaultLexicalEnvironment(Green runtime) {
        super(entry(runtime.getT(), () -> runtime.getT()),
              entry(new If    (runtime)),
              entry(new Lambda(runtime)),
              entry(new Quote ()),
              entry(new Atom  ()),
              entry(new Car   ()),
              entry(new Cdr   ()),
              entry(new Eq    ()),
              entry(new Eval  (runtime)),
              entry(new Cons  ())
              );

        this.runtime = runtime;
    }

    private final Green runtime;
}
