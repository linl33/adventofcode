package dev.linl33.adventofcode.year2018.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2018.Day9;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day9Test implements AdventSolutionTest<Long, Long> {
  @ParameterizedTest
  @CsvSource({
      "10, 1618, 8317",
      "13, 7999, 146373",
      "17, 1104, 2764",
      "21, 6111, 54718",
      "30, 5807, 37305"
  })
  void scoreGame(int players, int lastMarble, int expected) {
    Assertions.assertEquals(expected, Day9.scoreGame(players, lastMarble));
  }

  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day9();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 370210L);
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 3101176548L);
  }
}
