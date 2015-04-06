package me.cytochro.green.special.operator;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import me.cytochro.green.Green;
import me.cytochro.green.exception.Unbound;
import me.cytochro.green.SpecialOperator;

import static me.cytochro.green.Assert.assertEvalsEqual;

public class IfTest {
    @Test
    public void testIfIsSpecialOperator() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("if") instanceof SpecialOperator);
    }

    @Test
    public void testIfNil1() throws IOException {
        assertEvalsEqual("()", "(if () atom)");
    }

    @Test
    public void testIfNil2() throws IOException {
        assertEvalsEqual("atom", "(if () t atom)");
    }

    @Test
    public void testIfT1() throws IOException {
        assertEvalsEqual("atom", "(if t atom)");
    }

    @Test
    public void testIfT2() throws IOException {
        assertEvalsEqual("atom", "(if t atom t)");
    }

    @Test
    public void testIfObject1() throws IOException {
        assertEvalsEqual("atom", "(if if atom)");
    }

    @Test
    public void testIfObject2() throws IOException {
        assertEvalsEqual("atom", "(if if atom eq)");
    }

    @Test
    public void testUnbound() throws IOException {
        assertTrue(new Green().eval("(if x t t)") instanceof Unbound);
    }
}
