# Test Suite Minimization

## Project Overview

The Test Suite Minimization problem involves reducing the number of test cases in a given test suite while maintaining high test coverage. The goal is to optimize a set of test cases, balancing between minimizing the number of test cases and maximizing the coverage of the software under test. In this project, we apply the multi-objective optimization algorithm **NSGA-II** and **Random Search** to solve this problem.

## Problem Description

Regression testing is crucial for ensuring that new changes do not break existing functionality. However, executing all test cases during each regression testing session can be time-consuming and expensive. The challenge lies in reducing the number of test cases to execute while still achieving a satisfactory level of coverage.

We are dealing with a **multi-objective optimization** problem:
1. **Minimizing the size of the test suite** (i.e., the number of test cases).
2. **Maximizing the achieved coverage** (i.e., the percentage of lines covered by the test cases).

This problem has two competing objectives: reducing the number of tests often reduces coverage, and increasing coverage can require additional test cases.

## Solution Approach

We use **NSGA-II (Non-dominated Sorting Genetic Algorithm II)**, a multi-objective Genetic Algorithm, to solve the test suite minimization problem. The algorithm returns a Pareto front, representing a set of solutions that are non-dominated. Additionally, **Random Search** is implemented as a baseline approach to compare the results with NSGA-II.

### Key Concepts:
- **Chromosomes**: Each chromosome represents a test suite (subset of test cases).
- **Fitness Function**: We optimize for two objectives: minimizing the test suite size and maximizing coverage.
- **Operators**: 
  - **Crossover**: Combines two parent chromosomes to produce offspring.
  - **Mutation**: Introduces random changes to a chromosome to explore new solutions.
  - **Selection**: Uses binary tournament selection to choose parent chromosomes.
  
### The Algorithm:
- **NSGA-II**: Operates on a population of chromosomes, generating offspring through mutation and crossover. It uses Pareto dominance to rank and select the best solutions.
- **Random Search**: Samples random solutions and tracks the Pareto front of non-dominated solutions across all sampled individuals.



