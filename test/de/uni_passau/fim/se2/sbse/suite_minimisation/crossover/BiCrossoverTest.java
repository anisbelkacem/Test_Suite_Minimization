package de.uni_passau.fim.se2.sbse.suite_minimisation.crossover;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;
import de.uni_passau.fim.se2.sbse.suite_minimisation.mutation.BiMutation;
import de.uni_passau.fim.se2.sbse.suite_minimisation.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BiCrossoverTest {

    private BiChromosome parent1;
    private BiChromosome parent2;
    private BiMutation mutation;
    private BiCrossover crossover;

    @BeforeEach
    void setUp() {
        mutation = mock(BiMutation.class);
        crossover = mock(BiCrossover.class);

        parent1 = mock(BiChromosome.class);
        parent2 = mock(BiChromosome.class);

        when(parent1.getTestCases()).thenReturn(Arrays.asList(1, 0, 1, 0, 1));
        when(parent2.getTestCases()).thenReturn(Arrays.asList(0, 1, 0, 1, 0));

        when(parent1.copy()).thenReturn(new BiChromosome(Arrays.asList(1, 0, 1, 0, 1), mutation, crossover));
        when(parent2.copy()).thenReturn(new BiChromosome(Arrays.asList(0, 1, 0, 1, 0), mutation, crossover));

    }

    @Test
    void constructor_validatesCrossoverRate() {
        assertDoesNotThrow(() -> new BiCrossover(0.5), "Valid crossover rate should not throw an exception");
        assertThrows(IllegalArgumentException.class, () -> new BiCrossover(-0.1), "Negative crossover rate should throw exception");
        assertThrows(IllegalArgumentException.class, () -> new BiCrossover(1.1), "Crossover rate above 1 should throw exception");
    }

    @Test
    void apply_noCrossoverIfRateIsZero() {
        BiCrossover crossover = new BiCrossover(0.0);
        Pair<BiChromosome> offspring = crossover.apply(parent1, parent2);

       
        assertEquals(parent1.getTestCases(), offspring.getFst().getTestCases(), "Offspring1 should match Parent1 when crossover rate is 0");
        assertEquals(parent2.getTestCases(), offspring.getSnd().getTestCases(), "Offspring2 should match Parent2 when crossover rate is 0");
    }

    @Test
    void apply_performsCrossover() {
        BiCrossover crossover = new BiCrossover(1.0);
        Pair<BiChromosome> offspring = crossover.apply(parent1, parent2);

        
        assertNotEquals(parent1.getTestCases(), offspring.getFst().getTestCases(), "Offspring1 should differ from Parent1 after crossover");
        assertNotEquals(parent2.getTestCases(), offspring.getSnd().getTestCases(), "Offspring2 should differ from Parent2 after crossover");
    }

    @Test
    void toString_returnsCorrectRepresentation() {
        BiCrossover crossover = new BiCrossover(0.75);

        assertEquals("Binary crossover with rate: 0.75", crossover.toString(), "toString should return the correct description");
    }
}
