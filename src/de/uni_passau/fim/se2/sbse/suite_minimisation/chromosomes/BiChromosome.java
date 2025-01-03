package de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_minimisation.crossover.BiCrossover;
import de.uni_passau.fim.se2.sbse.suite_minimisation.crossover.Crossover;
import de.uni_passau.fim.se2.sbse.suite_minimisation.mutation.BiMutation;
import de.uni_passau.fim.se2.sbse.suite_minimisation.mutation.Mutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BiChromosome extends Chromosome<BiChromosome> {

    private final List<Integer> testCases; 

    public BiChromosome(List<Integer> testCases, BiMutation mutation, BiCrossover crossover) {
        super(mutation, crossover);
        this.testCases = new ArrayList<>(testCases);
    }

    public BiChromosome(BiChromosome other) {
        super(other);
        this.testCases = new ArrayList<>(other.testCases);
    }

    public static BiChromosome generateRandomChromosome(int size, BiMutation mutation, BiCrossover crossover) {
    Random random = new Random();
    List<Integer> testCases = new ArrayList<>();

    int numberOfOnes = 1 + random.nextInt(size - 1);

    for (int i = 0; i < numberOfOnes; i++) {
        testCases.add(1);
    }
    for (int i = numberOfOnes; i < size; i++) {
        testCases.add(0);
    }

    
    for (int i = testCases.size() - 1; i > 0; i--) {
        int j = random.nextInt(i + 1);
        int temp = testCases.get(i);
        testCases.set(i, testCases.get(j));
        testCases.set(j, temp);
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
