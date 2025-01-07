package de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_minimisation.crossover.BiCrossover;
import de.uni_passau.fim.se2.sbse.suite_minimisation.mutation.BiMutation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BiChromosomeTest {
    private BiMutation mutation;
    private BiCrossover crossover;

    @BeforeEach
    void setUp() {
        mutation = mock(BiMutation.class);
        crossover = mock(BiCrossover.class);
    }

    @Test
    void constructor_createsCopyOfTestCases() {
        List<Integer> testCases = Arrays.asList(1, 0, 1);
        BiChromosome chromosome = new BiChromosome(testCases, mutation, crossover);
        assertNotSame(testCases, chromosome.getTestCases(), "Constructor should create a copy of the test cases");
        assertEquals(testCases, chromosome.getTestCases(), "Constructor should retain the same values as the input list");
    }

    @Test
    void generateRandomChromosome() {
        BiChromosome chromosome = BiChromosome.generateRandomChromosome(10, mutation, crossover);
        assertEquals(10, chromosome.getTestCases().size(), "Generated chromosome should have the specified size");
        assertTrue(chromosome.getTestCases().contains(1), "Generated chromosome should contain at least one active test case (1)");
    }

    @Test
    void getActiveTestCases() {
        List<Integer> testCases = Arrays.asList(1, 0, 1, 0, 0);
        BiChromosome chromosome = new BiChromosome(testCases, mutation, crossover);
        List<Integer> activeTestCases = chromosome.getActiveTestCases();
        assertEquals(Arrays.asList(0, 2), activeTestCases, "getActiveTestCases should return correct indices of active test cases");
    }

    @Test
    void CreatesDeepCopy() {
        List<Integer> testCases = Arrays.asList(1, 0, 1);
        BiChromosome original = new BiChromosome(testCases, mutation, crossover);
        BiChromosome copy = original.copy();

        assertNotSame(original, copy, "Copy should be a different instance");
        assertEquals(original, copy, "Copy should be equal to the original");
        assertNotSame(original.getTestCases(), copy.getTestCases(), "Copy should have a separate copy of the test cases list");
    }

    @Test
    void equals_and_hashCode() {
        List<Integer> testCases1 = Arrays.asList(1, 0, 1);
        List<Integer> testCases2 = Arrays.asList(1, 0, 1);
        List<Integer> testCases3 = Arrays.asList(0, 0, 1);

        BiChromosome chromosome1 = new BiChromosome(testCases1, mutation, crossover);
        BiChromosome chromosome2 = new BiChromosome(testCases2, mutation, crossover);
        BiChromosome chromosome3 = new BiChromosome(testCases3, mutation, crossover);

        assertEquals(chromosome1, chromosome2, "Chromosomes with identical test cases should be equal");
        assertNotEquals(chromosome1, chromosome3, "Chromosomes with different test cases should not be equal");
        assertEquals(chromosome1.hashCode(), chromosome2.hashCode(), "Hash codes should match for equal chromosomes");
    }

    @Test
    void toString_returnsCorrectRepresentation() {
        List<Integer> testCases = Arrays.asList(1, 0, 1);

        BiChromosome chromosome = new BiChromosome(testCases, mutation, crossover);

        assertEquals("[1, 0, 1]", chromosome.toString(), "toString should return a string representation of the test cases list");
    }

    @Test
    void constructor_initializes() {
        int size = 10;
        BiChromosomeGenerator generator = new BiChromosomeGenerator(size, mutation, crossover);

        assertNotNull(generator, "Generator should be initialized correctly");
    }
    @Test
    void get_createsDifferentInstancesEachTime() {
        int size = 10;
        BiChromosomeGenerator generator = new BiChromosomeGenerator(size, mutation, crossover);

        BiChromosome chromosome1 = generator.get();
        BiChromosome chromosome2 = generator.get();

        assertNotSame(chromosome1, chromosome2, "Each call to get() should generate a new instance");
        assertNotEquals(chromosome1.getTestCases(), chromosome2.getTestCases(), "Each chromosome should have different test cases");
    }
}
