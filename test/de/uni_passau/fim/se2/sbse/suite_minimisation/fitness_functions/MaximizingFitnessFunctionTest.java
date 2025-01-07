package de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions;

import org.junit.jupiter.api.Test;
import java.util.function.DoubleUnaryOperator;

import static org.junit.jupiter.api.Assertions.*;

public class MaximizingFitnessFunctionTest {

    @Test
    public void testIsMinimizing() {
        MaximizingFitnessFunction<Object> function = obj -> 42.0;
        assertFalse(function.isMinimizing(), "isMinimizing() should always return false.");
    }

    @Test
    public void testAndThenAsDouble() {
        MaximizingFitnessFunction<Object> baseFunction = obj -> 5.0;
        DoubleUnaryOperator doubleValue = value -> value * 2;
        MaximizingFitnessFunction<Object> transformedFunction = baseFunction.andThenAsDouble(doubleValue);
        assertEquals(10.0, transformedFunction.applyAsDouble(new Object()), 
            "The transformed function should double the fitness value.");
    }

    @Test
    public void testApplyAsDouble() {
        MaximizingFitnessFunction<Object> function = obj -> 100.0;
        assertEquals(100.0, function.applyAsDouble(new Object()), 
            "applyAsDouble should return the fitness value.");
    }
}
