package de.uni_passau.fim.se2.sbse.suite_minimisation.crossover;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;
import de.uni_passau.fim.se2.sbse.suite_minimisation.utils.Pair;

import java.util.Random;

public class BiCrossover implements Crossover<BiChromosome> {

    private final double crossoverRate; 

    /**
     * Constructor to set the crossover rate.
     *
     * @param crossoverRate the probability of performing crossover (0 <= crossoverRate <= 1)
     */
    public BiCrossover(double crossoverRate) {
        if (crossoverRate < 0 || crossoverRate > 1) {
            throw new IllegalArgumentException("Crossover rate must be between 0 and 1.");
        }
        this.crossoverRate = crossoverRate;
    }

    /**
     * Applies one-point crossover to two parent chromosomes to produce two offspring.
     *
     * @param parent1 the first parent chromosome
     * @param parent2 the second parent chromosome
     * @return a pair of offspring chromosomes
     */
    @Override
    public Pair<BiChromosome> apply(final BiChromosome parent1, final BiChromosome parent2) {
        Random random = new Random();
        if (random.nextDouble() > crossoverRate) {
            return Pair.of(parent1.copy(), parent2.copy());
        }

        int geneLength = parent1.getTestCases().size();
        int crossoverPoint = random.nextInt(geneLength); 

        BiChromosome offspring1 = parent1.copy();
        BiChromosome offspring2 = parent2.copy();

        for (int i = crossoverPoint; i < geneLength; i++) {
            int tempGene = offspring1.getTestCases().get(i);
            offspring1.getTestCases().set(i, offspring2.getTestCases().get(i));
            offspring2.getTestCases().set(i, tempGene);
        }
        return Pair.of(offspring1, offspring2);
    }

    @Override
    public String toString() {
        return "Binary crossover with rate: " + crossoverRate;
    }
}
