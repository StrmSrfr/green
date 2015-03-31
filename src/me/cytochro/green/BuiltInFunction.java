package me.cytochro.green;

import java.util.Arrays;

import me.cytochro.zson.Objet;

import me.cytochro.green.exception.ArityException;

public abstract class BuiltInFunction implements Function {
    public abstract Objet apply(Objet[] arguments);

    public BuiltInFunction(Integer minArgs, Integer maxArgs) {
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
            
            Objet[] args =
                Arrays.stream(arguments)
                .map(Future::get)
                .toArray(Objet[]::new);

            for (Objet a : args) {
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
