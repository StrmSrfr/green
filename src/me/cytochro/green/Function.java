package me.cytochro.green;

import me.cytochro.zson.Nil;
import me.cytochro.zson.Objet;

@FunctionalInterface
public interface Function extends Objet, Nameable {
    public Future apply(Future[] arguments);
}
