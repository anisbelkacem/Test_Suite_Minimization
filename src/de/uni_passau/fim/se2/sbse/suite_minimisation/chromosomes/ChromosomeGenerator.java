package de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes;

import java.util.function.Supplier;

/**
 * A generator for random chromosomes.
 *
 * @param <C> the type of chromosomes this generator is able to produce
 * @author Sebastian Schweikl
 */
@FunctionalInterface
public interface ChromosomeGenerator<C extends Chromosome<C>> extends Supplier<C> {

    /**
     * Creates and returns a random chromosome. Implementations must ensure that the returned
     * chromosome represents a valid and admissible solution for the problem at hand.
     *
     * @return a random chromosome
     */
    @Override
    C get();
}
