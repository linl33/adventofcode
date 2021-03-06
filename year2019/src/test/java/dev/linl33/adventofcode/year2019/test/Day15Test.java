package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day15;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day15();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 234);
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 292);
  }

  @Test
  void part2ByPathFinding(AdventSolution<Integer, Integer> day15) {
    assertEquals(292, day15.run(Day15::part2ByPathFinding, day15.getPart2Resource()));
  }
}
