package dev.linl33.adventofcode.year2018.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2018.Day5;

import java.util.Map;

class Day5Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day5();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 11118);
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 6948);
  }
}
