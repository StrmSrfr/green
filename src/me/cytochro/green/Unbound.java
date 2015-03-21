package me.cytochro.green;

import java.io.IOError;
import java.io.IOException;

import me.cytochro.zson.Objet;
import me.cytochro.zson.Symbol;

/**
 * Represents an unbound variable.
 */
public class Unbound implements Objet {
    public Unbound(Symbol name) {
        this.symbol = name;
    }

    private final Symbol symbol;

    public Symbol getSymbol() {
        return symbol;
    }

    public String toString() {
        try {
            return toString(new StringBuffer()).toString();
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }

    public Appendable toString(Appendable buf) throws IOException {
        return buf
            .append("#<")
            .append(super.toString())
            .append("<")
            .append(symbol.toString())
            .append(">>");
    }
}
