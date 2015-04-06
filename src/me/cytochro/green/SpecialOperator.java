package me.cytochro.green;

import me.cytochro.zson.Nil;
import me.cytochro.zson.Objet;

@FunctionalInterface
public interface SpecialOperator extends Nameable {
    public Future eval(Objet expression);
}
