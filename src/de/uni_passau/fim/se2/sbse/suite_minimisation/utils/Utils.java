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
            final FitnessFunction f1, // Maximization function (coverage)
            final FitnessFunction f2, // Minimization function (size)
            final double r1, // Reference point for f1
            final double r2) // Reference point for f2
            throws IllegalArgumentException {

        Collections.sort(front, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                double value1 = f1.applyAsDouble(o1);
                double value2 = f1.applyAsDouble(o2);
                return Double.compare(value1, value2);
            }
        });

        double hyperVolume = 0.0;
        double lastY = r2; 

        for (Object solution : front) {
            if (solution == null) {
                continue; 
            }
            double x = f1.applyAsDouble(solution); // Maximization value (coverage)
            double y = f2.applyAsDouble(solution); // Minimization value (size)

            if (y < lastY) { 
                hyperVolume += (x - r1) * (lastY - y);
                lastY = y;
            }
        }
        return hyperVolume;
    }
}