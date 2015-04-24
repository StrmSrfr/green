package me.cytochro.green.exception;

import java.io.IOError;
import java.io.PrintWriter;
import java.io.StringWriter;

import me.cytochro.green.Exception;

public class IOException implements Exception {
    private final java.io.IOException cause;

    public IOException(java.io.IOException cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        try {
            return toString(new StringBuffer()).toString();
        } catch (java.io.IOException ioe) {
            throw new IOError(ioe);
        }
    }

    public Appendable toString(Appendable buf) throws java.io.IOException {
        final String stacktrace;
        if (cause != null) {
            final StringWriter sw = new StringWriter();
            cause.printStackTrace(new PrintWriter(sw));
            stacktrace = sw.toString();
        } else {
            stacktrace = "no Java cause";
        }
        return buf
            .append("#<")
            .append(super.toString())
            .append("<")
            .append(cause.toString())
            .append(">>");
    }
}
