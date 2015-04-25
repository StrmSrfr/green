package me.cytochro.green;

import java.util.Arrays;

import me.cytochro.zson.T;

import me.cytochro.green.exception.ArityException;

public abstract class AbstractFunction implements BuiltInFunction {
    public abstract T apply(T[] arguments);

    public AbstractFunction(Integer minArgs, Integer maxArgs) {
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    @Override
    public Future apply(Future[] arguments) {
        return () -> {
            if (arguments.length < minArgs
                || maxArgs != null && arguments.length > maxArgs) {
                return new ArityException(this, arguments.length,
                                          minArgs, maxArgs);
            }

            T[] args =
                Arrays.stream(arguments)
                .map(Future::get)
                .toArray(T[]::new);

            for (T a : args) {
                if (a instanceof Exception) {
                    return a;
                }
            }

            return apply(args);
        };
    }

    final Integer minArgs;
    final Integer maxArgs;
}
