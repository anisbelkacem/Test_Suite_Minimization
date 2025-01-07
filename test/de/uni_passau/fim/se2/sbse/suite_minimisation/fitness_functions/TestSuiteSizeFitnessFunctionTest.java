package de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestSuiteSizeFitnessFunctionTest {

    private TestSuiteSizeFitnessFunction fitnessFunction;
    private BiChromosome chromosome;

    @BeforeEach
    void setUp() {
        fitnessFunction = new TestSuiteSizeFitnessFunction();

        // Mock chromosome
        chromosome = mock(BiChromosome.class);
    }

    @Test
    void applyAsDouble_fullSuiteActive() {
        when(chromosome.getActiveTestCases()).thenReturn(Arrays.asList(0, 1, 2, 3));
        when(chromosome.getTestCases()).thenReturn(Arrays.asList(1, 1, 1, 1));

        double fitness = fitnessFunction.applyAsDouble(chromosome);
        assertEquals(1.0, fitness, "Fitness should be 1.0 when all test cases are active");
    }

    @Test
    void applyAsDouble_noActiveTestCases() {
        when(chromosome.getActiveTestCases()).thenReturn(Arrays.asList());
        when(chromosome.getTestCases()).thenReturn(Arrays.asList(1, 0, 0, 0));

        double fitness = fitnessFunction.applyAsDouble(chromosome);
        assertEquals(0.0, fitness, "Fitness should be 0.0 when no test cases are active");
    }

    @Test
    void applyAsDouble_someActiveTestCases() {
        when(chromosome.getActiveTestCases()).thenReturn(Arrays.asList(0, 2));
        when(chromosome.getTestCases()).thenReturn(Arrays.asList(1, 0, 1, 0));

        double fitness = fitnessFunction.applyAsDouble(chromosome);
        assertEquals(0.5, fitness, "Fitness should be 0.5 when half of the test cases are active");
    }

    @Test
    void applyAsDouble_emptyTestSuite() {
        when(chromosome.getActiveTestCases()).thenReturn(Arrays.asList());
        when(chromosome.getTestCases()).thenReturn(Arrays.asList());

        double fitness = fitnessFunction.applyAsDouble(chromosome);
        assertEquals(0.0, fitness, "Fitness should be 0.0 when the test suite is empty");
    }

    @Test
    void applyAsDouble_mixedTestCases() {
        when(chromosome.getActiveTestCases()).thenReturn(Arrays.asList(1, 3));
        when(chromosome.getTestCases()).thenReturn(Arrays.asList(0, 1, 0, 1, 0));

        double fitness = fitnessFunction.applyAsDouble(chromosome);
        assertEquals(0.4, fitness, "Fitness should be 0.4 when two out of five test cases are active");
    }
}
