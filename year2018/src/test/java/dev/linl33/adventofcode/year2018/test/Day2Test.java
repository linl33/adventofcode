package dev.linl33.adventofcode.year2018.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2018.Day2;

import java.util.Map;

class Day2Test implements AdventSolutionTest<Integer, String> {
  @Override
  public AdventSolution<Integer, String> newSolutionInstance() {
    return new Day2();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 6200,
        "day2test1", 12
    );
  }

  @Override
  public Map<String, String> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), "xpysnnkqrbuhefmcajodplyzw",
        "day2test2", "fgij"
    );
  }
}
