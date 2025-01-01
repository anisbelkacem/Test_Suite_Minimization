package de.uni_passau.fim.se2.sbse.suite_minimisation;

import de.uni_passau.fim.se2.sbse.suite_minimisation.algorithms.GeneticAlgorithm;
import de.uni_passau.fim.se2.sbse.suite_minimisation.algorithms.SearchAlgorithmType;
import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.Chromosome;
import de.uni_passau.fim.se2.sbse.suite_minimisation.chromosomes.BiChromosome;
import de.uni_passau.fim.se2.sbse.suite_minimisation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_minimisation.stopping_conditions.MaxFitnessEvaluations;
import de.uni_passau.fim.se2.sbse.suite_minimisation.stopping_conditions.StoppingCondition;
import de.uni_passau.fim.se2.sbse.suite_minimisation.utils.AlgorithmBuilder;
import de.uni_passau.fim.se2.sbse.suite_minimisation.utils.CoverageTracker;
import de.uni_passau.fim.se2.sbse.suite_minimisation.utils.Randomness;
import de.uni_passau.fim.se2.sbse.suite_minimisation.utils.Utils;
import picocli.CommandLine;

import java.util.*;
import java.util.concurrent.Callable;

import static java.util.stream.Collectors.summarizingDouble;

public class Main implements Callable<Integer> {

    @CommandLine.Option(
            names = {"-c", "--class"},
            description = "The name of the class under test.",
            required = true)
    private String className;

    @CommandLine.Option(
            names = {"-p", "--package"},
            description = "The package containing the class under test.",
            defaultValue = "de.uni_passau.fim.se2.sbse.suite_minimisation.examples")
    private String packageName;

    @CommandLine.Option(
            names = {"-f", "--max-evaluations"},
            description = "The maximum number of fitness evaluations.",
            defaultValue = "1000")
    private int maxEvaluations;

    @CommandLine.Option(
            names = {"-r", "--repetitions"},
            description = "The number of search repetitions to perform.",
            defaultValue = "10")
    private int repetitions;

    @CommandLine.Option(
            names = {"-s", "--seed"},
            description = "Use a fixed RNG seed.")
    public void setSeed(int seed) {
        Randomness.random().setSeed(seed);
    }

    @CommandLine.Parameters(
            paramLabel = "algorithms",
            description = "The search algorithms to use.",
            arity = "1...",
            converter = AlgorithmConverter.class)
    private List<SearchAlgorithmType> algorithms;

    /**
     * The names of the test cases (corresponding to the coverage matrix). That is, for and index
     * {@code i}, {@code testCases[i]} tells the name of the ith test case and
     * {@code coverageMatrix[i]} tells which lines of code are covered by the ith test case.
     */
    private String[] testCases;

    /**
     * The coverage matrix for the analyzed software system.
     */
    private boolean[][] coverageMatrix;

    /**
     * Instance of the algorithm builder to create the search algorithms.
     */
    private AlgorithmBuilder algorithmBuilder;


    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    /**
     * Executes the specified search algorithms and prints a summary of the obtained results.
     *
     * @return The exit code of the application
     */
    public Integer call() {
        try {
            setCoverageMatrixAndTestCaseNames();
        } catch (Exception e) {
            System.err.println("Error while initializing coverage matrix and test case names.");
            return 1;
        }
        StoppingCondition stoppingCondition = MaxFitnessEvaluations.of(maxEvaluations);
        this.algorithmBuilder = new AlgorithmBuilder(Randomness.random(), stoppingCondition, coverageMatrix);

        List<AlgorithmStats> results = startSearch();
        for (final AlgorithmStats result : results) {
            System.out.println(result);
        }
        return 0;
    }

    /**
     * Starts the command line tool, running the specified algorithms and returns the results.
     *
     * @return the results
     */
    List<AlgorithmStats> startSearch() {
        final var algorithmStats = new ArrayList<AlgorithmStats>();
        for (final var algorithm : algorithms) {
            final GeneticAlgorithm<? extends Chromosome<?>> search = algorithmBuilder.buildAlgorithm(algorithm);

            final long start = System.currentTimeMillis();
            final var stats = repeatSearch(search);
            final long totalTime = System.currentTimeMillis() - start;
            final double avgTime = (double) totalTime / repetitions;

            algorithmStats.add(new AlgorithmStats(algorithm, stats, totalTime, avgTime));
        }
        return algorithmStats;
    }

    private List<RepetitionStats> repeatSearch(final GeneticAlgorithm<? extends Chromosome<?>> algorithm) {
        // Normalized fitness functions that compute the coverage and size of a test suite.
        final FitnessFunction<? extends Chromosome<?>> coverageFF = algorithmBuilder.getCoverageFF();
        final FitnessFunction<? extends Chromosome<?>> sizeFF = algorithmBuilder.getSizeFF();

        // Coordinates for the reference point when computing the hyper-volume of a Pareto front.
        final double covRef = 0.0;  // worst possible coverage (0%)
        final double sizeRef = 1.0; // worst possible size (100%)

        final var list = new ArrayList<RepetitionStats>(repetitions);
        for (int i = 1; i <= repetitions; i++) {
            System.out.printf("Repetition %d/%d for algorithm %s\n", i, repetitions, algorithm);
            final List<? extends Chromosome<?>> testSuites = algorithm.findSolution();
            final double hyperVolume = Utils.computeHyperVolume(testSuites, coverageFF, sizeFF, covRef, sizeRef);
            final var testSuiteStats = makeTestSuiteStatsFrom(testSuites);
            final var repetitionStats = new RepetitionStats(i, testSuiteStats, hyperVolume);
            list.add(repetitionStats);
        }
        return list;
    }

    private List<TestSuiteStats> makeTestSuiteStatsFrom(
            final List<? extends Chromosome<?>> testSuites) {
        final var list = new ArrayList<TestSuiteStats>(testSuites.size());
        for (final Chromosome<?> testSuiteChromosome : testSuites) {
            list.add(makeTestSuiteStatsFrom(testSuiteChromosome));
        }
        return list;
    }

    private TestSuiteStats makeTestSuiteStatsFrom(final Chromosome<?> testSuiteChromosome) {
        final List<String> testCaseNames = getTestCaseNamesFrom(testSuiteChromosome);
        final double coverage = getCoverageOf(testSuiteChromosome);
        final double size = getSizeOf(testSuiteChromosome);
        return new TestSuiteStats(testCaseNames, coverage, size);
    }

    /**
     * Extracts the test case names from the given test suite chromosome. This chromosome has been
     * generated and returned by your search algorithm as part of the Pareto-front. The chromosome
     * represents a test suite comprises a some test cases.
     *
     * @param testSuiteChromosome the test suite chromosome
     * @return the names of the test cases in the test suite represented by the chromosome
     * @apiNote The input type uses a wildcard type "{@code ?}". This is because your custom
     * subclass of {@code Chromosome} has not existed yet at the time of writing this code, so I
     * couldn't specify it.
     * @implSpec The names of these test cases shall be retrieved from the {@code testCaseNames}
     * array of this class. Due to the wildcard in the input type, you will need to downcast to your
     * custom chromosome type before you can do so.
     */
    @SuppressWarnings("unchecked")
    List<String> getTestCaseNamesFrom(final Chromosome<?> testSuiteChromosome) {
        BiChromosome chromosome = (BiChromosome) testSuiteChromosome;
        List<Integer> activeTestCaseIndices = chromosome.getActiveTestCases();
        
        List<String> activeTestCaseNames = new ArrayList<>();

        for (int index : activeTestCaseIndices) {
            activeTestCaseNames.add(testCases[index]);
        }

        return activeTestCaseNames;
    }


    /**
     * Takes the given test suite chromosome as input and returns its relative/normalized coverage.
     *
     * @param testSuiteChromosome the test suite chromosome
     * @return the relative coverage of the test suite
     * @apiNote The input type uses a wildcard type "{@code ?}". This is because your custom
     * subclass of {@code Chromosome} has not existed yet at the time of writing this code, so I
     * couldn't specify it.
     * @implSpec As a consequence of the wildcard type, you'll need to downcast the
     * `testSuiteChromosome` to your own chromosome type. If you want to reuse the `coverageFF`
     * field of this class, you also need to cast the fitness function.
     */
    @SuppressWarnings("unchecked")
    double getCoverageOf(final Chromosome<?> testSuiteChromosome) {
        BiChromosome chromosome = (BiChromosome) testSuiteChromosome;
        FitnessFunction<BiChromosome> coverageFF = (FitnessFunction<BiChromosome>) algorithmBuilder.getCoverageFF();
        return coverageFF.applyAsDouble(chromosome);
    }


    /**
     * Takes the given test suite chromosome as input and returns its relative/normalized size.
     *
     * @param testSuiteChromosome the test suite chromosome
     * @return the relative size of the test suite
     * @apiNote The input type uses a wildcard type "{@code ?}". This is because your custom
     * subclass of {@code Chromosome} has not existed yet at the time of writing this code, so I
     * couldn't specify it.
     * @implSpec As a consequence of the wildcard type, you'll need to downcast the
     * `testSuiteChromosome` to your own chromosome type. If you want to reuse the `sizeFF` field of
     * this class, you also need to cast the fitness function.
     */
    @SuppressWarnings("unchecked")
    double getSizeOf(final Chromosome<?> testSuiteChromosome) {
        BiChromosome chromosome = (BiChromosome) testSuiteChromosome;
        FitnessFunction<BiChromosome> sizeFF = (FitnessFunction<BiChromosome>) algorithmBuilder.getSizeFF();
        return sizeFF.applyAsDouble(chromosome);
    }



    /**
     * Initializes the coverage matrix and the test case names.
     *
     * @throws Exception if an error occurs while initializing the coverage matrix and the test case names
     */
    private void setCoverageMatrixAndTestCaseNames() throws Exception {
        final String fullyQualifiedClassName = packageName + "." + className;
        CoverageTracker tracker = new CoverageTracker(fullyQualifiedClassName);
        this.coverageMatrix = tracker.getCoverageMatrix();
        this.testCases = tracker.getTestCases();
    }

    /**
     * Formats the given duration in milliseconds as {@code HH:MM:SS:ssss}.
     *
     * @param durationInMillis the duration in milliseconds
     * @return the formatted duration
     */
    private static String formatTime(final long durationInMillis) {
        final long millis = durationInMillis % 1000;
        final long seconds = (durationInMillis / 1000) % 60;
        final long minutes = (durationInMillis / (1000 * 60)) % 60;
        final long hours = (durationInMillis / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d.%d", hours, minutes, seconds, millis);
    }

    /**
     * Formats the given duration in milliseconds as {@code HH:MM:SS:ssss}. The fractional part of
     * the duration is removed.
     *
     * @param fractionalMillis the duration
     * @return the formatted duration
     */
    private static String formatTime(final double fractionalMillis) {
        return formatTime((long) fractionalMillis);
    }

    /**
     * Container class reporting statistics about test suites, namely the names of its test cases,
     * its coverage and size.
     */
    public record TestSuiteStats(List<String> testCaseNames, double coverage, double size) {
        private static final String INDENT2 = " ".repeat(RepetitionStats.INDENT1.length()) + "> ";
        private static final String INDENT3 = " ".repeat(INDENT2.length());

        public TestSuiteStats(
                final List<String> testCaseNames,
                final double coverage,
                final double size) {
            if (testCaseNames.isEmpty()) {
                throw new IllegalArgumentException("Expected a non-empty test suite");
            }

            if (!(0.0 <= coverage && coverage <= 1.0)) {
                throw new IllegalArgumentException("Expected normalized coverage: " + coverage);
            }

            if (!(0.0 <= size && size <= 1.0)) {
                throw new IllegalArgumentException("Expected normalized size: " + size);
            }

            this.testCaseNames = new ArrayList<>(testCaseNames);
            this.testCaseNames.sort(null); // Sort test cases in lexicographical order.
            this.coverage = coverage;
            this.size = size;
        }

        @Override
        public String toString() {
            return String.format("%sTest Suite: %s%n", INDENT2, testCaseNames)
                    + String.format("%sCoverage:   %s%n", INDENT3, coverage)
                    + String.format("%sSize:       %s%n", INDENT3, size);
        }
    }

    public record RepetitionStats(int repetition, List<TestSuiteStats> testSuiteStats, double hyperVolume) {

        private static final String INDENT1 = " * ";
        private static final Comparator<TestSuiteStats> byCoverage =
                Comparator.comparingDouble(ts -> ts.coverage);

        public RepetitionStats(
                final int repetition,
                final List<TestSuiteStats> testSuiteStats,
                final double hyperVolume) {
            if (!(0.0 <= hyperVolume && hyperVolume <= 1.0)) {
                throw new IllegalArgumentException("Expected normalized hyper-volume: "
                        + hyperVolume);
            }

            this.testSuiteStats = new ArrayList<>(testSuiteStats);
            this.testSuiteStats.sort(byCoverage);
            this.repetition = repetition;
            this.hyperVolume = hyperVolume;
        }

        @Override
        public String toString() {
            final var sb = new StringBuilder(
                    String.format("%sRepetition %d:\tHV %f\tFront Size %d%n",
                            INDENT1, repetition, hyperVolume, testSuiteStats.size()));

            for (final var testSuiteStat : testSuiteStats) {
                sb.append(testSuiteStat.toString());
            }

            return sb.toString();
        }
    }

    public static final class AlgorithmStats {

        private final SearchAlgorithmType algorithm;
        private final List<RepetitionStats> repetitionStats;
        private final DoubleSummaryStatistics hyperVolumeStats;
        private final long totalTime;
        private final double avgTime;

        public AlgorithmStats(
                final SearchAlgorithmType algorithm,
                final List<RepetitionStats> repetitionStats,
                final long totalTime,
                final double avgTime) {
            this.algorithm = algorithm;
            this.repetitionStats = repetitionStats;
            this.hyperVolumeStats = repetitionStats.stream()
                    .collect(summarizingDouble(stat -> stat.hyperVolume));
            this.totalTime = totalTime;
            this.avgTime = avgTime;
        }

        @Override
        public String toString() {
            final var sb = new StringBuilder("Results for " + algorithm);

            for (final var repetitionStat : repetitionStats) {
                sb.append(repetitionStat.toString());
            }

            sb.append("Summary:\n");
            sb.append(String.format("%s (hyper volumes):\tavg: %f\tmin: %f\tmax:%f%n",
                    algorithm,
                    hyperVolumeStats.getAverage(),
                    hyperVolumeStats.getMin(),
                    hyperVolumeStats.getMax()));
            sb.append(String.format("%s (time):\ttotal: %s\tavg: %s%n",
                    algorithm,
                    formatTime(totalTime),
                    formatTime(avgTime)));

            return sb.toString();
        }

        public DoubleSummaryStatistics getHyperVolumeStats() {
            return hyperVolumeStats;
        }
    }

}

/**
 * Converts supplied cli parameters to the respective {@link SearchAlgorithmType}.
 */
final class AlgorithmConverter implements CommandLine.ITypeConverter<SearchAlgorithmType> {
    @Override
    public SearchAlgorithmType convert(String algorithm) {
        return switch (algorithm.toUpperCase()) {
            case "RS" -> SearchAlgorithmType.RANDOM_SEARCH;
            case "NSGA2" -> SearchAlgorithmType.NSGA_II;
            default -> throw new IllegalArgumentException("The algorithm '" + algorithm + "' is not a valid option.");
        };
    }
}
