package me.cytochro.green;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Assert {
    public static void assertEvalsEqual(String desc,
                                        String expected,
                                        String actual) throws IOException {
        Green green = new Green();
        assertEquals(desc, green.eval(expected), green.eval(actual));
    }
    
    public static void assertEvalsEqual(String expected,
                                        String actual) throws IOException {
        Green green = new Green();
        assertEquals(green.eval(expected), green.eval(actual));
    }
}
