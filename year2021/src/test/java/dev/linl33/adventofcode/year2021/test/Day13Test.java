package dev.linl33.adventofcode.year2021.test;

import dev.linl33.adventofcode.lib.grid.ArrayGrid;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2021.Day13;

import java.util.Map;

class Day13Test implements AdventSolutionTest<Integer, ArrayGrid> {
  @Override
  public AdventSolution<Integer, ArrayGrid> newSolutionInstance() {
    return new Day13();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 647,
        "day13test1", 17
    );
  }

  @Override
  public Map<Object, ArrayGrid> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), new RowArrayGrid(new int[] { 35, 0, 0, 35, 0, 35, 35, 35, 35, 0, 0, 0, 35, 35, 0, 35, 0, 0, 35, 0, 0, 0, 35, 35, 0, 35, 35, 35, 0, 0, 0, 35, 35, 0, 0, 0, 0, 35, 35, 0, 35, 0, 0, 35, 0, 35, 0, 0, 0, 0, 0, 0, 0, 35, 0, 35, 0, 0, 35, 0, 0, 0, 0, 35, 0, 35, 0, 0, 35, 0, 35, 0, 0, 35, 0, 0, 0, 0, 35, 0, 35, 35, 35, 35, 0, 35, 35, 35, 0, 0, 0, 0, 0, 35, 0, 35, 35, 35, 35, 0, 0, 0, 0, 35, 0, 35, 0, 0, 35, 0, 35, 0, 0, 0, 0, 0, 0, 0, 35, 0, 35, 0, 0, 35, 0, 35, 0, 0, 0, 0, 0, 0, 0, 35, 0, 35, 0, 0, 35, 0, 0, 0, 0, 35, 0, 35, 35, 35, 0, 0, 35, 0, 0, 0, 0, 0, 0, 0, 35, 0, 35, 0, 0, 35, 0, 35, 0, 0, 0, 0, 35, 0, 0, 35, 0, 35, 0, 0, 35, 0, 35, 0, 0, 35, 0, 35, 0, 35, 0, 0, 35, 0, 0, 35, 0, 35, 0, 0, 35, 0, 35, 0, 0, 35, 0, 35, 35, 35, 35, 0, 0, 35, 35, 0, 0, 35, 0, 0, 35, 0, 0, 35, 35, 0, 0, 35, 0, 0, 35, 0, 0, 35, 35, 0, 0, 0, 35, 35, 0, 0 }, 6, 40),
        "day13test1", new RowArrayGrid(new int[] { 35, 35, 35, 35, 35, 35, 0, 0, 0, 35, 35, 0, 0, 0, 35, 35, 0, 0, 0, 35, 35, 35, 35, 35, 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, 7, 5)
    );
  }
}
