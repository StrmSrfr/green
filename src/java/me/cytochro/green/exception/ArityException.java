package me.cytochro.green.exception;

import java.io.IOError;
import java.io.IOException;

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

    public int getMinExpected() {
        return minExpected;
    }

    public int getMaxExpected() {
        return maxExpected;
    }

    @Override
    public String toString() {
        try {
            return toString(new StringBuffer()).toString();
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }

    public Appendable toString(Appendable buf) throws IOException {
        return buf
            .append("#<")
            .append(super.toString())
            .append("<")
            .append(String.valueOf(getFunction()))
            .append(" expected ")
            .append(String.valueOf(getMinExpected()))
            .append(" to ")
            .append(String.valueOf(getMaxExpected()))
            .append(" arguments; was ")
            .append(String.valueOf(getActualNumberOfArguments()))
            .append(">>");
    }
}
