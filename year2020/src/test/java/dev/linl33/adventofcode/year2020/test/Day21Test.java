package dev.linl33.adventofcode.year2020.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2020.Day21;

import java.util.Map;

class Day21Test implements AdventSolutionTest<Integer, String> {
  @Override
  public AdventSolution<Integer, String> newSolutionInstance() {
    return new Day21();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 2556,
        "day21test1", 5
    );
  }

  @Override
  public Map<Object, String> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), "vcckp,hjz,nhvprqb,jhtfzk,mgkhhc,qbgbmc,bzcrknb,zmh",
        "day21test1", "mxmxvkd,sqjhc,fvjkl"
    );
  }
}
