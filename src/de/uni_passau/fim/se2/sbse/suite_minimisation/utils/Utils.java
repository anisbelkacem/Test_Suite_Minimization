package de.uni_passau.fim.se2.sbse.suite_minimisation.utils;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.Chromosome;
import de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions.CoverageFitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions.TestSuiteSizeFitnessFunction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Utils {

    /**
     * Parses a coverage matrix from a string.
     *
     * @param matrixFile the string representation of the coverage matrix
     * @return the parsed coverage matrix
     * @throws IOException if the supplied file could not be read
     */
    public static boolean[][] parseCoverageMatrix(File matrixFile) throws IOException {
        String matrix = Files.readString(matrixFile.toPath()).replace("\n", " ");

        // Remove outer brackets
        matrix = matrix.substring(1, matrix.length() - 1);

        // Split rows by "], ["
        String[] rows = matrix.split("], \\[");

        // Initialize 2D boolean array
        boolean[][] parsedMatrix = new boolean[rows.length][];

        for (int i = 0; i < rows.length; i++) {
            // Remove any remaining brackets and split by comma
            String[] values = rows[i].replace("[", "").replace("]", "").split(", ");
            parsedMatrix[i] = new boolean[values.length];
            for (int j = 0; j < values.length; j++) {
                // Parse "true" or "false" as boolean
                parsedMatrix[i][j] = Boolean.parseBoolean(values[j].trim());
            }
        }

        return parsedMatrix;
    }

    /**
     * Computes the hyper-volume of the given Pareto {@code front}, using the given fitness
     * functions {@code f1} and {@code f2}, and {@code r1} and {@code r2} as coordinates of the
     * reference point. The fitness functions must produce normalized results between 0 and 1.
     *
     * @param front the front for which to compute the hyper-volume
     * @param f1    the first fitness function
     * @param f2    the second fitness function
     * @param r1    reference coordinate for {@code f1}
     * @param r2    reference coordinate for {@code f2}
     * @return the hyper volume of the given front w.r.t. the reference point
     * @apiNote The function uses ugly raw types because it seems the type system doesn't want to
     * let me express this in any other way :(
     * @implSpec In the implementation of this method you might need to cast or use raw types, too.
     */

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static double computeHyperVolume(
            final List front,
            final FitnessFunction f1,
            final FitnessFunction f2,
            final double r1,
            final double r2) throws IllegalArgumentException {
    
        if (front == null || front.isEmpty()) {
            return 0;
        }
        List<Chromosome> sortedFront = new ArrayList<>(front);
        sortedFront.sort((c1, c2) -> {
            double f2_1 = f2.applyAsDouble(c1);
            double f2_2 = f2.applyAsDouble(c2);
            if (f2_1 != f2_2) {
                return Double.compare(f2_2, f2_1); 
            }
            double f1_1 = f1.applyAsDouble(c1);
            double f1_2 = f1.applyAsDouble(c2);
            return Double.compare(f1_2, f1_1); 
        });
    
        double hypervolume = 0.0;
        double lastF1 = r1;
    
        for (Chromosome c : sortedFront) {
            double currentF1 = f1.applyAsDouble(c);
            double currentF2 = f2.applyAsDouble(c);
    
            double width = lastF1 - currentF1;
            double height = currentF2 - r2;
    
        hypervolume += Math.abs(width) * Math.abs(height);
    
            lastF1 = currentF1; 
        }
        return hypervolume;
    }
     
     
     
}