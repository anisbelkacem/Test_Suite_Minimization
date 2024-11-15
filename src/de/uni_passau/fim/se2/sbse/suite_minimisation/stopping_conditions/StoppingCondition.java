package de.uni_passau.fim.se2.sbse.suite_minimisation.stopping_conditions;

/**
 * A stopping condition defines the budget allocated to a search algorithm. If the budget is exhausted, the search must stop.
 * Some algorithms may choose to stop earlier, e.g., when the best solution has been found or solutions are no longer improving.
 * <p>
 * Stopping conditions are realised as observers that are registered to a search algorithm.
 * The search algorithm can then notify the registered observers of certain events, such as fitness evaluations.
 * Search algorithms can query their stopping conditions to determine whether the search can continue.
 */
public interface StoppingCondition {

    /**
     * Notifies this stopping condition that the search has started.
     * This method is intended to be called by the search algorithm to which the stopping condition is subscribed.
     */
    void notifySearchStarted();

    /**
     * Notifies this stopping condition that a fitness evaluation has taken place.
     * This method is intended to be called by the search algorithm to which the stopping condition is subscribed.
     */
    void notifyFitnessEvaluation();

     /**
     * Notifies this stopping condition that a number of fitness evaluations took place. Intended to
     * be called by the search algorithm the stopping condition is subscribed to.
     *
     * @param evaluations the number of evaluations, must not be negative
     * @throws IllegalArgumentException if the given number of evaluations is negative
     */
    default void notifyFitnessEvaluations(final int evaluations) throws IllegalArgumentException {
        if (evaluations < 0) {
            throw new IllegalArgumentException("Negative number of evaluations: " + evaluations);
        }

        for (int i = 0; i < evaluations; i++) {
            notifyFitnessEvaluation();
        }
    }

    /**
     * Tells the search algorithm whether it has exhausted its search budget and has to stop.
     *
     * @return {@code true} if the search must stop, {@code false} otherwise
     */
    boolean searchMustStop();

    /**
     * Computes the fraction of the search budget that has already been consumed, i.e. a value in the interval [0, 1]
     * with 0 representing the start of the search and 1 a fully exhausted search budget.
     *
     * @return the fraction of the search budget that has already been consumed
     */
    double getProgress();
}

