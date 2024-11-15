package de.uni_passau.fim.se2.sbse.suite_minimisation.examples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LiftTest {

    @Test
    public void test0() throws Throwable {
        // test here!
        Lift l = new Lift(10);

    }

    @Test
    public void test1() throws Throwable {
        // test here!
        Lift l = new Lift(10);
        assertFalse(l.isFull());
        assertEquals(10, l.getTopFloor());
    }

    @Test
    public void test2() throws Throwable {
        Lift lift = new Lift(2);
        lift.call(1);
        assertEquals(1, lift.getCurrentFloor());
    }

    @Test
    public void test3() throws Throwable {
        Lift lift = new Lift(3, 4);
        lift.addRiders(3);
        assertEquals(3, lift.getNumRiders());
    }

    @Test
    public void test4() throws Throwable {
        Lift lift = new Lift(3);
        assertEquals(0, lift.getCurrentFloor());
        lift.goUp();
        lift.goUp();
        lift.goUp();
        lift.goUp();
        assertEquals(3, lift.getCurrentFloor());
    }

    @Test
    public void test5() throws Throwable {
        Lift lift = new Lift(1, 1);
        assertEquals(1, lift.getTopFloor());
        assertEquals(1, lift.getCapacity());
    }

    @Test
    public void test6() throws Throwable {
        Lift lift = new Lift(1);
        lift.goUp();
        assertEquals(1, lift.getCurrentFloor());
        lift.goUp();
        assertEquals(1, lift.getCurrentFloor());
    }

    @Test
    public void test7() throws Throwable {
        Lift lift = new Lift(1, 2);
        lift.addRiders(2);
        assertEquals(0, lift.getNumRiders());
        lift.addRiders(2);
        assertEquals(0, lift.getNumRiders());
    }

    @Test
    public void test8() throws Throwable {
        Lift lift = new Lift(1);
        lift.goDown();
        lift.goUp();
        assertEquals(1, lift.getCurrentFloor());
        lift.goDown();
        lift.goDown();
        assertEquals(0, lift.getCurrentFloor());
    }

    @Test
    public void test9() throws Throwable {
        Lift lift = new Lift(2);
        lift.call(3);
        assertEquals(0, lift.getCurrentFloor());
        lift.call(2);
        assertEquals(2, lift.getCurrentFloor());
    }

    @Test
    public void test10() throws Throwable {
        Lift lift = new Lift(1);
        lift.call(-1);
        assertEquals(0, lift.getCurrentFloor());
        lift.goUp();
        lift.goUp();
        lift.call(0);
        assertEquals(0, lift.getCurrentFloor());
    }

    @Test
    public void test11() throws Throwable {
        Lift lift = new Lift(3, 3);
        lift.goUp();
        lift.call(3);
        assertEquals(3, lift.getCurrentFloor());
        lift.call(1);
        lift.goDown();
        assertEquals(0, lift.getCurrentFloor());
    }

    @Test
    public void test12() throws Throwable {
        Lift lift = new Lift(3, 5);
        assertFalse(lift.isFull());
        lift.addRiders(5);
        assertTrue(lift.isFull());
    }

    @Test
    public void test13() throws Throwable {
        Lift lift = new Lift(3, 4);
        assertEquals(4, lift.getCapacity());
        lift.addRiders(3);
        assertEquals(4, lift.getCapacity());
    }

    @Test
    public void test14() throws Throwable {
        Lift lift = new Lift(2);
        lift.goUp();
        assertEquals(1, lift.getCurrentFloor());
        assertEquals(1, lift.getCurrentFloor());
    }

    @Test
    public void test15() throws Throwable {
        Lift lift = new Lift(4);
        lift.goUp();
        lift.call(1);
        assertEquals(1, lift.getCurrentFloor());
        lift.goUp();
        lift.call(2);
        assertEquals(2, lift.getCurrentFloor());
    }

    @Test
    public void test16() throws Throwable {
        Lift lift = new Lift(1);
        assertEquals(10, lift.getCapacity());
    }

    @Test
    public void test17() throws Throwable {
        Lift lift = new Lift(11, 12);
        lift.call(11);
        lift.addRiders(12);
        assertEquals(11, lift.getCurrentFloor());
        assertEquals(12, lift.getNumRiders());
    }

    @Test
    public void test18() throws Throwable {
        Lift lift = new Lift(21, 22);
        assertEquals(22, lift.getCapacity());
        lift.addRiders(23);
        assertEquals(22, lift.getNumRiders());
    }

    @Test
    public void test19() throws Throwable {
        Lift lift = new Lift(21, 22);
        assertEquals(21, lift.getTopFloor());
        lift.call(21);
        lift.goDown();
        assertEquals(20, lift.getCurrentFloor());
    }

    @Test
    public void test20() throws Throwable {
        Lift lift = new Lift(30);
        lift.call(20);
        lift.goDown();
        assertEquals(19, lift.getCurrentFloor());
    }

    @Test
    public void test21() throws Throwable {
        Lift lift = new Lift(11, 12);
        lift.goUp();
        lift.call(11);
        lift.goUp();
        lift.goDown();
        lift.goUp();
        assertEquals(11, lift.getCurrentFloor());
        assertEquals(11, lift.getTopFloor());
    }

    @Test
    public void test22() throws Throwable {
        Lift lift = new Lift(1);
        lift.goDown();
        assertEquals(0, lift.getCurrentFloor());
        assertEquals(10, lift.getCapacity());
    }

    @Test
    public void test23() throws Throwable {
        Lift lift = new Lift(11, 12);
        lift.addRiders(13);
        assertEquals(12, lift.getNumRiders());
        assertTrue(lift.isFull());
    }

    @Test
    public void test24() throws Throwable {
        Lift lift = new Lift(1, 2);
        lift.addRiders(1);
        assertEquals(1, lift.getNumRiders());
        lift.addRiders(1);
        lift.addRiders(1);
        assertEquals(2, lift.getNumRiders());
    }

    @Test
    public void test25() throws Throwable {
        Lift lift = new Lift(10);
        lift.call(10);
        lift.call(0);
        lift.call(10);
        lift.call(0);
        assertEquals(0, lift.getCurrentFloor());
    }

    @Test
    public void test26() throws Throwable {
        Lift lift = new Lift(11, 12);
        lift.call(11);
        lift.goUp();
        assertEquals(11, lift.getCurrentFloor());
        assertEquals(11, lift.getTopFloor());
    }

    @Test
    public void test27() throws Throwable {
        Lift lift = new Lift(1, 5);
        lift.addRiders(6);
        assertEquals(5, lift.getNumRiders());
        assertEquals(5, lift.getCapacity());
    }

    @Test
    public void test28() throws Throwable {
        Lift lift = new Lift(5);
        assertEquals(10, lift.getCapacity());
        assertFalse(lift.isFull());
    }

    @Test
    public void test29() throws Throwable {
        Lift lift = new Lift(3);
        lift.call(3);
        assertEquals(3, lift.getCurrentFloor());
        lift.goDown();
        lift.goDown();
        lift.goDown();
        lift.goDown();
        assertEquals(0, lift.getCurrentFloor());
    }

    @Test
    public void test30() throws Throwable {
        Lift lift = new Lift(1, 3);
        assertEquals(1, lift.getTopFloor());
        lift = new Lift(2);
        assertEquals(2, lift.getTopFloor());
    }

    @Test
    public void test31() throws Throwable {
        Lift lift = new Lift(1);
        assertEquals(0, lift.getCurrentFloor());
        lift = new Lift(1, 1);
        assertEquals(0, lift.getCurrentFloor());
    }

    @Test
    public void test32() throws Throwable {
        Lift lift = new Lift(3);
        lift.goUp();
        assertEquals(1, lift.getCurrentFloor());
        lift.goUp();
        lift.goUp();
        lift.goUp();
        assertEquals(3, lift.getCurrentFloor());
    }
}
