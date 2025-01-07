package de.uni_passau.fim.se2.sbse.suite_minimisation.mutation;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;
import de.uni_passau.fim.se2.sbse.suite_minimisation.crossover.BiCrossover;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BiMutationTest {

    private BiMutation mutation;
    private BiCrossover crossover;
    private BiChromosome chromosome;

    @BeforeEach
    void setUp() {
        mutation = mock(BiMutation.class);
        crossover = mock(BiCrossover.class);
        chromosome = mock(BiChromosome.class);
    }

    @Test
    void constructor_validRate() {
        assertDoesNotThrow(() -> new BiMutation(0.5));
        assertDoesNotThrow(() -> new BiMutation(0.0));
        assertDoesNotThrow(() -> new BiMutation(1.0));
    }

    @Test
    void constructor_invalidRate() {
        assertThrows(IllegalArgumentException.class, () -> new BiMutation(-0.1));
        assertThrows(IllegalArgumentException.class, () -> new BiMutation(1.1));
    }

    @Test
    void apply_fullMutation() {
        mutation = new BiMutation(1.0); // Full mutation
        when(chromosome.copy()).thenReturn(new BiChromosome(Arrays.asList(1, 0, 1), mutation, crossover));

        BiChromosome mutatedChromosome = mutation.apply(chromosome);

        assertNotEquals(Arrays.asList(0, 1, 0), mutatedChromosome.getTestCases(),
                "All bits should flip when mutation rate is 1.");
    }

    @Test
    void apply_partialMutation() {
        mutation = new BiMutation(0.5); // Partial mutation
        when(chromosome.copy()).thenReturn(new BiChromosome(Arrays.asList(1, 1, 1), mutation, crossover));

        BiChromosome mutatedChromosome = mutation.apply(chromosome);

        assertEquals(3, mutatedChromosome.getTestCases().size(), "Chromosome size should remain the same.");
        assertTrue(mutatedChromosome.getTestCases().contains(0) || mutatedChromosome.getTestCases().contains(1),
                "Mutation should flip some bits while leaving others unchanged.");
    }

    @Test
    void apply_emptyActiveTestCases() {
        mutation = new BiMutation(0.5);
        BiChromosome chromosomeWithEmptyActive = spy(new BiChromosome(Arrays.asList(0, 0, 0), mutation, crossover));
        doReturn(Arrays.asList()).when(chromosomeWithEmptyActive).getActiveTestCases();

        BiChromosome mutatedChromosome = mutation.apply(chromosomeWithEmptyActive);

        assertNotEquals(Arrays.asList(0, 0, 0), mutatedChromosome.getTestCases(),
                "At least one bit should be flipped when no active test cases exist.");
        assertTrue(mutatedChromosome.getActiveTestCases().size() > 0,
                "There should be at least one active test case after mutation.");
    }

    @Test
    void apply_randomIndexActivation() {
        mutation = new BiMutation(0.0); // No regular mutation
        BiChromosome chromosomeWithEmptyActive = spy(new BiChromosome(Arrays.asList(0, 0, 0, 0), mutation, crossover));
        doReturn(Arrays.asList()).when(chromosomeWithEmptyActive).getActiveTestCases();

        BiChromosome mutatedChromosome = mutation.apply(chromosomeWithEmptyActive);

        assertTrue(mutatedChromosome.getTestCases().contains(1),
                "Mutation should ensure at least one bit is active by flipping a random index.");
    }
    @Test
    void testToString() {
        double mutationRate = 0.5;
        BiMutation mutation = new BiMutation(mutationRate);

        String expected = "Binary mutation with rate: 0.5";
        assertEquals(expected, mutation.toString(), "The toString method should return the correct string representation.");
    }
}
