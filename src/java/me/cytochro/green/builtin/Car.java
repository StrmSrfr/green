package me.cytochro.green.builtin;

import me.cytochro.zson.Cons;
import me.cytochro.zson.List;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.T;

import me.cytochro.green.Green;
import me.cytochro.green.Future;
import me.cytochro.green.exception.TypeException;

public class Car extends AbstractFunction {
    @Override
    public T apply(T[] arguments) {
        T o = arguments[0];
        if (!(o instanceof List)) {
            return new TypeException("list", o);
        } else {
            return ((List) o).getCar();
        }
    }

    @Override
    public Symbol name() {
        return Symbol.intern("car");
    }

    public Car() {
        super(1, 1);
    }
}
