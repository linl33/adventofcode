package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import dev.linl33.adventofcode.lib.util.MathUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.function.IntUnaryOperator;


public class Day7 extends AdventSolution2021<Integer, Integer> {
  private static final char INPUT_DELIMITER = ',';

  public static void main(String[] args) {
    new Day7().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var input = AdventUtil.readDelimiterSeperatedInts(reader.readLine(), INPUT_DELIMITER, 1000);
    var median = MathUtil.median(input);

    return solve(
        input,
        new int[] { median },
        IntUnaryOperator.identity()
    );
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var input = AdventUtil.readDelimiterSeperatedInts(reader.readLine(), INPUT_DELIMITER, 1000);
    var mean = MathUtil.mean(input);

    return solve(
        input,
        new int[] { mean, mean + 1 },
        n -> n * (n + 1) / 2 // triangular number
    );
  }

  private static int solve(@NotNull int[] crabPositions,
                           @NotNull int[] guesses,
                           @NotNull IntUnaryOperator fuelFunction) {
    var minFuel = Integer.MAX_VALUE;
    for (int guess : guesses) {
      var fuel = 0;
      for (int pos : crabPositions) {
        fuel += fuelFunction.applyAsInt(Math.abs(pos - guess));
      }

      if (fuel < minFuel) {
        minFuel = fuel;
      } else {
        // the cost curve is convex,
        // so once the total cost stops decreasing, we can return
        return minFuel;
      }
    }

    return minFuel;
  }
}
