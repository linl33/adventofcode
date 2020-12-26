package dev.linl33.adventofcode.year2018.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2018.Day14;

import java.util.Map;

class Day14Test implements AdventSolutionTest<String, Integer> {
  @Override
  public AdventSolution<String, Integer> newSolutionInstance() {
    return new Day14();
  }

  @Override
  public Map<Object, String> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), "5101271252",
        "string:9", "5158916779",
        "string:5", "0124515891",
        "string:18", "9251071085",
        "string:2018", "5941429882"
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 20287556,
        "string:51589", 9,
        "string:01245", 5,
        "string:92510", 18,
        "string:59414", 2018
    );
  }
}
