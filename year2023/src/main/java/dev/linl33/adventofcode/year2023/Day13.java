package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day13 extends AdventSolution2023<Integer, Integer> {
  public static void main(String[] args) {
    new Day13().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    return solve(reader, 0);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    return solve(reader, 1);
  }

  private static int solve(@NotNull BufferedReader reader, int expectedErrors) {
    var groups = AdventUtil.readInputGrouped(reader).map(s -> s.toArray(String[]::new)).toArray(String[][]::new);
    var sum = 0;

    groupLoop:
    for (int i = 0; i < groups.length; i++) {
      var group = groups[i];

      var width = group[0].length();
      var height = group.length;

      // horizontal symmetry
      hLoop:
      for (int x = 0; x < width - 1; x++) {
        var errors = 0;

        for (int y = 0; y < height; y++) {
          for (int xDelta = 0; xDelta <= Math.min(x, width - x - 2); xDelta++) {
            var left = group[y].codePointAt(x - xDelta);
            var right = group[y].codePointAt(x + xDelta + 1);

            if (left != right) {
              errors++;
              if (errors > expectedErrors) {
                continue hLoop;
              }
            }
          }
        }

        if (errors == expectedErrors) {
          sum += x + 1;
          continue groupLoop;
        }
      }

      // vertical symmetry
      vLoop:
      for (int y = 0; y < height - 1; y++) {
        var errors = 0;

        for (int x = 0; x < width; x++) {
          for (int yDelta = 0; yDelta <= Math.min(y, height - y - 2); yDelta++) {
            var top = group[y - yDelta].codePointAt(x);
            var bottom = group[y + yDelta + 1].codePointAt(x);

            if (top != bottom) {
              errors++;
              if (errors > expectedErrors) {
                continue vLoop;
              }
            }
          }
        }

        if (errors == expectedErrors) {
          sum += 100 * (y + 1);
          continue groupLoop;
        }
      }
    }

    return sum;
  }
}
