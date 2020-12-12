package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day4;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day4Test implements AdventSolutionTest<Long, Long> {
  @ParameterizedTest
  @CsvSource({
      "111111, true",
      "123789, false"
  })
  void checkCriteriaPart1(int input, boolean expected) {
    Assertions.assertEquals(expected, Day4.checkCriteriaPart1(input));
  }

  @ParameterizedTest
  @CsvSource({
      "112233, true",
      "123444, false",
      "111122, true"
  })
  void checkCriteriaPart2(int input, boolean expected) {
    assertEquals(expected, Day4.checkCriteriaPart2(input));
  }

  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day4();
  }

  @Override
  public Map<String, Long> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 1650L);
  }

  @Override
  public Map<String, Long> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 1129L);
  }
}
