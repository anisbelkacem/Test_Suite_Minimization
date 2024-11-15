package de.uni_passau.fim.se2.sbse.suite_minimisation.examples;

public class Debug {

    public Debug() {

    }

    public int foo(int i, int x) {
        return i + x;
    }

    public int foo2(int i, int x) {
        String s = "";
        for (int b = 0; b < x; b++) {
            s += "ding";
        }
        return s.length();
    }
}
