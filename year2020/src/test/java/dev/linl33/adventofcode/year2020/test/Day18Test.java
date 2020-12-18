package dev.linl33.adventofcode.year2020.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2020.Day18;

import java.util.Map;

class Day18Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day18();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 5374004645253L);
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 88782789402798L);
  }
}
