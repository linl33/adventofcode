package dev.linl33.adventofcode.year2025.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2025.Day6;

import java.util.Map;

public class Day6Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day6();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
      newSolutionInstance().getPart1Resource(), 4449991244405L,
      "day6test1", 4277556L
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
      newSolutionInstance().getPart2Resource(), 9348430857627L,
      "day6test1", 3263827L
    );
  }
}
