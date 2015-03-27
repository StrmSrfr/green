package me.cytochro.green.exception;

import me.cytochro.green.Exception;
import me.cytochro.green.Function;

public class ArityException implements Exception {
    private final Function function;
    private final Integer minExpected;
    private final Integer maxExpected;
    private final int actual;

    public ArityException(Function function,
                          int numberOfArguments,
                          Integer minArgs,
                          Integer maxArgs) {
        this.function = function;
        this.actual = numberOfArguments;
        this.minExpected = minArgs;
        this.maxExpected = maxArgs;
    }

    public ArityException(Function function, int numberOfArguments) {
        this(function, numberOfArguments, null, null);
    }

    public Function getFunction() {
        return function;
    }
    
    public int getActualNumberOfArguments() {
        return actual;
    }

    // TODO more getters, toString
}
