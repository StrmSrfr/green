package me.cytochro.green;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import me.cytochro.zson.Symbol;

import me.cytochro.green.Future;

public class LexicalEnvironment extends AbstractMap<Symbol, Future> {
    public LexicalEnvironment() {
        this(null, ImmutableMap.of());
    }

    public LexicalEnvironment(LexicalEnvironment parent,
                              Map<Symbol, Future> bindings) {
        this.parent = parent;
        this.bindings = ImmutableMap.copyOf(bindings);
    }

    public LexicalEnvironment(LexicalEnvironment parent,
                              Symbol name,
                              Future binding) {
        this(parent, ImmutableMap.of(name, binding));
    }

    public Set<Map.Entry<Symbol, Future>> entrySet() {
        if (parent == null) {
            return bindings.entrySet();
        } else {
            Set<Map.Entry<Symbol, Future>> result = new HashSet<>();
            result.addAll(bindings.entrySet());
            result.addAll(parent.entrySet());
            return ImmutableSet.copyOf(result);
        }
    }
        
    private final Map<Symbol, Future> bindings;
    private final LexicalEnvironment parent;
}
