package de.uni_passau.fim.se2.sbse.suite_minimisation.utils;

import org.junit.jupiter.api.Test;

import de.uni_passau.fim.se2.sbse.suite_minimisation.algorithms.NSGA2;
import de.uni_passau.fim.se2.sbse.suite_minimisation.stopping_conditions.StoppingCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmBuilderTest {

    private Random random;
    private boolean[][] coverageMatrix;
    private StoppingCondition stoppingCondition;
    private AlgorithmBuilder algorithmBuilder;

    @BeforeEach
    void setUp() {
        random = new Random();
        stoppingCondition = new MockStoppingCondition();
        coverageMatrix = new boolean[][]{
                {true, false, true},
                {false, true, false},
                {true, true, false}
        };
        algorithmBuilder = new AlgorithmBuilder(random, stoppingCondition, coverageMatrix);
    }

    @Test
    void testConstructorInitializesFields() {
        assertNotNull(algorithmBuilder.getSizeFF(), "Size fitness function should not be null.");
        assertNotNull(algorithmBuilder.getCoverageFF(), "Coverage fitness function should not be null.");
    }

    @Test
    void testGetSizeFitnessFunction() {
        var sizeFF = algorithmBuilder.getSizeFF();
        assertNotNull(sizeFF, "Size fitness function should be created.");
        // Mock test for evaluation
        assertTrue(sizeFF.isMinimizing(), "Size fitness function should minimize.");
    }

    @Test
    void testGetCoverageFitnessFunction() {
        var coverageFF = algorithmBuilder.getCoverageFF();
        assertNotNull(coverageFF, "Coverage fitness function should be created.");
        // Mock test for evaluation
        assertFalse(coverageFF.isMinimizing(), "Coverage fitness function should maximize.");
    }
}

class MockStoppingCondition implements StoppingCondition {

    private boolean shouldStop = false;

    @Override
    public void notifySearchStarted() {
        shouldStop = false;
    }

    @Override
    public void notifyFitnessEvaluation() {
        // Mock implementation
    }

    @Override
    public void notifyFitnessEvaluations(int evaluations) {
        // Mock implementation
    }

    @Override
    public boolean searchMustStop() {
        return shouldStop;
    }

    @Override
    public double getProgress() {
        return 0.5; // Mock value
    }

    public void setShouldStop(boolean shouldStop) {
        this.shouldStop = shouldStop;
    }
}