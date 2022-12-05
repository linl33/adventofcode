package dev.linl33.adventofcode.year2022.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2022.Day5;

import java.util.Map;

class Day5Test implements AdventSolutionTest<String, String> {
  @Override
  public AdventSolution<String, String> newSolutionInstance() {
    return new Day5();
  }

  @Override
  public Map<Object, String> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), "SBPQRSCDF",
        "day5test1", "CMZ"
    );
  }

  @Override
  public Map<Object, String> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), "RGLVRCQSB",
        "day5test1", "MCD"
    );
  }
}
