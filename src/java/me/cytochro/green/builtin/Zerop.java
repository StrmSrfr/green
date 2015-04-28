package me.cytochro.green.builtin;

import java.math.BigInteger;

import me.cytochro.green.builtin.AbstractFunction;
import me.cytochro.green.exception.TypeException;

import me.cytochro.zson.Integer;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.T;

public class Zerop extends AbstractFunction {
    public Zerop() {
        super(1, 1);
    }

    @Override
    public T apply(T[] args) {
        final T arg1 = args[0];

        if (!(arg1 instanceof Integer)) {
            return new TypeException("number", arg1);
        }

        final Integer arg = (Integer) args[0];
        return BigInteger.ZERO.equals(arg.getValue()) ? arg : Nil.NIL;
    }

    @Override
    public Symbol name() {
        return NAME;
    }

    private static final Symbol NAME = Symbol.intern("zerop");
}
