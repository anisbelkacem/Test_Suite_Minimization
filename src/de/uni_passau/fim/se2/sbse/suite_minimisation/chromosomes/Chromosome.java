package de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_minimisation.crossover.Crossover;
import de.uni_passau.fim.se2.sbse.suite_minimisation.mutation.Mutation;
import de.uni_passau.fim.se2.sbse.suite_minimisation.utils.Pair;
import de.uni_passau.fim.se2.sbse.suite_minimisation.utils.SelfTyped;

import java.util.Objects;

/**
 * Chromosomes define a gene representation (genotype) for valid solutions to a given optimization
 * problem and can be mutated & crossed over to produce offspring.
 *
 * @param <C> the type of chromosomes produced as offspring by mutation and crossover
 * @author Sebastian Schweikl
 * @apiNote Usually, it is desired that chromosomes of type {@code C} produce offspring of the same
 * type {@code C}. This requirement can be enforced at compile time by specifying a recursive type
 * parameter, here: {@code C extends Chromosome<C>}.
 */
public abstract class Chromosome<C extends Chromosome<C>> implements SelfTyped<C> {

    /**
     * The mutation operator telling how to mutate a chromosome of the current type.
     */
    private final Mutation<C> mutation;

    /**
     * The crossover operator defining how to pair two chromosomes of the current type.
     */
    private final Crossover<C> crossover;

    /**
     * Constructs a new chromosome, using the given mutation and crossover operators for offspring
     * creation.
     *
     * @param mutation  a strategy that tells how to perform mutation, not {@code null}
     * @param crossover a strategy that tells how to perform crossover, not {@code null}
     * @throws NullPointerException if an argument is {@code null}
     */
    protected Chromosome(final Mutation<C> mutation, final Crossover<C> crossover)
            throws NullPointerException {
        this.mutation = Objects.requireNonNull(mutation);
        this.crossover = Objects.requireNonNull(crossover);
    }

    /**
     * Constructs a new chromosome, using the {@link Mutation#identity() identity mutation} and
     * {@link Crossover#identity() identity crossover} operators for offspring creation.
     *
     * @apiNote This constructor primarily intended for use during unit testing, e.g., when aspects
     * of an algorithm are tested that do not rely on a particular mutation or crossover operator.
     */
    protected Chromosome() {
        this(Mutation.identity(), Crossover.identity());
    }

    /**
     * Creates a copy of this chromosome that uses the same mutation and crossover operators as the
     * given {@code other} chromosome.
     *
     * @param other the chromosome to copy
     * @throws NullPointerException if the given chromosome is {@code null}
     * @apiNote Can be called by copy constructors of implementing subclasses.
     */
    protected Chromosome(final C other) throws NullPointerException {
        Objects.requireNonNull(other);
        this.mutation = other.getMutation();
        this.crossover = other.getCrossover();
    }

    /**
     * Returns the mutation operator used by this chromosome.
     *
     * @return the mutation operator
     */
    public Mutation<C> getMutation() {
        return mutation;
    }

    /**
     * Returns the crossover operator used by this chromosome.
     *
     * @return the crossover operator
     */
    public Crossover<C> getCrossover() {
        return crossover;
    }

    /**
     * Applies the mutation operator to this chromosome and returns the resulting offspring.
     *
     * @return the mutated chromosome
     * @apiNote Intended as syntactic sugar for {@link Mutation#apply}.
     */
    public final C mutate() {
        return mutation.apply(self());
    }

    /**
     * Applies the crossover operator to this chromosome and the given other given chromosome and
     * returns the resulting offspring.
     *
     * @param other the chromosome with which to pair, not {@code null}
     * @return the offspring
     * @throws NullPointerException if {@code other} is {@code null}
     * @apiNote Intended as syntactic sugar for {@link Crossover#apply}.
     */
    public final Pair<C> crossover(final C other) {
        Objects.requireNonNull(other);
        return crossover.apply(self(), other);
    }

    /**
     * Creates a copy of this chromosome. Implementors should clearly indicate whether a shallow or
     * deep copy is made.
     *
     * @return a copy of this chromosome
     */
    public abstract C copy();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean equals(final Object other); // enforce custom implementation

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int hashCode(); // enforce custom implementation
}