package dev.linl33.adventofcode.year2023.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2023.Day11;

import java.util.Map;

public class Day11Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day11();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 9947476L,
        "day11test1", 374L,
        "day11test2", 2466269413L
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 519939907614L,
        "day11test1", 82000210L,
        "day11test2", 155354715564293L
    );
  }
}
