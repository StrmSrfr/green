package me.cytochro.green.special.operator;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import me.cytochro.zson.T;

import me.cytochro.green.Function;
import me.cytochro.green.Green;
import me.cytochro.green.exception.ArityException;
import me.cytochro.green.exception.Unbound;
import me.cytochro.green.SpecialOperator;

import static me.cytochro.green.Assert.assertEvalsEqual;

public class LambdaTest {
    @Test
    public void testIsSpecialOperator() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("lambda") instanceof SpecialOperator);
    }

    @Test
    public void testResultIsFunction() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(lambda () ())") instanceof Function);
    }

    @Test
    public void testNoArgs() throws IOException {
        assertEvalsEqual("atom", "((lambda () atom))");
    }

    @Test
    public void testArityTooHigh() throws IOException {
        Green green = new Green();
        T result = green.eval("((lambda () atom) atom)");
        ArityException ae = (ArityException) result;
        assertEquals("expected max args", 0, ae.getMaxExpected());
        assertEquals("expected min args", 0, ae.getMinExpected());
        assertEquals("actual args", 1, ae.getActualNumberOfArguments());
    }

    @Test
    public void testOneArgConstantly() throws IOException {
        assertEvalsEqual("t", "((lambda (x) t) atom)");
    }

    @Test
    public void testOneArgConstantlyUnbound() throws IOException {
        Green green = new Green();
        T result = green.eval("((lambda (x) t) x)");
        assertTrue(result instanceof Unbound);
    }

    @Test
    public void testOneArgIdentity() throws IOException {
        assertEvalsEqual("atom", "((lambda (x) x) atom)");
    }

    @Test
    public void testOneArgIdentityUnbound() throws IOException {
        Green green = new Green();
        T result = green.eval("((lambda (x) x) y)");
        assertTrue(result instanceof Unbound);
    }

    @Test
    public void testOneArgInnerUnbound() throws IOException {
        Green green = new Green();
        T result = green.eval("((lambda (x) y) atom)");
        assertTrue(result instanceof Unbound);
    }

    @Test
    public void testSimpleNesting() throws IOException {
        assertEvalsEqual("t", "((lambda (x) ((lambda (y) y) t)) atom)");
    }

    @Test
    public void testNestingShadows() throws IOException {
        assertEvalsEqual("t", "((lambda (x) ((lambda (x) x) t)) atom)");
    }

    @Test
    public void testNoLeak() throws IOException {
        assertEvalsEqual("t", "(cdr (cons ((lambda (x) x) ()) ((lambda (x) x) t)))");
    }
}
