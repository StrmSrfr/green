package me.cytochro.green.exception;

import me.cytochro.zson.Objet;

import me.cytochro.green.Exception;

public class TypeException implements Exception {
    private final Objet expected;
    private final Objet actual;

    public TypeException(Objet expected,
                         Objet actual) {
        this.expected = expected;
        this.actual = actual;
    }

    public Objet getExpected() {
        return expected;
    }
    
    public Objet getACtual() {
        return actual;
    }
}
