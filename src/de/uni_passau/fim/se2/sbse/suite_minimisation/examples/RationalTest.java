package de.uni_passau.fim.se2.sbse.suite_minimisation.examples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RationalTest {

    @Test
    public void test0() throws Throwable {
        try {
            Rational r = new Rational(42, 0);
            fail("");
        } catch (ArithmeticException e) {
            assertEquals("", e.getMessage());
        }
    }

    @Test
    public void test1() throws Throwable {
        try {
            new Rational(0, 0);
            fail("...");
        } catch (ArithmeticException e) {
            // ok
        }

    }

    @Test
    public void test2() throws Throwable {
        Rational rational = new Rational(7, 8);
        assertEquals(7, rational.num());
        assertEquals(8, rational.denom());
    }

    @Test
    public void test3() throws Throwable {
        try {
            Rational rational = new Rational(1, 0);
            fail("Expected exception did not occur.");
        } catch (ArithmeticException e) {
        }
    }

    @Test
    public void test4() throws Throwable {
        Rational rational = new Rational(-1, -2);
        rational.reduce();
        assertEquals(1, rational.num());
        assertEquals(2, rational.denom());
    }

    @Test
    public void test5() throws Throwable {
        Rational rat = new Rational(1, -2);
        Rational rat2 = new Rational(-1, 2);
        rat.mul(rat2);
        assertEquals(rat.num(), 1);
        assertEquals(rat.denom(), 4);
    }

    @Test
    public void test6() throws Throwable {
        Rational rat = new Rational(15, 40);
        rat.reduce();
        assertEquals(rat.num(), 3);
        assertEquals(rat.denom(), 8);
    }

    @Test
    public void test7() throws Throwable {
        Rational rat = new Rational(12, 40);
        Rational rat2 = new Rational(8, 40);
        rat.add(rat2);
        assertEquals(rat.num(), 20);
        assertEquals(rat.denom(), 40);
    }

    @Test
    public void test8() throws Throwable {
        Rational rat = new Rational(12, 40);
        Rational rat2 = new Rational(8, -65);
        rat.add(rat2);
        assertEquals(rat.num(), (12 * -65) + (8 * 40));
        assertEquals(rat.denom(), 40 * -65);
    }

    @Test
    public void test9() throws Throwable {
        Rational rat = new Rational(10, 40);
        Rational rat2 = new Rational(20, 40);
        rat.add(rat2);
        rat.reduce();
        assertEquals(3, rat.num());
        assertEquals(4, rat.denom());
    }

    @Test
    public void test10() throws Throwable {
        Rational rat = new Rational(0, 1);
        Rational rat2 = new Rational(-4, 1);
        rat2.sub(rat);
        assertEquals(-4, rat2.num());
        assertEquals(1, rat2.denom());
    }

    @Test
    public void test11() throws Throwable {
        Rational rat = new Rational(-2, 0);
        rat.reduce();
        Rational correct = new Rational(1, 0);
        assertEquals(correct, rat);
    }

    @Test
    public void test12() throws Throwable {
        Rational rat = new Rational(4, -5);
        Rational sub = new Rational(8, -5);
        rat.sub(sub);
        assertEquals(1, rat.num());
        assertEquals(1, rat.denom());
    }

    @Test
    public void test13() throws Throwable {
        // test here!
        Rational rational = new Rational(-2, -2);
        Rational rationalMultiplier = new Rational(1, 1);
        rational.mul(rationalMultiplier);
        assertEquals(rational.num(), 2);
        assertEquals(rational.denom(), 2);
    }

    @Test
    public void test14() throws Throwable {
        Rational rat = new Rational(2, 3);
        Rational ratOther = new Rational(3, 2);
        assertTrue(rat.equals(ratOther));
        assertEquals(rat.num(), 2);
        assertEquals(ratOther.num(), 3);
        assertEquals(rat.denom(), 3);
        assertEquals(ratOther.denom(), 2);


    }

    @Test
    public void test15() throws Throwable {
        Rational rat = new Rational(1, 0);
        assertEquals(rat.num(), 1);
        assertEquals(rat.denom(), 0);
    }

    @Test
    public void test16() throws Throwable {
        Rational rational = new Rational(-1, -1);
        long x = rational.num() / rational.gcd(rational.num(), rational.denom());
        rational.reduce();
        assertEquals(x, rational.num());
    }

    @Test
    public void test17() throws Throwable {
        Rational rational = new Rational(1, 2);
        Rational sub = new Rational(1, 1);
        rational.sub(sub);
        assertEquals(-1, rational.num());
    }

    @Test
    public void test18() throws Throwable {
        Rational rational = new Rational(2, 2);
        long num = 2;
        rational.reduce();
        assertEquals(1, rational.num());
    }

    @Test
    public void test19() throws Throwable {
        Rational rational = new Rational(2, 2);
        Rational add = new Rational(1, 1);
        rational.add(add);
        long num = 4;
        assertEquals(4, rational.num());
    }

    @Test
    public void test20() throws Throwable {

        Rational rational = new Rational(0, 1);
        assertEquals(1, rational.denom());
    }

    @Test
    public void test21() throws Throwable {
        Rational rational = new Rational(2, 2);
        Rational rational2 = new Rational(1, -2);
        rational.add(rational2);
        assertEquals(-1, rational.num());
        assertEquals(2, rational.num());
    }

    @Test
    public void test22() throws Throwable {
        Rational rational = new Rational(-5, 35);
        Rational rational2 = new Rational(5, 20);
        rational.sub(rational2);
        assertEquals(-1, rational.num());
        assertEquals(7, rational.denom());
    }

    @Test
    public void test23() throws Throwable {
        Rational rational = new Rational(-5, 35);
        Rational rational2 = new Rational(5, 20);
        rational.sub(rational2);
        assertEquals(rational, rational2);
    }

    @Test
    public void test24() throws Throwable {
        Rational rational = new Rational(-5, 35);
        Rational rational2 = new Rational(-5, 35);
        boolean test = rational.equals(rational2);
        assertTrue(test);
    }

    @Test
    public void test25() throws Throwable {
        Rational rational = new Rational(-5, -35);
        Rational rational2 = new Rational(-5, 35);
        boolean test = rational.equals(rational2);
        assertFalse(test);
    }

    @Test
    public void test26() throws Throwable {
        try {
            Rational rational = new Rational(-5, 0);
        } catch (ArithmeticException e) {
            assertTrue(true);
        }
    }

    @Test
    public void test27() throws Throwable {
        Rational rational = new Rational(-5, -35);
        Rational rational2 = new Rational(1, 1);
        rational.div(rational2);
        assertEquals(5, rational.num());
        assertEquals(35, rational.denom());
    }

    @Test
    public void test28() throws Throwable {
        Rational rational = new Rational(-5, -35);
        Rational rational2 = new Rational(2, 35);
        rational.add(rational2);
        assertEquals(5, rational.num());
        assertEquals(35, rational.denom());
    }

    @Test
    public void test29() throws Throwable {
        Rational rational = new Rational(2, 4);
        assertEquals(2, rational.num());
    }

    @Test
    public void test30() throws Throwable {
        Rational rational = new Rational(2, 4);
        Rational rational2 = new Rational(4, 8);
        assertTrue(rational.equals(rational2));
    }

    @Test
    public void test31() throws Throwable {
        Rational rational = new Rational(2, 4);
        assertEquals(2, rational.gcd(4, 8));
    }

    @Test
    public void test32() throws Throwable {
        // test here!
        Rational rat = new Rational(2, 2);
    }

    @Test
    public void test33() throws Throwable {
        // test here!
        Rational rat = new Rational(2, 2);
        assertEquals(2, rat.gcd(2, 2));

    }

    @Test
    public void test34() throws Throwable {
        // test here!
        Rational r = new Rational(2, 1);
        r.reduce();
        assertEquals(2, r.num());
        assertEquals(1, r.denom());
    }

    @Test
    public void test35() throws Throwable {
        Rational a = new Rational(1, 1);
        a.reduce();
        assertEquals(-1, a.num());
    }

    @Test
    public void test36() throws Throwable {
        Rational a = new Rational(1, 2);
        assertEquals(2, a.denom());
    }

    @Test
    public void test37() throws Throwable {
        Rational a = new Rational(2, 1);
        assertEquals(2, a.num());
    }

    @Test
    public void test38() throws Throwable {
        Rational a = new Rational(-2, -1);
        assertEquals(2, a.num());
        assertEquals(1, a.denom());
    }

    @Test
    public void test39() throws Throwable {
        Rational a = new Rational(-2, -1);
        a.reduce();
        assertEquals(2, a.num());
        assertEquals(1, a.denom());
    }

    @Test
    public void test40() throws Throwable {
        Rational a = new Rational(4, 2);
        Rational b = new Rational(1, 0);
        long toTest = a.gcd(4, 2);
        long toTest2 = a.gcd(1, 0);
        assertEquals(1, toTest2);
        assertEquals(2, toTest);
    }

    @Test
    public void test41() throws Throwable {
        Rational a = new Rational(4, 2);
        long toTest = a.gcd(4, 2);
        long toTest2 = a.gcd(1, 0);
        assertEquals(1, toTest2);
        assertEquals(2, toTest);
    }

    @Test
    public void test42() throws Throwable {
        Rational r1 = new Rational(2, 2);
        Rational r2 = new Rational(-1, 2);
        Rational expected = new Rational(1, 2);
        long gcd = r2.gcd(1, -2);

        r1.add(r2);

        assertEquals(r1, expected);
        assertEquals(gcd, 1);
    }

    @Test
    public void test43() throws Throwable {
        Rational a = new Rational(2, 3);
        Rational b = new Rational(5, 6);
        a.add(b);
        assertTrue(a.num() == 2);
        assertTrue(a.denom() == 3);
    }

    @Test
    public void test44() throws Throwable {
        Rational a = new Rational(2, 3);
        Rational b = new Rational(5, 6);
        a.add(b);
        assertEquals(2, a.num());
        assertEquals(3, a.denom());
    }

    @Test
    public void test45() throws Throwable {
        Rational a = new Rational(2, 3);
        Rational b = new Rational(5, 6);
        a.add(b);
        assertEquals(2, a.num());

    }

    @Test
    public void test46() throws Throwable {
        Rational a = new Rational(1, 0);
    }
}
