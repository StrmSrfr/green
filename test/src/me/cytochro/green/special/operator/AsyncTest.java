package me.cytochro.green.special.operator;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import me.cytochro.green.Green;
import me.cytochro.green.exception.Unbound;
import me.cytochro.green.SpecialOperator;

import static me.cytochro.green.Assert.assertEvalsEqual;

public class AsyncTest {
    @Test
    public void testIsSpecialOperator() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("async") instanceof SpecialOperator);
    }

    @Test
    public void testFirstUnbound() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("(async x (list))") instanceof Unbound);
    }
}
