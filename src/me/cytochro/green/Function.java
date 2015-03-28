package me.cytochro.green;

import java.util.Optional;

import me.cytochro.zson.Nil;
import me.cytochro.zson.Objet;

@FunctionalInterface
public interface Function extends Objet {
    public Future apply(Future[] arguments);

    public default Objet name() {
        return Nil.NIL;
    }
}
