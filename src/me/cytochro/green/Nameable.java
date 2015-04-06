package me.cytochro.green;

import me.cytochro.zson.Objet;

import me.cytochro.zson.Nil;

public interface Nameable extends Objet {
    public default Objet name() {
        return Nil.NIL;
    }
}
    
