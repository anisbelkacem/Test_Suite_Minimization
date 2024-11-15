package de.uni_passau.fim.se2.sbse.suite_minimisation.examples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComplexTest {

    @Test
    public void test0() throws Throwable {
        double value = 2.5;
        Complex com = new Complex(value, value);
        assertEquals(com.real, value);
        assertEquals(com.imag, value);
    }

    @Test
    public void test1() throws Throwable {
        double value = 2.5;
        Complex com = new Complex(value, value);
        assertEquals(com.real, value, 0);
        assertEquals(com.imag, value, 0);
    }

    @Test
    public void test2() throws Throwable {
        double value = Double.MAX_VALUE;
        Complex com = new Complex(value, value);
        assertEquals(com.real, Double.MAX_VALUE);
        assertEquals(com.imag, Double.MAX_VALUE);
    }

    @Test
    public void test3() throws Throwable {
        double value = 2.5;
        Complex com1 = new Complex(value, value);

        Complex com2 = new Complex(value, value);
        Complex wow = com1.add(com2);
        assertEquals(wow.real, 2 * value);
        assertEquals(wow.imag, 2 * value);

    }

    @Test
    public void test4() throws Throwable {
        Complex c = new Complex(1.0, 2.0);
        assertTrue(c.real > 0.9 && c.real < 1.1);
    }

    @Test
    public void test5() throws Throwable {
        Complex c = new Complex(1.0, 2.0);
        assertEquals(c.real, 1.0, 0);
    }

    @Test
    public void test6() throws Throwable {
        Complex c = new Complex(0, 4.77);
        assertTrue(c.isPure());
    }

    @Test
    public void test7() throws Throwable {
        Complex c = new Complex(2.0, 3.0);
        assertEquals(2.0, c.real);
        assertEquals(3.0, c.imag);
    }

    @Test
    public void test8() throws Throwable {
        Complex num = new Complex(2.0, 1.0);
        Complex numPower = new Complex(1.0, 1.0);
        assertEquals(1.29, num.pow(numPower).real, 0.01);
        assertEquals(1.85, num.pow(numPower).imag, 0.01);
    }

    @Test
    public void test9() throws Throwable {
        Complex firstAddend = new Complex(2.0, 1.0);
        Complex secondAddend = new Complex(1.0, 3.0);
        Complex sum = firstAddend.add(secondAddend);

        assertEquals(3.0, sum.real, 0.01);
        assertEquals(4.0, sum.imag, 0.01);
    }

    @Test
    public void test10() throws Throwable {
        Complex firstFactor = new Complex(2.0, 1.0);
        Complex secondFactor = new Complex(1.0, 3.0);
        Complex product = firstFactor.multiply(secondFactor);

        assertEquals(-1.0, product.real, 0.01);
        assertEquals(7.0, product.imag, 0.01);
    }

    @Test
    public void test11() throws Throwable {
        Complex firstFactor = new Complex(2.0, 1.0);
        double secondFactor = 2.0;
        Complex product = firstFactor.multiply(secondFactor);

        assertEquals(4.0, product.real, 0.01);
        assertEquals(2.0, product.imag, 0.01);
    }

    @Test
    public void test12() throws Throwable {
        Complex complex = new Complex(3.0, 1.0);

        assertEquals(Math.sqrt(10.0), complex.mod(), 0.01);
        assertEquals(Math.sqrt(10.0), complex.norm(), 0.01);
    }

    @Test
    public void test13() throws Throwable {
        Complex minuend = new Complex(3.0, 2.0);
        Complex subtrahend = new Complex(2.0, 1.0);
        Complex difference = minuend.subtract(subtrahend);

        assertEquals(1.0, difference.real, 0.01);
        assertEquals(1.0, difference.imag, 0.01);
    }

    @Test
    public void test14() throws Throwable {
        Complex complex = new Complex(3.0, 2.0);
        Complex negatedComplex = complex.negate();

        assertEquals(-3.0, negatedComplex.real, 0.01);
        assertEquals(-2.0, negatedComplex.imag, 0.01);
    }

    @Test
    public void test15() throws Throwable {
        Complex dividend = new Complex(2.0, 1.0);
        Complex divisor = new Complex(1.0, 1.0);
        Complex quotient = dividend.divide(divisor);

        assertEquals(1.5, quotient.real, 0.01);
        assertEquals(-0.5, quotient.imag, 0.01);
    }

    @Test
    public void test16() throws Throwable {
        Complex dividend = new Complex(4.0, 2.0);
        double divisor = 2;
        Complex quotient = dividend.divide(divisor);

        assertEquals(2.0, quotient.real, 0.01);
        assertEquals(1.0, quotient.imag, 0.01);
    }

    @Test
    public void test17() throws Throwable {
        Complex complex = new Complex(2.0, 2.0);

        assertEquals(0.25, complex.inverse().real, 0.01);
        assertEquals(-0.25, complex.inverse().imag, 0.01);
    }

    @Test
    public void test18() throws Throwable {
        Complex complex = new Complex(3.0, 1.0);

        assertEquals(1.15, complex.log().real, 0.01);
        assertEquals(0.32, complex.log().imag, 0.01);
    }

    @Test
    public void test19() throws Throwable {
        Complex complex = new Complex(3.0, 1.0);

        assertEquals(complex.angle(), complex.phase(), 0.01);
        assertEquals(complex.angle(), complex.phase(), 0.01);
    }

    @Test
    public void test20() throws Throwable {
        Complex complex = new Complex(3.0, 1.0);
        Complex complexNegative = new Complex(3.0, -1.0);

        assertEquals("3.0 + 1.0 i", complex.toString());
        assertEquals("3.0 - 1.0 i", complexNegative.toString());
    }

    @Test
    public void test21() throws Throwable {
        Complex num = new Complex(2.0, 1.0);
        assertEquals(3.0, num.pow(2.0).real, 0.0001);
        assertEquals(4.0, num.pow(2.0).imag, 0.0001);
    }
}

