package de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;

import java.util.List;


public class CoverageFitnessFunction implements MaximizingFitnessFunction<BiChromosome> {

    private final boolean[][] coverageMatrix; 

    /**
     * Constructor for CoverageFitnessFunction.
     *
     * @param coverageMatrix the boolean coverage matrix where rows represent test cases
     *                       and columns represent lines of code covered.
     */
    public CoverageFitnessFunction(boolean[][] coverageMatrix) {
        this.coverageMatrix = coverageMatrix;
    }

    @Override
    public double applyAsDouble(BiChromosome chromosome) {
        List<Integer> activeTestCases = chromosome.getActiveTestCases();
        int totalLines = coverageMatrix[0].length;
        int coveredLines = 0;

        for (int line = 0; line < totalLines; line++) {
            for (int testCase : activeTestCases) {
                if (coverageMatrix[testCase][line]) {
                    coveredLines++;
                    break; 
                }
            }
        }
        if (totalLines == 0) {
            return 0.0;  
        }
        return (double) coveredLines / totalLines; 
    }
}

