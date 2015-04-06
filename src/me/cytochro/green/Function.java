package me.cytochro.green;

import java.util.Optional;

import me.cytochro.zson.Nil;
import me.cytochro.zson.Objet;

@FunctionalInterface
public interface Function extends Objet, Nameable {
    public Future apply(Future[] arguments);
}
