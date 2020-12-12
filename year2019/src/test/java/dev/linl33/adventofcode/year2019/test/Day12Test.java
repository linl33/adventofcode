package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day12;

import java.util.Map;

class Day12Test implements AdventSolutionTest<Integer, Long> {
  @Override
  public AdventSolution<Integer, Long> newSolutionInstance() {
    return new Day12();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 13399);
  }

  @Override
  public Map<String, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 312992287193064L,
        "day12test1", 2772L,
        "day12test2", 4686774924L
    );
  }
}
