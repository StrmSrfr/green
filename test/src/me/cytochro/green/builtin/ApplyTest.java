package me.cytochro.green.builtin;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import me.cytochro.green.Function;
import me.cytochro.green.Green;
import me.cytochro.green.exception.ArityException;
import me.cytochro.green.SpecialOperator;

import me.cytochro.zson.T;

import static me.cytochro.green.Assert.assertEvalsEqual;

public class ApplyTest {
    @Test
    public void testIsFunction() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("apply") instanceof Function);
    }

    @Test
    public void testApplyAtomNil() throws IOException {
        assertEvalsEqual("t", "(apply atom (cons () ()))");
    }

    @Test
    public void testApplyAtomAtom() throws IOException {
        assertEvalsEqual("t", "(apply atom (cons (quote atom) ()))");
    }

    @Test
    public void testApplyAtomAtomQuoted() throws IOException {
        assertEvalsEqual("t", "(apply atom (quote (atom))))");
    }

    @Test
    public void testApplyAtomList() throws IOException {
        assertEvalsEqual("()", "(apply atom (cons (cons () ()) ()))");
    }

    @Test
    public void testApplyAtomListQuoted() throws IOException {
        assertEvalsEqual("()", "(apply atom (quote (((a b c)))))");
    }

    @Test
    public void testAtomTwoArgArityException() throws IOException {
        final Green green = new Green();
        final T result = green.eval("(apply atom (quote (a b)))");
        assertTrue(result instanceof ArityException);
    }
}
