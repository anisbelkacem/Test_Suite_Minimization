package de.uni_passau.fim.se2.sbse.suite_minimisation.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;
import de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions.CoverageFitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions.TestSuiteSizeFitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_minimisation.stopping_conditions.StoppingCondition;

public class NSGA2Test {
    @Test
    void testFindSolution() {
        // Create mock fitness functions
        FitnessFunction<BiChromosome> sizeFF = new TestSuiteSizeFitnessFunction();
        boolean[][] coverageMatrix = {
            {true, false, true, false, false},
            {false, true, false, true, true},
            {true, false, true, false, true},
            {false, true, false, true, false},
            {true, true, false, false, true}
        };
        
        

        // Create the coverage fitness function with the initialized coverage matrix
        FitnessFunction<BiChromosome> coverageFF = new CoverageFitnessFunction(coverageMatrix, 3);

        // Mock the stopping condition
        StoppingCondition stoppingCondition = mock(StoppingCondition.class);

        // Define behavior for the mock (e.g., the search should not stop)
        final int[] repetitionCount = {0};
        when(stoppingCondition.searchMustStop()).thenAnswer(invocation -> {
        // Increment the repetition count
        repetitionCount[0]++;
        // Stop after 2 repetitions (i.e., when count reaches 3)
         return repetitionCount[0] >= 3;});

        doNothing().when(stoppingCondition).notifySearchStarted();
        doNothing().when(stoppingCondition).notifyFitnessEvaluations(anyInt());
        doNothing().when(stoppingCondition).notifyFitnessEvaluation();

        // Create the NSGA2 algorithm instance
        Random random = new Random();
        @SuppressWarnings({ "rawtypes", "unchecked" })
        NSGA2<BiChromosome> nsga2 = new NSGA2(stoppingCondition, sizeFF, coverageFF, 3, random);
        List<BiChromosome> solution = nsga2.findSolution();
        assertNotNull(solution, "Solution should not be null");
        assertFalse(solution.isEmpty(), "Solution should not be empty");

        //assertEquals(100, solution.size(), "Final population should have 100 individuals");

        
        verify(stoppingCondition, times(1)).notifySearchStarted();
        verify(stoppingCondition, atLeastOnce()).notifyFitnessEvaluations(anyInt());
        verify(stoppingCondition, atLeastOnce()).notifyFitnessEvaluation();
    }


    @Test
    void testGetStoppingCondition() {

    }
}
