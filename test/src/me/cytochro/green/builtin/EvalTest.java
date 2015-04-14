package me.cytochro.green.builtin;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import me.cytochro.green.Function;
import me.cytochro.green.Green;
import me.cytochro.green.exception.Unbound;
import me.cytochro.green.SpecialOperator;

import static me.cytochro.green.Assert.assertEvalsEqual;

public class EvalTest {
    @Test
    public void testEvalIsFunction() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("eval") instanceof Function);
    }

    @Test
    public void testDefaultLexenv() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("((lambda (x) (eval (quote x))) t)")
                   instanceof Unbound);
    }
}
