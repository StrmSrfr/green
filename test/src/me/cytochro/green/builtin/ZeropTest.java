package me.cytochro.green.builtin;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import me.cytochro.green.Function;
import me.cytochro.green.Green;
import me.cytochro.green.exception.TypeException;
import me.cytochro.green.SpecialOperator;

import me.cytochro.zson.T;

import static me.cytochro.green.Assert.assertEvalsEqual;

public class ZeropTest {
    @Test
    public void testIsFunction() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("zerop") instanceof Function);
    }

    @Test
    public void testAtom() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(zerop atom)") instanceof TypeException);
    }

    @Test
    public void testOne() throws IOException {
        assertEvalsEqual("(zerop 0b1)", "()");
    }

    @Test
    public void testZero() throws IOException {
        assertEvalsEqual("(zerop 0b0)", "0b0");
    }
}
