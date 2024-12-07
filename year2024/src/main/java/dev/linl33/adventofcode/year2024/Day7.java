package dev.linl33.adventofcode.year2024;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.function.LongBinaryOperator;

public class Day7 extends AdventSolution2024<Long, Long> {
  private static final LongBinaryOperator[] OPERATORS = {
    Long::sum,
    (left, right) -> left * right,
    (left, right) -> left * nextPowerOfTen(right) + right,
  };

  public static void main() {
    new Day7().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    return sum(reader, 2);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    return sum(reader, 3);
  }

  private static long sum(BufferedReader reader, int operators) {
    return reader
      .lines()
      .mapToLong(line -> {
        var sepIdx = line.indexOf(':');
        var value = Long.parseLong(line, 0, sepIdx, 10);
        var nums = Arrays.stream(line.substring(sepIdx + 2).split(" ")).mapToLong(Long::parseLong).toArray();

        return testEquation(value, nums, nums[0], 1, operators) ? value : 0;
      })
      .sum();
  }

  private static boolean testEquation(long target, long[] nums, long currVal, int startIdx, int operators) {
    // TODO: dfs can be traversed from the end, which would allow more aggressive pruning

    if (startIdx == nums.length) {
      return currVal == target;
    }

    for (var i = 0; i < operators; i++) {
      var nextVal = OPERATORS[i].applyAsLong(currVal, nums[startIdx]);
      if (nextVal > target && nums[startIdx] != 1 && currVal != 1) {
        return false;
      }

      if (testEquation(target, nums, nextVal, startIdx + 1, operators)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Find the next power of 10.
   */
  private static long nextPowerOfTen(long num) {
    // Conditionals are much faster than log, i.e. Math.pow(10, Math.floor(Math.log10(right)) + 1)

    if (num < 10) {
      return 10;
    }

    if (num < 100) {
      return 100;
    }

    if (num < 1000) {
      return 1000;
    }

    return 10_000;
  }
}
