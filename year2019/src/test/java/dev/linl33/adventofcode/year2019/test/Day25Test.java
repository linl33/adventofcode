package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day25;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Day25Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day25();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 319815680);
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), Integer.MIN_VALUE);
  }
}
