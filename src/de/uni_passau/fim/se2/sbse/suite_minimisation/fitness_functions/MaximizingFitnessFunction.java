package de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions;

import java.util.function.DoubleUnaryOperator;

/**
 * Represents a maximizing fitness function, which awards higher values to better solutions.
 *
 * @param <C> the type of configuration rated by this function
 * @author Sebastian Schweikl
 */
@FunctionalInterface
public interface MaximizingFitnessFunction<C> extends FitnessFunction<C> {

    /**
     * Always returns {@code false} as this is a maximizing fitness function.
     *
     * @return always {@code false}
     */
    @Override
    default boolean isMinimizing() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default MaximizingFitnessFunction<C> andThenAsDouble(final DoubleUnaryOperator after) {
        return (MaximizingFitnessFunction<C>) FitnessFunction.super.andThenAsDouble(after);
    }
}
