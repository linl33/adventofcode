package dev.linl33.adventofcode.year2022.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.testlib.AdventSolutionTestUtil;
import dev.linl33.adventofcode.testlib.TestPart;
import dev.linl33.adventofcode.year2022.Day16;

import java.util.Map;

public class Day16Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day16();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        "day16", 1857,
        "day16test1", 1651
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        "day16", 2536,
        "day16test1", 1707
    );
  }

  @Override
  public Map<TestPart, Map<ResourceIdentifier, String>> getDisabledTests() {
    return AdventSolutionTestUtil.disableSlowPart2(this);
  }
}
