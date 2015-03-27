package me.cytochro.green;

import me.cytochro.zson.Objet;

public interface Function extends Objet {
    public Future apply(Future[] arguments);
}
