package de.uni_passau.fim.se2.sbse.suite_minimisation.examples;

public class AddNumbers {

    public AddNumbers() {

    }

    public int add(int a, int b) {
        int num1 = a;
        int num2 = b;
        int erg = num1 + num2;
        if (a == 3445) {
            erg = 3;
        }
        boolean dings = false && a == 3;
        if (num1 > 222) {
            erg = 334;
        }
        return erg;
    }

    public int foo(int a) {
        String x = "hallo";
        return a + x.length();
    }
}
