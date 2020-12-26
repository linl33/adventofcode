package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.testlib.AdventSolutionTestUtil;
import dev.linl33.adventofcode.testlib.TestPart;
import dev.linl33.adventofcode.year2019.Day16;

import java.util.Map;

class Day16Test implements AdventSolutionTest<String, String> {
  @Override
  public AdventSolution<String, String> newSolutionInstance() {
    return new Day16();
  }

  @Override
  public Map<Object, String> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), "30550349",
        "string:80871224585914546619083218645595", "24176176",
        "string:19617804207202209144916044189917", "73745418",
        "string:69317163492948606335995924319873", "52432133"
    );
  }

  @Override
  public Map<Object, String> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), "62938399",
        "string:03036732577212944063491565474664", "84462026",
        "string:02935109699940807407585447034323", "78725270",
        "string:03081770884921959731165446850517", "53553731"
    );
  }

  @Override
  public Map<TestPart, Map<ResourceIdentifier, String>> getDisabledTests() {
    return AdventSolutionTestUtil.disableSlowPart2(this);
  }
}
