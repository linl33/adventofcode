package dev.linl33.adventofcode.year2018.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2018.Day11;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test implements AdventSolutionTest<String, String> {
  @ParameterizedTest
  @CsvSource({
      "3613, 20, 54, 30",
      "18, 33, 45, 29"
  })
  void findLargestPower(int serial, int x, int y, int power) {
    Assertions.assertEquals(new Day11.GridSquare(x, y, 3, power), Day11.findHighestPower(serial, 3, null));
  }

  @ParameterizedTest
  @CsvSource({
      "3613, 233, 93, 13, 141",
      "18, 90, 269, 16, 113",
      "42, 232, 251, 12, 119"
  })
  void findLargestSize(int serial, int x, int y, int size, int power) {
    assertEquals(new Day11.GridSquare(x, y, size, power), Day11.findLargestSize(serial));
  }

  @Override
  public AdventSolution<String, String> newSolutionInstance() {
    return new Day11();
  }

  @Override
  public Map<String, String> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), "20,54");
  }

  @Override
  public Map<String, String> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), "233,93,13");
  }
}
