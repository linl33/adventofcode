package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.testlib.AdventSolutionTestUtil;
import dev.linl33.adventofcode.testlib.TestPart;
import dev.linl33.adventofcode.year2019.Day18;

import java.util.Map;

class Day18Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day18();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 3586,
        "day18test1", 132,
        "day18test2", 136,
        "day18test3", 81,
        "day18test4", 8,
        "day18test5", 86
    );
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), -1,
        "day18test6", 8,
        "day18test7", 24,
        "day18test8", 32,
        "day18test9", 72
    );
  }

  @Override
  public Map<TestPart, Map<String, String>> getDisabledTests() {
    return AdventSolutionTestUtil.disableSlowPart1(this);
  }
}
