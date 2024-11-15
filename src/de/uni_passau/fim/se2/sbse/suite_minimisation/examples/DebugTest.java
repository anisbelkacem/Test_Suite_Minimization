package de.uni_passau.fim.se2.sbse.suite_minimisation.examples;

import org.junit.jupiter.api.Test;

public class DebugTest {

    @Test
    public void test0() {
        Debug d = new Debug();

    }

    @Test
    public void test1() {
        Debug d = new Debug();
        d.foo(1, 2);

    }

    @Test
    public void test2() {
        Debug d = new Debug();
        d.foo2(3, 5);

    }
}
