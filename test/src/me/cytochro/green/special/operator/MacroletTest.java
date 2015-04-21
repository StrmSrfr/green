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

public class MacroletTest {
    @Test
    public void testIsSpecialOperator() throws IOException {
        Green green = new Green();
        assertTrue(green.eval("macrolet") instanceof SpecialOperator);
    }

    @Test
    public void testXUbound() throws IOException {
        Green green = new Green();
        T result = green.eval("(macrolet ((macrox () (quote x))) (macrox))");
        assertTrue(result instanceof Unbound);
    }

    @Test
    public void testQuoteAtom() throws IOException {
        assertEvalsEqual("atom", "(macrolet ((x () (quote atom))) (x))");
    }

    @Test
    public void testLet() throws IOException {
        assertEvalsEqual("atom",
                         "((lambda (list mapcar)"
                         + " (macrolet ((let (bindings expression)"
                         + "              (cons (list (quote lambda) (mapcar car bindings)"
                         + "                      expression)"
                         + "                (mapcar car"
                         + "                        (mapcar cdr bindings)))))"
                         + "   (let ((x atom)) x)))"
                         + "(lambda list list)"
                         + "((lambda (mapcar)"
                         + "   (lambda (f list)"
                         + "     (mapcar f list mapcar)))"
                         + " (lambda (f list mapcar)"
                         + "   (if list"
                         + "     (cons (f (car list)) (mapcar f (cdr list) mapcar))))))");
    }
}
