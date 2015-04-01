package me.cytochro.green;

import com.google.common.collect.ImmutableMap;

import me.cytochro.zson.Symbol;

import me.cytochro.green.builtin.Atom;
import me.cytochro.green.builtin.Eq;
import me.cytochro.green.builtin.Car;
import me.cytochro.green.builtin.Cdr;
import me.cytochro.green.builtin.Cons;

import me.cytochro.green.special.operator.Quote;

public class DefaultLexicalEnvironment extends LexicalEnvironment {
    public DefaultLexicalEnvironment() {
        super(entry(Green.getT(), () -> Green.getT()),
              entry(QUOTE.name(), () -> QUOTE),
              entry(ATOM .name(), () -> ATOM ),
              entry(CAR  .name(), () -> CAR  ),
              entry(CDR  .name(), () -> CDR  ),
              entry(EQ   .name(), () -> EQ   ),
              entry(CONS .name(), () -> CONS )
              );
    }

    private static final Quote QUOTE = new Quote();
    private static final Atom  ATOM  = new Atom();
    private static final Car   CAR   = new Car();
    private static final Cdr   CDR   = new Cdr();
    private static final Eq    EQ    = new Eq();
    private static final Cons  CONS  = new Cons();
}
