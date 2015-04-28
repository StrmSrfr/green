package me.cytochro.green.exception;

import java.io.IOException;
import java.io.IOError;

import me.cytochro.zson.Symbol;
import me.cytochro.zson.T;
import me.cytochro.zson.ZSON;

import me.cytochro.green.Exception;

public class TypeException implements Exception {
    private final T expected;
    private final T actual;

    public TypeException(T expected,
                         T actual) {
        this.expected = expected;
        this.actual = actual;
    }

    public TypeException(String expected,
                         String actual) {
        try {
            this.expected = ZSON.read(expected);
            this.actual = ZSON.read(actual);
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }

    public TypeException(String expected,
                         T actual) {
        try {
            this.expected = ZSON.read(expected);
            this.actual = Symbol.intern(actual.getClass().getName());
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }

    public T getExpected() {
        return expected;
    }
    
    public T getActual() {
        return actual;
    }
}
