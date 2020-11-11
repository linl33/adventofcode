package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;

import java.util.Map;

public class Day21Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day21();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 19354818);
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 1143787220);
  }
}
