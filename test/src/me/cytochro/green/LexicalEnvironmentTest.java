package me.cytochro.green;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;


import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;

import me.cytochro.zson.Symbol;
import me.cytochro.green.Future;

@RunWith(JMock.class)
public class LexicalEnvironmentTest {
    Mockery context = new JUnit4Mockery();
    
    private static final Symbol X = new Symbol("x");

    @Test
    public void testNullLexicalEnvironmentSizeIs0() {
        assertEquals("size of null lexenv", 0, new LexicalEnvironment().size());
    }

    @Test
    public void testXIsX() {
        final Future f1 = context.mock(Future.class);
        
        final LexicalEnvironment lexenv = new LexicalEnvironment(X, f1);

        assertEquals("value of x", f1, lexenv.get(X));
    }

    @Test
    public void testUnbound() {
        assertNull("unbound variable", new LexicalEnvironment().get(X));
    }

    @Test
    public void testEnclosing() {
        final Future f1 = context.mock(Future.class, "f1");
        final Future f2 = context.mock(Future.class, "f2");

        final LexicalEnvironment outer = new LexicalEnvironment(X, f1);
        final LexicalEnvironment inner =
            new LexicalEnvironment(outer, new Symbol("y"), f2);
        assertEquals("value of x from enclosing scope",
                     f1,
                     inner.get(X));
    }

    @Test
    public void testNoLeak() {
        final Future f1 = context.mock(Future.class);

        final LexicalEnvironment outer = new LexicalEnvironment();
        final LexicalEnvironment inner
            = new LexicalEnvironment(outer, X, f1);
        assertNull("value of inner from outer",
                   outer.get(X));
    }
}
