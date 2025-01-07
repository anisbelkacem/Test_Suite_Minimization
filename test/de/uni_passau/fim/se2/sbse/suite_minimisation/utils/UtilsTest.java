package de.uni_passau.fim.se2.sbse.suite_minimisation.utils;

import de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions.FitnessFunction;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UtilsTest {

    @Test
    public void testEmptyFront() {
        double result = Utils.computeHyperVolume(
            new ArrayList<>(),
            mock(FitnessFunction.class),
            mock(FitnessFunction.class),
            1.0,
            1.0
        );
        assertEquals(0.0, result, "Hypervolume should be 0 for an empty front.");
    }

    @Test
    public void testNullFront() {
        double result = Utils.computeHyperVolume(
            null,
            mock(FitnessFunction.class),
            mock(FitnessFunction.class),
            1.0,
            1.0
        );
        assertEquals(0.0, result, "Hypervolume should be 0 for a null front.");
    }

    @Test
    public void testSinglePointFront() {
        FitnessFunction mockF1 = mock(FitnessFunction.class);
        FitnessFunction mockF2 = mock(FitnessFunction.class);
        Object point = new Object();

        when(mockF1.applyAsDouble(point)).thenReturn(0.5);
        when(mockF2.applyAsDouble(point)).thenReturn(0.5);

        List<Object> front = List.of(point);
        double result = Utils.computeHyperVolume(front, mockF1, mockF2, 1.0, 1.0);
        assertEquals(0.25, result, 1e-9, "Hypervolume for a single point should be correctly calculated.");
    }

    @Test
    public void testMultiplePoints() {
        FitnessFunction mockF1 = mock(FitnessFunction.class);
        FitnessFunction mockF2 = mock(FitnessFunction.class);

        Object point1 = new Object();
        Object point2 = new Object();

        when(mockF1.applyAsDouble(point1)).thenReturn(0.2);
        when(mockF2.applyAsDouble(point1)).thenReturn(0.8);
        when(mockF1.applyAsDouble(point2)).thenReturn(0.6);
        when(mockF2.applyAsDouble(point2)).thenReturn(0.4);

        List<Object> front = List.of(point1, point2);
        double result = Utils.computeHyperVolume(front, mockF1, mockF2, 1.0, 1.0);
        assertEquals(0.28, result, 1e-9, "Hypervolume for multiple points should be correctly calculated.");
    }
    @Test
    public void testsort() {
        FitnessFunction mockF1 = mock(FitnessFunction.class);
        FitnessFunction mockF2 = mock(FitnessFunction.class);

        Object point1 = new Object();
        Object point2 = new Object();

        when(mockF1.applyAsDouble(point1)).thenReturn(0.2);
        when(mockF2.applyAsDouble(point1)).thenReturn(0.8);
        when(mockF1.applyAsDouble(point2)).thenReturn(0.2);
        when(mockF2.applyAsDouble(point2)).thenReturn(0.4);

        List<Object> front = List.of(point1, point2);
        double result = Utils.computeHyperVolume(front, mockF1, mockF2, 1.0, 1.0);
        assertEquals(0.04, result, 1e-9, "Hypervolume for multiple points should be correctly calculated.");
    }

    @Test
    public void testPointsOutsideRange() {
        FitnessFunction mockF1 = mock(FitnessFunction.class);
        FitnessFunction mockF2 = mock(FitnessFunction.class);

        Object point1 = new Object();
        Object point2 = new Object();

        when(mockF1.applyAsDouble(point1)).thenReturn(1.5); 
        when(mockF2.applyAsDouble(point1)).thenReturn(2.5);
        when(mockF1.applyAsDouble(point2)).thenReturn(-3.5);
        when(mockF2.applyAsDouble(point2)).thenReturn(-0.5); 

        List<Object> front = List.of(point1, point2);
        double result = Utils.computeHyperVolume(front, mockF1, mockF2, 1.0, 1.0);
        assertEquals(0.0, result, "Points outside the range should be ignored.");
    }

    @Test
    public void testDescendingOrderPoints() {
        FitnessFunction mockF1 = mock(FitnessFunction.class);
        FitnessFunction mockF2 = mock(FitnessFunction.class);

        Object point1 = new Object();
        Object point2 = new Object();

        when(mockF1.applyAsDouble(point1)).thenReturn(0.7);
        when(mockF2.applyAsDouble(point1)).thenReturn(0.3);
        when(mockF1.applyAsDouble(point2)).thenReturn(0.3);
        when(mockF2.applyAsDouble(point2)).thenReturn(0.7);

        List<Object> front = List.of(point1, point2);
        double result = Utils.computeHyperVolume(front, mockF1, mockF2, 1.0, 1.0);
        assertEquals(0.37, result, 1e-9, "The method should handle unordered points correctly after sorting.");
    }
}
