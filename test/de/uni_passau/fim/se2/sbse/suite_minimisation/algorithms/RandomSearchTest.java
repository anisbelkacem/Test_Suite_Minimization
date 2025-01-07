package de.uni_passau.fim.se2.sbse.suite_minimisation.algorithms;

import org.junit.jupiter.api.Test;


import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;
import de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions.MinimizingFitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions.MaximizingFitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_minimisation.stopping_conditions.StoppingCondition;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RandomSearchTest {

    private StoppingCondition stoppingCondition;
    private MinimizingFitnessFunction<BiChromosome> sizeFF;
    private MaximizingFitnessFunction<BiChromosome> coverageFF;
    private RandomSearch<BiChromosome> randomSearch;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        stoppingCondition = Mockito.mock(StoppingCondition.class);
        sizeFF = Mockito.mock(MinimizingFitnessFunction.class);
        coverageFF = Mockito.mock(MaximizingFitnessFunction.class);

        // Setup RandomSearch instance with mock objects
        randomSearch = new RandomSearch<>(stoppingCondition, sizeFF, coverageFF, 10, new Random());
    }

    @Test
    void testFindSolution() {
        Mockito.when(stoppingCondition.searchMustStop())
                .thenReturn(false)  
                .thenReturn(false)  
                .thenReturn(true); 
                
        Mockito.when(sizeFF.applyAsDouble(Mockito.any(BiChromosome.class)))
                .thenReturn(1.0); 
        Mockito.when(coverageFF.applyAsDouble(Mockito.any(BiChromosome.class)))
                .thenReturn(1.0); 
        List<BiChromosome> solution = randomSearch.findSolution();

        assertEquals(2, solution.size(), "The Pareto front should have one solution.");
        Mockito.verify(stoppingCondition, Mockito.times(3)).searchMustStop();
    }

    @Test
    void testDomination() {
        BiChromosome c1 = Mockito.mock(BiChromosome.class);
        BiChromosome c2 = Mockito.mock(BiChromosome.class);

        Mockito.when(sizeFF.applyAsDouble(c1)).thenReturn(1.0);
        Mockito.when(sizeFF.applyAsDouble(c2)).thenReturn(2.0);
        Mockito.when(coverageFF.applyAsDouble(c1)).thenReturn(2.0);
        Mockito.when(coverageFF.applyAsDouble(c2)).thenReturn(1.0);

        RandomSearch<BiChromosome> search = new RandomSearch<>(stoppingCondition, sizeFF, coverageFF, 10, new Random());
        boolean dominates = search.dominates(c1, c2);

        assertTrue(dominates, "c1 should dominate c2 based on the mock fitness functions.");
    }
}
