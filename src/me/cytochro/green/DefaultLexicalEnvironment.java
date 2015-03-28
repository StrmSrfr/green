package me.cytochro.green;

import com.google.common.collect.ImmutableMap;

import me.cytochro.zson.Symbol;

import me.cytochro.green.builtin.Atom;
import me.cytochro.green.builtin.Eq;
import me.cytochro.green.builtin.Car;

import me.cytochro.green.special.operator.Quote;

public class DefaultLexicalEnvironment extends LexicalEnvironment {
    public DefaultLexicalEnvironment() {
        super(null,
              ImmutableMap.of(Green.getT(), () -> Green.getT(),
                              QUOTE.name(), () -> QUOTE,
                              ATOM.name(), () -> ATOM,
                              CAR.name(), () -> CAR,
                              EQ.name(), () -> EQ));
    }

    private static final Quote QUOTE = new Quote();
    private static final Atom ATOM = new Atom();
    private static final Car CAR = new Car();
    private static final Eq EQ = new Eq();
}
