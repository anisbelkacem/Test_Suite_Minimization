package de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;

import java.util.List;


public class CoverageFitnessFunction implements MaximizingFitnessFunction<BiChromosome> {

    private final boolean[][] coverageMatrix; 
    private final int numberLines;

    /**
     * Constructor for CoverageFitnessFunction.
     *
     * @param coverageMatrix the boolean coverage matrix where rows represent test cases
     *                       and columns represent lines of code covered.
     */
    public CoverageFitnessFunction(boolean[][] coverageMatrix, int numberLines) {
        this.coverageMatrix = coverageMatrix;
        this.numberLines = numberLines;
    }

    @Override
    public double applyAsDouble(BiChromosome chromosome) {
        List<Integer> activeTestCases = chromosome.getActiveTestCases();
        int totalLines = numberLines;
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

