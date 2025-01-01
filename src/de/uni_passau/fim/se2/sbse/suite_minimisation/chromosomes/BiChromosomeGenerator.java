package de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_minimisation.crossover.Crossover;
import de.uni_passau.fim.se2.sbse.suite_minimisation.mutation.Mutation;

public class BiChromosomeGenerator implements ChromosomeGenerator<BiChromosome> {

    private final Mutation<BiChromosome> mutation;
    private final Crossover<BiChromosome> crossover;
    private final int size;

    public BiChromosomeGenerator(int size,Mutation<BiChromosome> mutation, Crossover<BiChromosome> crossover) {
        this.mutation = mutation;
        this.crossover = crossover;
        this.size = size;
    }

    @Override
    public BiChromosome get() {
        return BiChromosome.generateRandomChromosome(size ,mutation, crossover);
    }
}
