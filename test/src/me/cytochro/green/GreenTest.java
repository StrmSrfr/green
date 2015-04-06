package me.cytochro.green;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;

import me.cytochro.zson.Cons;
import me.cytochro.zson.EOF;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.ZSON;

import static me.cytochro.green.Assert.assertEvalsEqual;

import me.cytochro.green.exception.ArityException;
import me.cytochro.green.exception.TypeException;
import me.cytochro.green.exception.Unbound;
import me.cytochro.green.special.operator.Quote;

public class GreenTest {
    private static final Symbol X = Symbol.intern("x");

    @Test
    public void testNilIsNil() throws IOException {
        Green green = new Green();
        Objet result = green.eval("()");
        assertTrue("is nil", result instanceof Nil);
    }

    @Test
    public void testTIsT() throws IOException {
        final Green green = new Green();
        final Objet result = green.eval("t");
        assertEquals("t", green.getT(), result);
    }

    @Test
    public void testUnbound()  throws IOException {
        Green green = new Green();
        Objet result = green.eval("x");
        assertTrue("is unbound", result instanceof Unbound);
        Unbound ub = (Unbound) result;
        assertEquals("name of var", "x", ub.getSymbol().getName());
    }

    @Test
    public void testNestedUnboundFunction() throws IOException {
        final Green green = new Green();
        final Objet result = green.eval("((f x) y)");
        assertTrue("is unbound", result instanceof Unbound);
        final Unbound ub = (Unbound) result;
        assertEquals("name of var", "f", ub.getSymbol().getName());
    }

    @Test
    public void testUnboundFunction() throws IOException {
        final Green green = new Green();
        final Objet result = green.eval("(f x)");
        assertTrue("is unbound", result instanceof Unbound);
        final Unbound ub = (Unbound) result;
        assertEquals("name of var", "f", ub.getSymbol().getName());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testNilInFunctionPosition() throws IOException {
        new Green().eval("(() a b c)");
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testNestedNilInFunctionPosition() throws IOException {
        new Green().eval("((()) a b c)");
    }

    @Test
    public void testQuoteByItself() throws IOException {
        assertTrue("quote evaluates to its special operator",
                     new Green().eval("quote") instanceof Quote);
    }

    @Test
    public void testQuoteNothing() throws IOException {
        assertEquals(Nil.NIL, new Green().eval("(quote)"));
    }

    @Test
    public void testQuoteNil() throws IOException {
        assertEquals(Nil.NIL, new Green().eval("(quote ())"));
    }

    @Test
    public void testQuoteQuote() throws IOException {
        assertEquals(new Quote().name(), new Green().eval("(quote quote)"));
    }

    @Test
    public void testQuoteT() throws IOException {
        final Green green = new Green();
        assertEquals(green.getT(), green.eval("(quote t)"));
    }

    @Test
    public void testQuoteX() throws IOException {
        assertEquals(X, new Green().eval("(quote x)"));
    }

    @Test
    public void testQuoteList() throws IOException {
        assertTrue(new Green().eval("(quote (a list))") instanceof Cons);
    }

    @Test
    public void testAtomIsAnAtom() throws IOException {
        Green green = new Green();
        assertEquals(green.getT(), green.eval("(atom atom)"));
    }

    @Test
    public void testTIsAnAtom() throws IOException {
        Green green = new Green();
        assertEquals(green.getT(), green.eval("(atom t)"));
    }

    @Test
    public void testNilIsAnAtom() throws IOException {
        Green green = new Green();
        assertEquals(green.getT(), green.eval("(atom ())"));
    }

    @Test
    public void testQuotedAtom() throws IOException {
        Green green = new Green();
        assertEquals(green.getT(), green.eval("(atom (quote x))"));
    }

    @Test
    public void testUnboundAtom() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(atom x)") instanceof Unbound);
    }

    @Test
    public void testConsIsNotAnAtom() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(atom (quote (a list)))") instanceof Nil);
    }

    @Test
    public void testAtomNoArgs() throws IOException {
        Green green = new Green();
        Objet o = green.eval("(atom )");
        assertTrue(o instanceof ArityException);
    }

    @Test
    public void testAtomTwoArgs() throws IOException {
        Green green = new Green();
        Objet o = green.eval("(atom t t)");
        assertTrue(o instanceof ArityException);
    }

    @Test
    public void testEqNoArgs() throws IOException {
        Green green = new Green();
        assertEquals(green.getT(), green.eval("(eq)")); // TODO should this be true or false?
    }

    @Test
    public void testEqOneUnbound() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(eq x)") instanceof Unbound);
    }

    @Test
    public void testEqT() throws IOException {
        Green green = new Green();
        assertEquals(green.getT(), green.eval("(eq t)"));
    }

    @Test
    public void testEqOneQuoted() throws IOException {
        Green green = new Green();
        assertEquals(green.getT(), green.eval("(eq (quote x))"));
    }

    @Test
    public void testEqTwoQuoted() throws IOException {
        Green green = new Green();
        assertEquals(green.getT(), green.eval("(eq (quote x) (quote x))"));
    }

    @Test
    public void testEqFunctions() throws IOException {
        Green green = new Green();
        assertEquals(green.getT(), green.eval("(eq atom atom)"));
    }

    @Test
    public void testNotEqTwoQuoted() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(eq (quote x) (quote y))") instanceof Nil);
    }

    @Test
    public void testNotEqFunctions() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(eq atom eq)") instanceof Nil);
    }

    @Test
    public void testCarArity() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(car)") instanceof ArityException);
        assertTrue(green.eval("(car car car)") instanceof ArityException);
    }

    @Test
    public void testCarNil() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(car ())") instanceof Nil);
    }

    @Test
    public void testCarAtom() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(car atom)") instanceof TypeException);
    }

    @Test
    public void testCarList() throws IOException {
        Green green = new Green();
        assertEquals(Symbol.intern("car"), green.eval("(car (quote (car)))"));
    }

    @Test
    public void testWeHashcons() throws IOException {
        Green green = new Green();
        assertEquals(green.getT(),
                     green.eval("(eq (cons atom atom) (cons atom atom))"));
    }

    @Test
    public void testCarCons() throws IOException {
        assertEvalsEqual("atom",
                         "(car (cons atom cdr))");
    }

    @Test
    public void testCdrCons() throws IOException {
        Green green = new Green();
        assertEvalsEqual("atom",
                         "(cdr (cons car atom))");
    }

    @Test
    public void testCarConsNil() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(car (cons () atom))") instanceof Nil);
    }

    @Test
    public void testCdrConsNil() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(cdr (cons atom ()))") instanceof Nil);
    }

    @Test
    public void testConsReturnsCons() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(cons car cdr)") instanceof Cons);
    }
}
