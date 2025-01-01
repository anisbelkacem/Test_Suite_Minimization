package de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_minimisation.crossover.Crossover;
import de.uni_passau.fim.se2.sbse.suite_minimisation.mutation.Mutation;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BiChromosome extends Chromosome<BiChromosome> {

    private final List<Integer> testCases; 

    public BiChromosome(List<Integer> testCases, Mutation<BiChromosome> mutation, Crossover<BiChromosome> crossover) {
        super(mutation, crossover);
        this.testCases = new ArrayList<>(testCases);
    }

    public BiChromosome(BiChromosome other) {
        super(other);
        this.testCases = new ArrayList<>(other.testCases);
    }

    public static BiChromosome generateRandomChromosome(Mutation<BiChromosome> mutation, Crossover<BiChromosome> crossover) {
        Random random = new Random();
        //int randomSize = random.nextInt(); 
        List<Integer> testCases = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            testCases.add(random.nextBoolean() ? 1 : 0);
        }

        return new BiChromosome(testCases, mutation, crossover);
    }

    public List<Integer> getActiveTestCases() {
        List<Integer> activeTestCases = new ArrayList<>();
        for (int i = 0; i < (int)testCases.size(); i++) {
            if (testCases.get(i) == 1) {
                activeTestCases.add(i);
            }
        }
        return activeTestCases;
    }

    public List<Integer> getTestCases() {
        return testCases;
    }

    @Override
    public BiChromosome copy() {
        return new BiChromosome(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BiChromosome that = (BiChromosome) obj;
        return testCases.equals(that.testCases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testCases);
    }

    @Override
    public String toString() {
        return testCases.toString();
    }

    @Override
    public BiChromosome self() {
        return this ;
    }
}
