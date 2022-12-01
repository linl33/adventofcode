package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.jmh.JmhSolutionBenchmark;
import dev.linl33.adventofcode.lib.solution.AbsAdventSolution;

public abstract class AdventSolution2022<T1, T2> extends AbsAdventSolution<T1, T2> implements JmhSolutionBenchmark<T1, T2> {
  @Override
  public int getYear() {
    return 2022;
  }
}
