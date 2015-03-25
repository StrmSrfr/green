package me.cytochro.green;

import me.cytochro.zson.Objet;

@FunctionalInterface
public interface SpecialOperator {
    public Future eval(Objet expression);
}
