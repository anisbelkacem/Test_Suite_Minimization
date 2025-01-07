package de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CoverageFitnessFunctionTest {

    private boolean[][] coverageMatrix;
    private CoverageFitnessFunction fitnessFunction;
    private BiChromosome chromosome;

    @BeforeEach
    void setUp() {
        coverageMatrix = new boolean[][]{
            {true, false, true},   // Test case 0 covers lines 0 and 2
            {false, true, false},  // Test case 1 covers line 1
            {true, true, false}    // Test case 2 covers lines 0 and 1
        };
        int numberLines = 3;

        fitnessFunction = new CoverageFitnessFunction(coverageMatrix, numberLines);

        chromosome = mock(BiChromosome.class);
    }

    @Test
    void applyAsDouble_fullCoverage() {
        when(chromosome.getActiveTestCases()).thenReturn(Arrays.asList(0, 1, 2));

        double fitness = fitnessFunction.applyAsDouble(chromosome);

        // All lines are covered
        assertEquals(1.0, fitness, "Fitness should be 1.0 when all lines are covered");
    }

    @Test
    void applyAsDouble_partialCoverage() {
        when(chromosome.getActiveTestCases()).thenReturn(Arrays.asList(0, 1));

        double fitness = fitnessFunction.applyAsDouble(chromosome);
        assertEquals(1.0, fitness, "Fitness should be 1.0 when all lines are covered using a subset of test cases");
    }

    @Test
    void applyAsDouble_noCoverage() {
        when(chromosome.getActiveTestCases()).thenReturn(Arrays.asList());

        double fitness = fitnessFunction.applyAsDouble(chromosome);
        assertEquals(0.0, fitness, "Fitness should be 0.0 when no test cases are active");
    }

    @Test
    void applyAsDouble_someCoverage() {
        when(chromosome.getActiveTestCases()).thenReturn(Arrays.asList(0));

        double fitness = fitnessFunction.applyAsDouble(chromosome);
        assertEquals(2.0 / 3.0, fitness, 0.0001, "Fitness should reflect the proportion of covered lines");
    }

    @Test
    void applyAsDouble_emptyMatrix() {
        fitnessFunction = new CoverageFitnessFunction(new boolean[0][0], 0);

        when(chromosome.getActiveTestCases()).thenReturn(Arrays.asList(0));

        double fitness = fitnessFunction.applyAsDouble(chromosome);
        assertEquals(0.0, fitness, "Fitness should be 0.0 when there are no lines to cover");
    }
}
