package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day10;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day10();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 263,
        "day10test1", 33,
        "day10test2", 35,
        "day10test3", 41,
        "day10test4", 210
    );
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 1110,
        "day10test4", 802
    );
  }

  @Test
  void part2Detailed(Day10 solutionInstance) {
    var asteroidList = solutionInstance.run(Day10::part2Detailed, "day10test4");

    assertEquals(299, asteroidList.size());
    assertEquals(new Point2D(11, 12), asteroidList.get(0));
    assertEquals(new Point2D(12, 1), asteroidList.get(1));
    assertEquals(new Point2D(12, 2), asteroidList.get(2));
    assertEquals(new Point2D(12, 8), asteroidList.get(9));
    assertEquals(new Point2D(16, 0), asteroidList.get(19));
    assertEquals(new Point2D(16, 9), asteroidList.get(49));
    assertEquals(new Point2D(10, 16), asteroidList.get(99));
    assertEquals(new Point2D(9, 6), asteroidList.get(198));
    assertEquals(new Point2D(8, 2), asteroidList.get(199));
    assertEquals(new Point2D(10, 9), asteroidList.get(200));
    assertEquals(new Point2D(11, 1), asteroidList.get(298));
  }
}
