package de.uni_passau.fim.se2.sbse.suite_minimisation.algorithms;

import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;
import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.Chromosome;
import de.uni_passau.fim.se2.sbse.suite_minimisation.crossover.BiCrossover;
import de.uni_passau.fim.se2.sbse.suite_minimisation.mutation.BiMutation;
import de.uni_passau.fim.se2.sbse.suite_minimisation.selection.BinaryTournamentSelection;
import de.uni_passau.fim.se2.sbse.suite_minimisation.stopping_conditions.StoppingCondition;
import de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_minimisation.utils.Pair;
import java.util.*;

public class NSGA2<T extends Chromosome<T>> implements GeneticAlgorithm<T> {

    private final StoppingCondition stoppingCondition;
    private final FitnessFunction<BiChromosome> sizeFF;
    private final FitnessFunction<BiChromosome> coverageFF;
    private final int lenchromosome;
    private final Random random;

    public NSGA2(
            StoppingCondition stoppingCondition,
            FitnessFunction<BiChromosome> sizeFF,
            FitnessFunction<BiChromosome> coverageFF,
            int lenchromosome,
            Random random) {
        this.stoppingCondition = stoppingCondition;
        this.sizeFF = sizeFF;
        this.coverageFF = coverageFF;
        this.lenchromosome= lenchromosome;
        this.random = random;
    }

    @Override @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<T> findSolution() {
        double MutationRate = random.nextDouble();
        double crossoverRate = random.nextDouble();
        BiMutation mutation = new BiMutation(MutationRate); 
        BiCrossover crossover = new BiCrossover(crossoverRate);
        BinaryTournamentSelection selection = new BinaryTournamentSelection(
        (c1, c2) -> {
        BiChromosome chrom1 = (BiChromosome) c1;
        BiChromosome chrom2 = (BiChromosome) c2;

        double f1_1 = coverageFF.applyAsDouble(chrom1);
        double f1_2 = coverageFF.applyAsDouble(chrom2);
        if (Double.compare(f1_1, f1_2) != 0) {
            return Double.compare(f1_1, f1_2); 
        }
        double f2_1 = sizeFF.applyAsDouble(chrom1);
        double f2_2 = sizeFF.applyAsDouble(chrom2);
        return Double.compare(f2_2, f2_1);
            }, 
            random
        );

        List<T> population = initializePopulation(1000,mutation,crossover,lenchromosome); 
        stoppingCondition.notifySearchStarted();
        

        while (!stoppingCondition.searchMustStop()) {
            //List<T> offspring = generateOffspring(population,selection,mutation,crossover);
            List<T> combinedPopulation = new ArrayList<>();
            combinedPopulation.addAll(population);
            //combinedPopulation.addAll(offspring);
            stoppingCondition.notifyFitnessEvaluation();
            List<List<T>> paretoFronts = nonDominatedSorting(combinedPopulation);
            population=paretoFronts.get(0);
        }
        Set<T> finalParetoFront = new HashSet<>(population);
        population = new ArrayList<>(finalParetoFront);
        return population;
    }
    @SuppressWarnings("unchecked")
    private List<T> initializePopulation(int size ,BiMutation mutation,BiCrossover crossover,int lenchromosome) {
        List<T> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            
            T randomChromosome = (T) generateRandomChromosome(lenchromosome, mutation,crossover);
            randomChromosome= (T) mutation.apply((BiChromosome)randomChromosome);
            population.add(randomChromosome); 
        }
        return population;
    }

    private BiChromosome generateRandomChromosome(int size,BiMutation mutation,BiCrossover crossover) {
        return BiChromosome.generateRandomChromosome(size ,mutation, crossover);
    }

    /*@SuppressWarnings("unchecked")
    private List<T> generateOffspring(List<T> population , BinaryTournamentSelection selection ,BiMutation mutation,BiCrossover crossover) {
        List<T> offspring = new ArrayList<>();
        while (offspring.size() < population.size()) {
            T  parent1 =  (T) selection.apply(population);
            T  parent2 = (T) selection.apply(population);
            Pair<T> children = (Pair<T>) crossover.apply((BiChromosome)parent1, (BiChromosome) parent2);
            for (T  child : children) {
                mutation.apply((BiChromosome)child);
                offspring.add(child);
            }
        }
        return offspring;
    }*/

    private List<List<T>> nonDominatedSorting(List<T> population) {
        Map<T, Integer> dominationCount = new HashMap<>();
        for (T individual : population) {
            int count = 0;
            for (T other : population) {
                if (dominates( (BiChromosome) other,  (BiChromosome) individual)) {
                    count++;
                }
            }
            dominationCount.put(individual, count);
        }
        List<T> sortedPopulation = new ArrayList<>(population);
        sortedPopulation.sort(Comparator.comparingInt(dominationCount::get));
    
        List<List<T>> fronts = new ArrayList<>();
        int currentCount = dominationCount.get(sortedPopulation.get(0));
        List<T> currentFront = new ArrayList<>();
    
        for (T individual : sortedPopulation) {
            int count = dominationCount.get(individual);
            if (count != currentCount) {
                if (!currentFront.isEmpty()) {
                    fronts.add(new ArrayList<>(currentFront));
                    currentFront.clear();
                }
                currentCount = count;
            }
            currentFront.add(individual);
        }
        if (!currentFront.isEmpty()) {
            fronts.add(currentFront);
        }
        /*for(List<T> front : fronts) {
            sortFront(front,population.size()); 
        }*/
        return fronts;
    }
    /*private void sortFront(List<T> front,int size) {
        Map<T, Double> crowdingDistances = new HashMap<>();
        for (T individual : front) {
            crowdingDistances.put(individual, calculateCrowdingDistance(front, individual,size));
        }
        front.sort((ind1, ind2) -> Double.compare(crowdingDistances.get(ind2), crowdingDistances.get(ind1)));
    }

    private double calculateCrowdingDistance(List<T> front, T individual,int size) {
    
        double crowdingDistance = 0.0;
    
        List<T> sortedByCoverage = new ArrayList<>(front);
        sortedByCoverage.sort(
            (c1, c2) -> {
                BiChromosome chrom1 = (BiChromosome) c1;
                BiChromosome chrom2 = (BiChromosome) c2;
        
                double f1_1 = coverageFF.applyAsDouble(chrom2);
                double f1_2 = coverageFF.applyAsDouble(chrom1);
                return Double.compare(f1_1, f1_2); 
            });

        List<T> sortedBySize = new ArrayList<>(front);
        sortedBySize.sort(
            (c1, c2) -> {
                BiChromosome chrom1 = (BiChromosome) c1;
                BiChromosome chrom2 = (BiChromosome) c2;
        
                double f1_1 = coverageFF.applyAsDouble(chrom2);
                double f1_2 = coverageFF.applyAsDouble(chrom1);
                return Double.compare(f1_1, f1_2); 
            });
    
        int index = sortedByCoverage.indexOf(individual);
        if (index == 0 || index == sortedByCoverage.size() - 1) {
            crowdingDistance += Double.POSITIVE_INFINITY; 
        } else {
            crowdingDistance += Math.abs(coverageFF.applyAsDouble(  (BiChromosome) sortedByCoverage.get(index + 1))  - sizeFF.applyAsDouble(  (BiChromosome) sortedByCoverage.get(index - 1)));
        }
        index = sortedBySize.indexOf(individual);
        if (index == 0 || index == sortedBySize.size() - 1) {
            crowdingDistance += Double.POSITIVE_INFINITY;  
        } else {
            crowdingDistance += Math.abs(sizeFF.applyAsDouble(  (BiChromosome) sortedBySize.get(index - 1))  - sizeFF.applyAsDouble(  (BiChromosome) sortedBySize.get(index + 1)));
        }
        return crowdingDistance / size;
    } */

    private boolean dominates( BiChromosome  c1, BiChromosome c2) {
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
