package de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_minimisation.crossover.BiCrossover;
import de.uni_passau.fim.se2.sbse.suite_minimisation.mutation.BiMutation;

public class BiChromosomeGenerator implements ChromosomeGenerator<BiChromosome> {

    private final BiMutation mutation;
    private final BiCrossover crossover;
    private final int size;

    public BiChromosomeGenerator(int size,BiMutation mutation, BiCrossover crossover) {
        this.mutation = mutation;
        this.crossover = crossover;
        this.size = size;
    }

    @Override
    public BiChromosome get() {
        return BiChromosome.generateRandomChromosome(size ,mutation, crossover);
    }
}
