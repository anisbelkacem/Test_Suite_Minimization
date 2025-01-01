package de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;

public class TestSuiteSizeFitnessFunction implements MinimizingFitnessFunction<BiChromosome> {

    /**
     * Computes the fitness value based on the size of the test suite.
     *
     * @param chromosome the binary chromosome representing the test suite
     * @return the normalized size of the test suite (fraction of active test cases)
     */
    @Override
    public double applyAsDouble(BiChromosome chromosome) {
        int activeTestCases = chromosome.getActiveTestCases().size();
        int totalTestCases = chromosome.getTestCases().size();
        if (totalTestCases == 0) {
            return 0.0;  
        }
        return (double) activeTestCases / totalTestCases; 
    }
}
