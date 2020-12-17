package dev.linl33.adventofcode.lib.benchmark;

import dev.linl33.adventofcode.lib.solution.AdventSolution;

public interface AdventSolutionBenchmark<T1, T2> extends AdventSolution<T1, T2> {
  void benchmark(BenchmarkOption... options);
}
