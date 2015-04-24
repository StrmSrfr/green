package me.cytochro.green;

import me.cytochro.zson.T;

import me.cytochro.zson.Nil;

public interface Nameable extends T {
    public default T name() {
        return Nil.NIL;
    }
}
    
