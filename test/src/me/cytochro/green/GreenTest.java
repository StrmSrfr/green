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

import me.cytochro.zson.EOF;
import me.cytochro.zson.Nil;
import me.cytochro.zson.Objet;
import me.cytochro.zson.Symbol;
import me.cytochro.zson.ZSON;

public class GreenTest {
    @Test
    public void testNilIsNil() throws IOException {
        Green green = new Green();
        Objet result = green.eval("()");
        assertTrue("is nil", result instanceof Nil);
    }

    @Test
    public void testUnbound()  throws IOException {
        Green green = new Green();
        Objet result = green.eval("x");
        assertTrue("is unbound", result instanceof Unbound);
        Unbound ub = (Unbound) result;
        assertEquals("name of var", "x", ub.getSymbol().getName());
    }
}
