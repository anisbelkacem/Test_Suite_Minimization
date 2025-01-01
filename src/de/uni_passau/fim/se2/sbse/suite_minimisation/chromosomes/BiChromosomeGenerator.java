package de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_minimisation.crossover.Crossover;
import de.uni_passau.fim.se2.sbse.suite_minimisation.mutation.Mutation;

public class BiChromosomeGenerator implements ChromosomeGenerator<BiChromosome> {

    private final Mutation<BiChromosome> mutation;
    private final Crossover<BiChromosome> crossover;

    public BiChromosomeGenerator(Mutation<BiChromosome> mutation, Crossover<BiChromosome> crossover) {
        this.mutation = mutation;
        this.crossover = crossover;
    }

    @Override
    public BiChromosome get() {
        return BiChromosome.generateRandomChromosome(mutation, crossover);
    }
}
