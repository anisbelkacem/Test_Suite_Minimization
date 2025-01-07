package de.uni_passau.fim.se2.sbse.suite_minimisation.selection;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;
import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.Chromosome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BinaryTournamentSelectionTest {

    private Random random;
    private Comparator<ChromosomeMock> comparator;
    private BinaryTournamentSelection<ChromosomeMock> selection;

    @BeforeEach
    void setUp() {
        random = mock(Random.class);
        comparator = Comparator.comparingInt(ChromosomeMock::getFitness);
        selection = new BinaryTournamentSelection<>(comparator, random);
    }

    @Test
    void testApplyWithValidPopulation() {
        ChromosomeMock chromosome1 = new ChromosomeMock(10);
        ChromosomeMock chromosome2 = new ChromosomeMock(20);
        List<ChromosomeMock> population = List.of(chromosome1, chromosome2);

        when(random.nextInt(2)).thenReturn(0, 1);

        ChromosomeMock result = selection.apply(population);
        assertEquals(chromosome2, result, "The chromosome with the higher fitness should be selected.");
    }

    @Test
    void testApplyWithDuplicateRandomSelection() {
        ChromosomeMock chromosome1 = new ChromosomeMock(10);
        ChromosomeMock chromosome2 = new ChromosomeMock(20);
        List<ChromosomeMock> population = List.of(chromosome1, chromosome2);

        when(random.nextInt(2)).thenReturn(0, 0, 1); // First two calls are the same; third is different.

        ChromosomeMock result = selection.apply(population);
        assertEquals(chromosome2, result, "The chromosome with the higher fitness should be selected.");
    }

    @Test
    void testApplyWithSingleElementPopulation() {
        ChromosomeMock chromosome = new ChromosomeMock(10);
        List<ChromosomeMock> population = List.of(chromosome);

        ChromosomeMock result = selection.apply(population);
        assertEquals(chromosome, result, "The only chromosome in the population should be selected.");
    }

    @Test
    void testApplyWithEmptyPopulation() {
        List<ChromosomeMock> population = new ArrayList<>();
        assertThrows(NoSuchElementException.class, () -> selection.apply(population),
                "An empty population should throw a NoSuchElementException.");
    }

    @Test
    void testApplyWithNullPopulation() {
        assertThrows(NullPointerException.class, () -> selection.apply(null),
                "A null population should throw a NullPointerException.");
    }

    @Test
    void testConstructorWithNullRandom() {
        assertThrows(NullPointerException.class, () -> new BinaryTournamentSelection<>(comparator, null),
                "A null random source should throw a NullPointerException.");
    }

    // Mock implementation of Chromosome for testing
    private static class ChromosomeMock extends Chromosome<ChromosomeMock> {
        private final int fitness;
    
        ChromosomeMock(int fitness) {
            this.fitness = fitness;
        }
    
        int getFitness() {
            return fitness;
        }
    
        @Override
        public ChromosomeMock copy() {
            return new ChromosomeMock(fitness);
        }
    
        @Override
        public ChromosomeMock self() {
            return this;
        }
    
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ChromosomeMock that = (ChromosomeMock) obj;
            return fitness == that.fitness;
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(fitness);
        }
    }
    
}
