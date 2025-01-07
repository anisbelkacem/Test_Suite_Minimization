package de.uni_passau.fim.se2.sbse.suite_minimisation.utils;

import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    @Test
    void testConstructorAndGetters() {
        Pair<String> pair = new Pair<>("First", "Second");
        assertEquals("First", pair.getFst());
        assertEquals("Second", pair.getSnd());
    }

    @Test
    void testConstructorWithNullValues() {
        assertThrows(NullPointerException.class, () -> new Pair<>(null, "Second"));
        assertThrows(NullPointerException.class, () -> new Pair<>("First", null));
    }

    @Test
    void testCopyConstructor() {
        Pair<Integer> original = new Pair<>(1, 2);
        Pair<Integer> copy = new Pair<>(original);
        assertEquals(original.getFst(), copy.getFst());
        assertEquals(original.getSnd(), copy.getSnd());
    }

    @Test
    void testOfFactoryMethod() {
        Pair<Double> pair = Pair.of(1.1, 2.2);
        assertEquals(1.1, pair.getFst());
        assertEquals(2.2, pair.getSnd());
    }

    @Test
    void testGenerateFactoryMethod() {
        Supplier<String> supplier = () -> "constant";
        Pair<String> pair = Pair.generate(supplier);
        assertEquals("constant", pair.getFst());
        assertEquals("constant", pair.getSnd());
    }

    @Test
    void testMapMethod() {
        Pair<Integer> pair = new Pair<>(3, 4);
        Pair<String> mapped = pair.map(Object::toString);
        assertEquals("3", mapped.getFst());
        assertEquals("4", mapped.getSnd());
    }

    @Test
    void testReduceWithBiFunction() {
        Pair<Integer> pair = new Pair<>(5, 10);
        int sum = pair.reduce(Integer::sum);
        assertEquals(15, sum);
    }

    @Test
    void testEqualsAndHashCode() {
        Pair<String> pair1 = new Pair<>("First", "Second");
        Pair<String> pair2 = new Pair<>("First", "Second");
        Pair<String> pair3 = new Pair<>("Different", "Second");

        assertEquals(pair1, pair2);
        assertEquals(pair1.hashCode(), pair2.hashCode());
        assertNotEquals(pair1, pair3);
    }

    @Test
    void testToString() {
        Pair<String> pair = new Pair<>("First", "Second");
        assertEquals("Pair(First, Second)", pair.toString());
    }

    @Test
    void testIterator() {
        Pair<String> pair = new Pair<>("First", "Second");
        var iterator = pair.iterator();

        assertTrue(iterator.hasNext());
        assertEquals("First", iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals("Second", iterator.next());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testSize() {
        Pair<String> pair = new Pair<>("First", "Second");
        assertEquals(2, pair.size());
    }
}

