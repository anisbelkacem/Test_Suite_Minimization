package de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions;

import java.util.function.DoubleUnaryOperator;

/**
 * Represents a minimizing fitness function, which awards lower values to better solutions.
 *
 * @param <C> the type of configuration rated by this function
 * @author Sebastian Schweikl
 */
@FunctionalInterface
public interface MinimizingFitnessFunction<C> extends FitnessFunction<C> {

    /**
     * Always returns {@code true} as this is a minimizing fitness function.
     *
     * @return always {@code true}
     */
    @Override
    default boolean isMinimizing() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default MinimizingFitnessFunction<C> andThenAsDouble(final DoubleUnaryOperator after) {
        return (MinimizingFitnessFunction<C>) FitnessFunction.super.andThenAsDouble(after);
    }
}
