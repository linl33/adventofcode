package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Day25 extends AdventSolution2024<Integer, Void> {
  private static final int WIDTH = 5;
  private static final int HEIGHT = 7;
  private static final String FULL_ROW = "#".repeat(WIDTH);

  public static void main() {
    new Day25().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var groups = AdventUtil.readInputGrouped(reader).map(x -> x.collect(Collectors.joining())).toArray(String[]::new);

    var keys = new ArrayList<int[]>();
    var locks = new ArrayList<int[]>();

    for (var i = 0; i < groups.length; i++) {
      var g = groups[i];

      var shape = new int[WIDTH];
      for (var row = 0; row < HEIGHT; row++) {
        for (var col = 0; col < WIDTH; col++) {
          if (g.charAt(row * WIDTH + col) == '#') {
            shape[col] += 1;
          }
        }
      }

      (g.startsWith(FULL_ROW) ? locks : keys).add(shape);
    }

    var count = 0;
    for (var i = 0; i < locks.size(); i++) {
      var lock = locks.get(i);

      for (var j = 0; j < keys.size(); j++) {
        var key = keys.get(j);

        var fits = true;
        for (var k = 0; k < WIDTH; k++) {
          if (lock[k] + key[k] > HEIGHT) {
            fits = false;
            break;
          }
        }

        if (fits) {
          count += 1;
        }
      }
    }

    return count;
  }

  @Override
  public Void part2(@NotNull BufferedReader reader) {
    throw new UnsupportedOperationException();
  }
}
