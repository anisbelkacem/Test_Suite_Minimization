package de.uni_passau.fim.se2.sbse.suite_minimisation.stopping_conditions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MaxFitnessEvaluationsTest {

    @Test
    public void testConstructorWithPositiveValue() {
        MaxFitnessEvaluations stoppingCondition = new MaxFitnessEvaluations(100);
        assertNotNull(stoppingCondition, "StoppingCondition instance should not be null.");
    }

    @Test
    public void testConstructorWithNonPositiveValue() {
        assertThrows(IllegalArgumentException.class, () -> new MaxFitnessEvaluations(0), 
            "Constructor should throw exception for non-positive maxFitnessEvaluations.");
        assertThrows(IllegalArgumentException.class, () -> new MaxFitnessEvaluations(-5), 
            "Constructor should throw exception for negative maxFitnessEvaluations.");
    }

    @Test
    public void testOfStaticFactoryMethod() {
        MaxFitnessEvaluations stoppingCondition = MaxFitnessEvaluations.of(50);
        assertNotNull(stoppingCondition, "Static factory method should create a valid instance.");
    }

    @Test
    public void testNotifySearchStarted() {
        MaxFitnessEvaluations stoppingCondition = new MaxFitnessEvaluations(100);
        stoppingCondition.notifySearchStarted();
        assertEquals(0, stoppingCondition.getProgress() * 100, 
            "Progress should be 0 after notifySearchStarted.");
    }

    @Test
    public void testNotifyFitnessEvaluation() {
        MaxFitnessEvaluations stoppingCondition = new MaxFitnessEvaluations(10);
        stoppingCondition.notifySearchStarted();

        stoppingCondition.notifyFitnessEvaluation();
        assertEquals(0.1, stoppingCondition.getProgress(), 0.01, 
            "Progress should reflect single fitness evaluation.");

        stoppingCondition.notifyFitnessEvaluation();
        assertEquals(0.2, stoppingCondition.getProgress(), 0.01, 
            "Progress should reflect cumulative fitness evaluations.");
    }

    @Test
    public void testNotifyFitnessEvaluationsWithPositiveValue() {
        MaxFitnessEvaluations stoppingCondition = new MaxFitnessEvaluations(50);
        stoppingCondition.notifySearchStarted();

        stoppingCondition.notifyFitnessEvaluations(10);
        assertEquals(0.2, stoppingCondition.getProgress(), 0.01, 
            "Progress should reflect added fitness evaluations.");
    }

    @Test
    public void testNotifyFitnessEvaluationsWithNegativeValue() {
        MaxFitnessEvaluations stoppingCondition = new MaxFitnessEvaluations(50);
        assertThrows(IllegalArgumentException.class, 
            () -> stoppingCondition.notifyFitnessEvaluations(-5), 
            "Should throw exception for negative evaluations.");
    }

    @Test
    public void testSearchMustStop() {
        MaxFitnessEvaluations stoppingCondition = new MaxFitnessEvaluations(5);
        stoppingCondition.notifySearchStarted();

        for (int i = 0; i < 5; i++) {
            stoppingCondition.notifyFitnessEvaluation();
        }

        assertTrue(stoppingCondition.searchMustStop(), 
            "searchMustStop should return true after maxFitnessEvaluations is reached.");
    }

    @Test
    public void testGetProgress() {
        MaxFitnessEvaluations stoppingCondition = new MaxFitnessEvaluations(20);
        stoppingCondition.notifySearchStarted();

        stoppingCondition.notifyFitnessEvaluations(10);
        assertEquals(0.5, stoppingCondition.getProgress(), 0.01, 
            "Progress should be calculated correctly.");
    }

    @Test
    public void testToString() {
        MaxFitnessEvaluations stoppingCondition = new MaxFitnessEvaluations(100);
        String expected = "MaxFitnessEvaluations(100)";
        assertEquals(expected, stoppingCondition.toString(), 
            "toString should return the expected format.");
    }
}
