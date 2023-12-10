package dev.linl33.adventofcode.year2023;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day9 extends AdventSolution2023<Long, Long> {
  private static final int[][] BINOMIAL_COEFFICIENTS = new int[][] {
    null,
    null,
    null,
    null,
    null,
    new int[] { 1, 5, 10, 10, 5, 1 },
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    new int[] { 1, 20, 190, 1140, 4845, 15504, 38760, 77520, 125970, 167960, 184756, 167960, 125970, 77520, 38760, 15504, 4845, 1140, 190, 20, 1 },
  };

  private static final int[] NEG_ODD = new int[] {
    1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1, -1,
  };

  public static void main(String[] args) {
    new Day9().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    return solve(reader, -1);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    return solve(reader, 0);
  }

  private static long solve(@NotNull BufferedReader reader, int dir) {
    var lines = reader.lines().toArray(String[]::new);

    var oasisHistoryDepth = 0;
    var spaceIdx = 0;
    do {
      oasisHistoryDepth++;
      spaceIdx = lines[0].indexOf(' ', spaceIdx + 1);
    } while (spaceIdx != -1);

    var sum = 0L;
    var oasisValueHistory = new int[oasisHistoryDepth];
    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];

      var lineIdx = 0;
      for (int j = 0; j < oasisValueHistory.length - 1; j++) {
        var endIdx = line.indexOf(' ', lineIdx);
        oasisValueHistory[j] = Integer.parseInt(line, lineIdx, endIdx, 10);
        lineIdx = endIdx + 1;
      }
      oasisValueHistory[oasisValueHistory.length - 1] = Integer.parseInt(line, lineIdx, line.length(), 10);

      // calculate (oasisValueHistory.length - 1)th order finite difference
      // https://en.wikipedia.org/wiki/Finite_difference
      // TODO: vectorize
      var val = 0;
      if (dir == -1) {
        for (int j = 1; j < oasisValueHistory.length; j++) {
          val += oasisValueHistory[j] * BINOMIAL_COEFFICIENTS[oasisHistoryDepth - 1][j - 1] * NEG_ODD[j - 1 + oasisValueHistory.length % 2];
        }
      }

      if (dir == 0) {
        for (int j = 0; j < oasisValueHistory.length - 1; j++) {
          val += oasisValueHistory[j] * BINOMIAL_COEFFICIENTS[oasisHistoryDepth - 1][j + 1] * NEG_ODD[j];
        }
      }

      sum += val;
    }

    return sum;
  }
}
