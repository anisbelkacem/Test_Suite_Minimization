/* ******************************************************************************
 * Copyright (c) 2009, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 * https://www.jacoco.org/jacoco/trunk/doc/api.html
 * https://www.jacoco.org/jacoco/trunk/doc/examples/java/CoreTutorial.java
 *******************************************************************************/

package de.uni_passau.fim.se2.sbse.suite_minimisation.utils;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Measures code coverage of a class under test by a given test suite.
 */
public final class CoverageTracker {

    /**
     * Annotation with which a JUnit 5 test case is marked.
     */
    private static final Class<? extends Annotation> JUNIT5_TEST = org.junit.jupiter.api.Test.class;

    /**
     * Default suffix appended to the name of the CUT to obtain the name of its test suite.
     */
    private static final String DEFAULT_TEST_SUFFIX = "Test";

    /**
     * The class under test (often abbreviated as "CUT").
     */
    private final Class<?> classUnderTest;

    /**
     * Name of the class under test.
     */
    private final String classUnderTestName;

    /**
     * The test suite. Consists of test cases.
     */
    private final Class<?> testSuite;

    /**
     * Name of the test suite.
     */
    private final String testSuiteName;

    /**
     * Method names of the test cases in the test suite.
     */
    private final String[] testCases;

    /**
     * The coverage matrix. Lazily instantiated and cached. Every line corresponds to a test case.
     * Every row corresponds to a line in the source code of the CUT. An entry in the matrix is
     * {@code true} if the corresponding test covers the corresponding line.
     */
    private final boolean[][] coverageMatrix;

    /**
     * The line numbers in the source code. Lazily instantiated and cached.
     */
    private int[] sourceLineNumbers;

    /**
     * Whether coverage has already been measured. Used for caching.
     */
    private boolean cached;

    /**
     * Creates a new instance for tracking the coverage of the given class under test by the
     * specified test suite.
     *
     * @param classUnderTest class whose coverage to measure
     * @param testSuite      corresponding test suite
     */
    public CoverageTracker(final Class<?> classUnderTest, final Class<?> testSuite) {
        this.classUnderTest = Objects.requireNonNull(classUnderTest);
        this.classUnderTestName = classUnderTest.getName();
        this.testSuite = Objects.requireNonNull(testSuite);
        this.testSuiteName = testSuite.getName();
        this.testCases = getTestCases(testSuite);
        this.coverageMatrix = new boolean[testCases.length][];
        this.sourceLineNumbers = null;
        this.cached = false;
    }

    /**
     * Creates a new instance for tracking the coverage of the given class under test, attempting
     * to determine the corresponding test suite automatically.
     *
     * @param classUnderTestName fully-qualified name of the class whose coverage to measure
     * @throws ClassNotFoundException if a class could not be found
     */
    public CoverageTracker(final String classUnderTestName) throws ClassNotFoundException {
        this(load(classUnderTestName), load(getTestSuiteName(classUnderTestName)));
    }

    /**
     * Attempts to load and return the class with the given fully-qualified name.
     *
     * @param className name of the class to load
     * @return the loaded class
     * @throws ClassNotFoundException if the class with the given name could not be found
     */
    private static Class<?> load(final String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    /**
     * Returns the name of the test suite for the given class under test.
     *
     * @param classUnderTestName name of the class for which to return the name of its test suite
     * @return name of the test suite
     */
    private static String getTestSuiteName(final String classUnderTestName) {
        return classUnderTestName + DEFAULT_TEST_SUFFIX;
    }

    /**
     * Retrieves the test cases from the given test suite.
     *
     * @param testSuite a test suite
     * @return test cases
     */
    private static String[] getTestCases(final Class<?> testSuite) {
        final Method[] declaredMethods = testSuite.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
                .filter(CoverageTracker::isTestCase)
                .map(Method::getName)
                .sorted() // to impose a fixed order (here: lexicographical order)
                .toArray(String[]::new);
    }

    /**
     * Returns the test cases of the test suite this {@code CoverageTracker} was constructed with.
     * The test cases follow the same order as in the coverage matrix returned by {@code
     * getCoverageMatrix()}.
     *
     * @return the test cases
     */
    public String[] getTestCases() {
        return testCases;
    }

    /**
     * Determines whether the given method is a unit test.
     *
     * @param method the method to test
     * @return {@code true} if {@code method} is a unit test, {@code false} otherwise
     */
    private static boolean isTestCase(final Method method) {
        return method.isAnnotationPresent(JUNIT5_TEST);
    }

    /**
     * Returns line coverage of the CUT as coverage matrix. Hereby, every row in the matrix
     * represents a test case and every column represents a line in the CUT. An entry {@code
     * matrix[i][j] == true} indicates that test case {@code i} covers line {@code j}. The matrix is
     * rectangular.
     *
     * @return coverage matrix as described above
     * @throws Exception if an error occurred
     */
    public boolean[][] getCoverageMatrix() throws Exception {
        if (!cached) {
            measureCoverage();
            assert isRectangularMatrix() : "coverage matrix is not rectangular";
            cached = true;
        }

        return coverageMatrix;
    }

    /**
     * Tells whether the coverage matrix is rectangular.
     *
     * @return {@code true} if rectangular, {@code false} otherwise
     */
    private boolean isRectangularMatrix() {
        boolean rectangular = true;
        for (int i = 0; rectangular && i < coverageMatrix.length - 1; ) {
            // All rows in the matrix must have the same length.
            rectangular = coverageMatrix[i++].length == coverageMatrix[i].length;
        }
        return rectangular;
    }

    /**
     * Measures the line coverage of the CUT achieved by all unit tests in the test suite.
     *
     * @throws Exception if some error occurred
     */
    private void measureCoverage() throws Exception {
        for (int i = 0; i < testCases.length; i++) {
            final String testCase = testCases[i];
            final boolean[] coverage = measureCoverage(testCase);
            coverageMatrix[i] = coverage;
        }
    }

    /**
     * Measures the line coverage of the CUT achieved by the specified unit test and returns a
     * boolean array. An entry in the array is {@code true} if the corresponding line was covered.
     *
     * @param testCase for which to measure coverage
     * @throws Exception if an error occurred
     */
    private boolean[] measureCoverage(final String testCase) throws Exception {
        // For instrumentation and runtime we need an IRuntime instance to collect execution data.
        final var coverageReporter = new LoggerRuntime();

        // The instrumenter creates a modified version of our class under test that contains
        // additional probes for execution data recording.
        final var coverageInstrumenter = new Instrumenter(coverageReporter);

        // We use a special class loader to directly load the instrumented class definitions from
        // byte[] instances.
        final var loader = new MemoryClassLoader();
        instrumentAndLoad(classUnderTest, coverageInstrumenter, loader);

        // Now we're ready to run our instrumented class and need to start the runtime first.
        final var runtimeData = new RuntimeData();
        coverageReporter.startup(runtimeData);

        // We execute the test case via Java reflection. The test suite must have benn loaded with
        // the same class loader as the class under test.
        execute(testCase, addDefinitionAndLoad(testSuite, loader));

        // At the end of test execution we collect execution data and shutdown the runtime.
        final var executionData = new ExecutionDataStore();
        runtimeData.collect(executionData, new SessionInfoStore(), false);
        coverageReporter.shutdown();

        // Together with the original class definition we can calculate coverage information.
        final var coverageBuilder = new CoverageBuilder();
        final var analyzer = new Analyzer(executionData, coverageBuilder);
        try (final InputStream originalCut = getClassStream(classUnderTestName)) {
            analyzer.analyzeClass(originalCut, classUnderTestName);
        }

        // (Just for debugging purposes: record the original line numbers of the source file.)
        if (sourceLineNumbers == null) {
            sourceLineNumbers = getNonEmptyLines(coverageBuilder);
        }

        // Finally, we build and return the coverage array.
        return getCoverageArray(coverageBuilder);
    }

    /**
     * Executes the test case part of the given test suite.
     *
     * @param testCase  the test case to execute
     * @param testSuite the test suite containing the test case
     * @throws NoSuchMethodException     if no test case with the given name or no default
     *                                   constructor for the test suite exists
     * @throws IllegalAccessException    if the test case or constructor of the test suite is
     *                                   inaccessible
     * @throws InstantiationException    if the test suite could not be instantiated
     * @throws InvocationTargetException if the constructor throws an exception
     */
    private void execute(final String testCase, final Class<?> testSuite)
            throws NoSuchMethodException, IllegalAccessException, InstantiationException,
            InvocationTargetException {
        execute(testSuite.getDeclaredMethod(testCase), newInstanceOf(testSuite));
    }

    /**
     * Returns a new instance of the give class.
     *
     * @param clazz the class to instantiate
     * @return new instance of {@code clazz}
     * @throws NoSuchMethodException     if no default constructor was found
     * @throws IllegalAccessException    if the constructor is inaccessible
     * @throws InvocationTargetException if the constructor throws an exception
     * @throws InstantiationException    if the class could not be instantiated
     */
    private Object newInstanceOf(final Class<?> clazz)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        // We just assume there's a default constructor that doesn't require parameters.
        final Constructor<?> ctor = clazz.getDeclaredConstructor();
        ctor.setAccessible(true);
        return ctor.newInstance();
    }

    /**
     * Executes a test case on the given instance of the test suite.
     *
     * @param testCase  the test case to execute
     * @param testSuite the instance of the test suite on which to invoke the test case
     * @throws IllegalAccessException    if the test case is inaccessible
     */
    private void execute(final Method testCase, final Object testSuite)
            throws IllegalAccessException {
        testCase.setAccessible(true); // Make non-public tests accessible.
        try {
            testCase.invoke(testSuite);
        } catch (InvocationTargetException e) {
            // Swallow exceptions thrown by the class under test.
        }
    }

    /**
     * Instruments the given class using the specified instrumenter and loads the instrumented class
     * using the specified class loader.
     *
     * @param clazz        to load
     * @param instrumenter with which to instrument
     * @param loader       with which to load the instrumented class
     * @throws IOException            if instrumentation failed
     * @throws ClassNotFoundException if the class was not found
     */
    private void instrumentAndLoad(final Class<?> clazz,
            final Instrumenter instrumenter,
            final MemoryClassLoader loader)
            throws IOException, ClassNotFoundException {
        final String className = clazz.getName();
        final byte[] instrumentedBytecode = instrument(className, instrumenter);
        addDefinitionAndLoad(className, instrumentedBytecode, loader);
    }

    /**
     * Reloads a class using the given class loader.
     *
     * @param clazz the class to reload
     * @return the reloaded class
     * @throws ClassNotFoundException if the class was not found
     * @throws IOException            if an I/O error occurs
     */
    private Class<?> addDefinitionAndLoad(final Class<?> clazz, final MemoryClassLoader loader)
            throws ClassNotFoundException, IOException {
        final String className = clazz.getName();
        final InputStream targetClass = getClassStream(className);
        return addDefinitionAndLoad(className, targetClass.readAllBytes(), loader);
    }

    /**
     * Defines and loads a class with the given name using the specified bytecode and classloader.
     *
     * @param className name of the class to load
     * @param bytecode  class definition to load
     * @param loader    with which to load the class definition
     * @return the loaded class
     * @throws ClassNotFoundException if the class was not found
     */
    private Class<?> addDefinitionAndLoad(final String className,
            final byte[] bytecode,
            final MemoryClassLoader loader)
            throws ClassNotFoundException {
        loader.addDefinition(className, bytecode);
        return loader.loadClass(className);
    }

    /**
     * Instruments the class with the given name using the given instrumenter.
     *
     * @param className    name of the class to instrument
     * @param instrumenter with which to instrument
     * @return instrumented byte code of the class
     * @throws IOException if instrumentation failed
     */
    private byte[] instrument(final String className, final Instrumenter instrumenter)
            throws IOException {
        final byte[] instrumentedBytecode;
        try (final InputStream originalCut = getClassStream(className)) {
            instrumentedBytecode = instrumenter.instrument(originalCut, className);
        }
        return instrumentedBytecode;
    }

    /**
     * Gathers line coverage data from the given coverage builder and returns a boolean array where
     * every entry {@code array[i] == true} indicates that line {@code i} was covered.
     *
     * @param builder from which to gather information
     * @return coverage array as described above
     */
    private boolean[] getCoverageArray(final CoverageBuilder builder) {
        final var classes = builder.getClasses();
        assert classes.size() == 1 : "There should only be coverage information about the CUT";
        final var classCoverage = classes.iterator().next();

        final int firstLine = classCoverage.getFirstLine();
        final int lastLine = classCoverage.getLastLine();
        assert firstLine != -1 && lastLine != -1 : "no coverage information is present";

        final int lines = classCoverage.getLineCounter().getTotalCount(); // # of non-empty lines
        assert lines > 0 : "no coverage information is present";
        final boolean[] coverage = new boolean[lines];

        // For every non-empty line in the CUT, check if it was covered or not.
        for (int i = firstLine, j = 0; i <= lastLine && j < lines; i++) {
            final int status = classCoverage.getLine(i).getStatus();
            if (status != ICounter.EMPTY) {
                coverage[j++] = status != ICounter.NOT_COVERED;
            }
        }

        return coverage;
    }

    private int[] getNonEmptyLines(final CoverageBuilder builder) {
        final var classCoverage = builder.getClasses().iterator().next();
        final int lines = classCoverage.getLineCounter().getTotalCount();
        final int[] nonEmptyLines = new int[lines];

        final int firstLine = classCoverage.getFirstLine();
        final int lastLine = classCoverage.getLastLine();
        for (int i = firstLine, j = 0; i <= lastLine && j < lines; i++) {
            final int status = classCoverage.getLine(i).getStatus();
            if (status != ICounter.EMPTY) {
                nonEmptyLines[j++] = i;
            }
        }

        return nonEmptyLines;
    }

    private String dumpCoverageInfo() {
        final var sb = new StringBuilder();
        sb.append(String.format("Coverage of %s by %s%n", classUnderTestName, testSuiteName));

        if (!cached) {
            sb.append(" * Not measured yet!\n");
            return sb.toString();
        }

        for (int i = 0; i < testCases.length; i++) {
            final String testCase = testCases[i];
            sb.append(String.format(" * %s%n", testCase));

            final boolean[] coverage = coverageMatrix[i];
            for (int j = 0; j < coverage.length; j++) {
                final boolean c = coverage[j];
                final int line = sourceLineNumbers[j];
                sb.append(String.format("    > line %d: %s%n", line, c ? "covered" : "MISSED"));
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return dumpCoverageInfo();
    }

    /**
     * Returns the class with the given name as an input stream of bytes.
     *
     * @param className name of the class
     * @return input stream of bytes for the specified class
     */
    private InputStream getClassStream(final String className) {
        final String resource = '/' + className.replace('.', '/') + ".class";
        return getClass().getResourceAsStream(resource);
    }

    /**
     * A class loader that loads classes from in-memory data.
     */
    public static class MemoryClassLoader extends ClassLoader {

        private final Map<String, byte[]> definitions = new HashMap<>();

        /**
         * Adds an in-memory representation of a class.
         *
         * @param name  name of the class
         * @param bytes class definition
         */
        public void addDefinition(final String name, final byte[] bytes) {
            definitions.put(name, bytes);
        }

        @Override
        protected Class<?> loadClass(final String name, final boolean resolve)
                throws ClassNotFoundException {
            final byte[] bytes = definitions.get(name);
            if (bytes != null) {
                return defineClass(name, bytes, 0, bytes.length);
            }
            return super.loadClass(name, resolve);
        }
    }
}
