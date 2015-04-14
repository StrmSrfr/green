package me.cytochro.green.exception;

import me.cytochro.zson.T;

import me.cytochro.green.Exception;

public class TypeException implements Exception {
    private final T expected;
    private final T actual;

    public TypeException(T expected,
                         T actual) {
        this.expected = expected;
        this.actual = actual;
    }

    public T getExpected() {
        return expected;
    }
    
    public T getActual() {
        return actual;
    }
}
