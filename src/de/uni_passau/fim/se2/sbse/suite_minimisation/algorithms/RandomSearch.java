package de.uni_passau.fim.se2.sbse.suite_minimisation.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.Chromosome;
import de.uni_passau.fim.se2.sbse.suite_minimisation.crossover.BiCrossover;
import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;
import de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions.MaximizingFitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions.MinimizingFitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_minimisation.mutation.BiMutation;
import de.uni_passau.fim.se2.sbse.suite_minimisation.stopping_conditions.StoppingCondition;



public class RandomSearch<T extends Chromosome<T>> implements GeneticAlgorithm<T> {

    private final StoppingCondition stoppingCondition;
    private final MinimizingFitnessFunction<T> sizeFF;
    private final MaximizingFitnessFunction<T> coverageFF;
    private final int numberTestCases;
    private final Random random;

    public RandomSearch(
        StoppingCondition stoppingCondition,
        MinimizingFitnessFunction<T> sizeFF,
        MaximizingFitnessFunction<T> coverageFF,
        int numberTestCases,
        Random random
    ) {
        this.stoppingCondition = stoppingCondition;
        this.sizeFF = sizeFF;
        this.coverageFF = coverageFF;
        this.numberTestCases = numberTestCases;
        this.random = random;
    }

    @Override
    public List<T> findSolution() {
        List<T> paretoFront = new ArrayList<>();
        stoppingCondition.notifySearchStarted();
        double MutationRate = random.nextDouble();
        double crossoverRate = random.nextDouble();
        BiMutation mutation = new BiMutation(MutationRate); 
        BiCrossover crossover = new BiCrossover(crossoverRate); 
        
        while (!stoppingCondition.searchMustStop()) {
            T randomChromosome = generateRandomChromosome(numberTestCases, mutation,crossover);
            int change=0;
            updateParetoFront(paretoFront, randomChromosome,change);
            if (change==1) stoppingCondition.notifyFitnessEvaluation();
        }
        for(int i=0;i<paretoFront.size();i++)
        {
            for(int j=0;j<paretoFront.size();j++)
            {
                if (dominates(paretoFront.get(i), paretoFront.get(j))) {
                    throw new NoSuchElementException("there is domination in the pareto front" + i+"  "+j);
                }
            }
        }
        Set<T> finalParetoFront = new HashSet<>(paretoFront);
        paretoFront = new ArrayList<>(finalParetoFront);
        //if(paretoFront.size()!=0)throw new NoSuchElementException("the size" + paretoFront.size());
        return paretoFront;
    }
    @SuppressWarnings("unchecked")
    private T generateRandomChromosome(int size,BiMutation mutation,BiCrossover crossover) {
        return (T) BiChromosome.generateRandomChromosome(size ,mutation, crossover);
    }

    private void updateParetoFront(List<T> paretoFront, T newChromosome,int change) {
  
        for (T chromosome : paretoFront) {
            if (dominates(chromosome, newChromosome)) {
                return; //newchromosome is dominated
            }
        }
        paretoFront.removeIf(chromosome -> dominates(newChromosome, chromosome));
        paretoFront.add(newChromosome);
        change=1;
    }

    private boolean dominates(T c1, T c2) {
        double f1c1 = sizeFF.applyAsDouble(c1);
        double f2c1 = coverageFF.applyAsDouble(c1);

        double f1c2 = sizeFF.applyAsDouble(c2);
        double f2c2 = coverageFF.applyAsDouble(c2);

        return (f1c1 <= f1c2 && f2c1 >= f2c2) && (f1c1 < f1c2 || f2c1 > f2c2);
    }

    @Override
    public StoppingCondition getStoppingCondition() {
        return stoppingCondition;
    }
}