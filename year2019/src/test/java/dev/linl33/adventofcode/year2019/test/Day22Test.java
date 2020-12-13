package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day22;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class Day22Test implements AdventSolutionTest<Integer, Long> {
  @Override
  public AdventSolution<Integer, Long> newSolutionInstance() {
    return new Day22();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 6061);
  }

  @Override
  public Map<String, Long> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 79490866971571L);
  }

  @ParameterizedTest
  @CsvSource({
      "day22test1, 0 3 6 9 2 5 8 1 4 7",
      "day22test2, 3 0 7 4 1 8 5 2 9 6",
      "day22test3, 6 3 0 7 4 1 8 5 2 9",
      "day22test4, 9 2 5 8 1 4 7 0 3 6"
  })
  void testShuffleWholeDeck(String resource, String expected, AdventSolution<Integer, Long> day22) {
    assertArrayEquals(
        Arrays
            .stream(expected.split(" "))
            .mapToInt(Integer::parseInt)
            .toArray(),
        day22.run(Day22Test::shuffleWholeDeck, resource)
    );
  }

  static int[] shuffleWholeDeck(Day22 instance, BufferedReader reader) {
    return instance.shuffleWholeDeck(reader, 10);
  }
}
