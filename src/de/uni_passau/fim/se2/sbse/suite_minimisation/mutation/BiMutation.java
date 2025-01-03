package de.uni_passau.fim.se2.sbse.suite_minimisation.mutation;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;

import java.util.Random;

public class BiMutation implements Mutation<BiChromosome> {

    private final double mutationRate; // Probability of mutating a bit

    /**
     * Constructor to set the mutation rate.
     *
     * @param mutationRate the probability of mutating each bit (0 <= mutationRate <= 1)
     */
    public BiMutation(double mutationRate) {
        if (mutationRate < 0 || mutationRate > 1) {
            throw new IllegalArgumentException("Mutation rate must be between 0 and 1.");
        }
        this.mutationRate = mutationRate;
    }

    /**
     * Applies mutation to the given binary chromosome. Each bit has a chance to be flipped based
     * on the mutation rate.
     *
     * @param parent the parent chromosome to mutate
     * @return the mutated chromosome
     */
    @Override
    public BiChromosome apply( BiChromosome parent) {
        BiChromosome offspring = parent.copy();
        Random random = new Random();
        for (int i = 0; i < offspring.getTestCases().size(); i++) {
            if (random.nextDouble() < (offspring.getActiveTestCases().size()/offspring.getTestCases().size())) {
                
                int currentBit = offspring.getTestCases().get(i);
                offspring.getTestCases().set(i, currentBit == 1 ? 0 : 1);
            }
        } 
        if (offspring.getActiveTestCases().isEmpty()) {
            
            int randomIndex = random.nextInt(offspring.getTestCases().size());
            offspring.getTestCases().set(randomIndex, 1);
        }
        return offspring;
    }

    @Override
    public String toString() {
        return "Binary mutation with rate: " + mutationRate;
    }
}
