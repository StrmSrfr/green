package me.cytochro.green.exception;

import java.io.IOException;
import java.io.IOError;

import me.cytochro.zson.Symbol;

import me.cytochro.green.Exception;

public class FileNotFoundException extends me.cytochro.green.exception.IOException {
    private final Symbol name;

    public FileNotFoundException(Symbol name) {
        super(null);
        this.name = name;
    }

    @Override
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
            .append(this.getClass().getName())
            .append("@")
            .append(Integer.toHexString(hashCode()))
            .append("<")
            .append(String.valueOf(name))
            .append(">>");
    }
}
