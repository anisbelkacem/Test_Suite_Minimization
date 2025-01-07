package de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions;

import org.junit.jupiter.api.Test;
import java.util.function.DoubleUnaryOperator;

import static org.junit.jupiter.api.Assertions.*;

public class MinimizingFitnessFunctionTest {

    @Test
    public void testIsMinimizing() {
        MinimizingFitnessFunction<Object> function = obj -> 42.0;
        assertTrue(function.isMinimizing(), "isMinimizing() should always return true.");
    }

    @Test
    public void testAndThenAsDouble() {
        MinimizingFitnessFunction<Object> baseFunction = obj -> 5.0;
        DoubleUnaryOperator halveValue = value -> value / 2;
        MinimizingFitnessFunction<Object> transformedFunction = baseFunction.andThenAsDouble(halveValue);
        assertEquals(2.5, transformedFunction.applyAsDouble(new Object()), 
            "The transformed function should halve the fitness value.");
    }

    @Test
    public void testApplyAsDouble() {
        MinimizingFitnessFunction<Object> function = obj -> 100.0;
        assertEquals(100.0, function.applyAsDouble(new Object()), 
            "applyAsDouble should return the fitness value.");
    }
}
