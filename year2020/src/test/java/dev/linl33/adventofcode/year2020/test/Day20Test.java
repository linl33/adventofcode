package dev.linl33.adventofcode.year2020.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2020.Day20;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Day20Test implements AdventSolutionTest<Long, Integer> {
  @Override
  public AdventSolution<Long, Integer> newSolutionInstance() {
    return new Day20();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 17148689442341L,
        "day20test1", 20899048083289L
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 2009/*,
        "day20test1", 273*/ // TODO:
    );
  }
}
