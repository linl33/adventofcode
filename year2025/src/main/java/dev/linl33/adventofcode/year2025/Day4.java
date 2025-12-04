package dev.linl33.adventofcode.year2025;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;

public class Day4 extends AdventSolution2025<Integer, Integer> {
  static void main() {
    new Day4().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    return removeRollsOfPaper(lines);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var count = 0;
    while (true) {
      var c = removeRollsOfPaper(lines);
      count += c;
      if (c == 0) {
        return count;
      }
    }
  }

  private static int removeRollsOfPaper(final String[] grid) {
    var grid2 = Arrays.copyOf(grid, grid.length);

    var height = grid2.length;
    var width = grid2[0].length();

    var count = 0;

    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        if (grid2[y].charAt(x) != '@') {
          continue;
        }

        var neighborCount = 0;
        for (int yDelta = -1; yDelta <= 1; yDelta++) {
          for (int xDelta = -1; xDelta <= 1; xDelta++) {
            if (yDelta == 0 && xDelta == 0) {
              continue;
            }

            var y2 = y + yDelta;
            var x2 = x + xDelta;

            if (y2 < 0 || y2 >= height || x2 < 0 || x2 >= width) {
              continue;
            }

            if (grid2[y2].charAt(x2) == '@') {
              neighborCount++;
            }
          }
        }

        if (neighborCount < 4) {
          var arr = grid[y].toCharArray();
          arr[x] = 0;
          grid[y] = new String(arr);
          count++;
        }
      }
    }

    return count;
  }
}
