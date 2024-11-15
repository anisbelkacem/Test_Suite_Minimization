package de.uni_passau.fim.se2.sbse.suite_minimisation.algorithms;


import de.uni_passau.fim.se2.sbse.suite_minimisation.stopping_conditions.StoppingCondition;

/**
 * Represents a strategy to search for an (approximated) solution to a given problem.
 *
 * @param <C> the type of solution encoding for the problem at hand
 */
public interface SearchAlgorithm<C> {

    /**
     * <p>
     * Runs the search algorithm and returns a possible admissible solution of the encoded problem.
     * </p>
     * <p>
     * Note: every run must perform a new search and must be independent of the previous one. In
     * particular, it must be possible to call this method multiple times in a row. Implementors
     * must ensure multiple runs do not interfere each other.
     * </p>
     *
     * @return a solution
     */
    C findSolution();

    /**
     * Returns the stopping condition this algorithm uses.
     *
     * @return the stopping condition
     */
    StoppingCondition getStoppingCondition();


    /**
     * Tells whether the search algorithm must stop.
     *
     * @return {@code true} if the search must stop, {@code false} otherwise
     * @implNote an alias for {@code getStoppingCondition().searchMustStop()}
     */
    default boolean searchMustStop() {
        return getStoppingCondition().searchMustStop();
    }

    /**
     * Returns how much search budget has already been consumed by the search.
     *
     * @return the amount of search budget consumed
     * @implNote an alias for {@code getStoppingCondition().getProgress()}
     */
    default double getProgress() {
        return getStoppingCondition().getProgress();
    }

    /**
     * Notifies the stopping condition that the search has started.
     *
     * @implNote an alias for {@code getStoppingCondition().notifySearchStarted()}
     */
    default void notifySearchStarted() {
        getStoppingCondition().notifySearchStarted();
    }

    /**
     * Notifies the stopping condition that a fitness evaluation took place.
     *
     * @implNote an alias for {@code getStoppingCondition().notifyFitnessEvaluation()}
     */
    default void notifyFitnessEvaluation() {
        getStoppingCondition().notifyFitnessEvaluation();
    }

    /**
     * Notifies the stopping condition that a number of fitness evaluations took place.
     *
     * @param evaluations the number of evaluations, must not be negative
     * @throws IllegalArgumentException if the given number of evaluations is negative
     * @implNote an alias for {@code getStoppingCondition().notifyFitnessEvaluation()}
     */
    default void notifyFitnessEvaluation(final int evaluations) throws IllegalArgumentException {
        getStoppingCondition().notifyFitnessEvaluations(evaluations);
    }

}
