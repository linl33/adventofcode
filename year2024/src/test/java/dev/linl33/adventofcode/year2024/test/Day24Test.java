package dev.linl33.adventofcode.year2024.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2024.Day24;

import java.util.Map;

public class Day24Test implements AdventSolutionTest<Long, String> {
  @Override
  public AdventSolution<Long, String> newSolutionInstance() {
    return new Day24();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 47666458872582L,
         "day24test1", 2024L
    );
  }

  @Override
  public Map<Object, String> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), "dnt,gdf,gwc,jst,mcm,z05,z15,z30"
    );
  }
}
