package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day1Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day1();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 3291760);
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 4934767);
  }

  @ParameterizedTest(name = "mass {0} => fuel {1}")
  @CsvSource({
      "12, 2",
      "14, 2",
      "1969, 654",
      "100756, 33583"
  })
  void massToFuel(int mass, int fuel) {
    assertEquals(fuel, Day1.massToFuel(mass));
  }

  @ParameterizedTest(name = "mass {0} => total fuel {1}")
  @CsvSource({
      "14, 2",
      "1969, 966",
      "100756, 50346"
  })
  void massToTotalFuel(int mass, int fuel) {
    assertEquals(fuel, Day1.massToTotalFuel(mass));
  }
}
