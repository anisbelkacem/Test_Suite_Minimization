package de.uni_passau.fim.se2.sbse.suite_minimisation.examples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AddNumbersTest {

    @Test
    void test0() {
        AddNumbers addNumbers = new AddNumbers();
        int res = addNumbers.add(1, 2);
        assertTrue(res == 3);
    }

    @Test
    void test1() {
        AddNumbers addNumbers = new AddNumbers();
        addNumbers.add(1, 2);
        addNumbers.foo(3);
    }

    @Test
    void emptyTest0() {

    }

    @Test
    void emptyTest1() {

    }

    @Test
    void emptyTest2() {

    }

    @Test
    void emptyTest3() {

    }

    private void noTest() {

    }
}