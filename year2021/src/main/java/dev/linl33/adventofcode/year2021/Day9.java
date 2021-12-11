package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;

public class Day9 extends AdventSolution2021<Integer, Integer> {
  public static void main(String[] args) {
    new Day9().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var width = lines[0].length();
    var height = lines.length;
    var grid = new RowArrayGrid(height + 2, width + 2);
    Arrays.fill(grid.array(), '9');

    for (int i = 0; i < height; i++) {
      var line = lines[i];

      for (int j = 0; j < width; j++) {
        grid.set(j + 1, i + 1, line.codePointAt(j));
      }
    }

    var sum = 0;

    for (int row = 1; row <= height; row++) {
      for (int col = 1; col <= width; col++) {
        var center = grid.get(col, row);
        if (center == '9') {
          continue;
        }

        var left = grid.get(col - 1, row);
        var right = grid.get(col + 1, row);
        var up = grid.get(col, row - 1);
        var down = grid.get(col, row + 1);

        if (center < left && center < right && center < up && center < down) {
          sum += center - '0' + 1;
        }
      }
    }

    return sum;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var width = lines[0].length();
    var height = lines.length;
    var grid = new RowArrayGrid(height + 2, width + 2);
    Arrays.fill(grid.array(), '9');

    for (int i = 0; i < height; i++) {
      var line = lines[i];

      for (int j = 0; j < width; j++) {
        grid.set(j + 1, i + 1, line.codePointAt(j));
      }
    }

    var lowPoints = new int[width * height];
    var lowPointsCount = 0;

    for (int row = 1; row <= height; row++) {
      for (int col = 1; col <= width; col++) {
        var center = grid.get(col, row);
        if (center == '9') {
          continue;
        }

        var left = grid.get(col - 1, row);
        var right = grid.get(col + 1, row);
        var up = grid.get(col, row - 1);
        var down = grid.get(col, row + 1);

        if (center < left && center < right && center < up && center < down) {
          lowPoints[lowPointsCount++] = row * (width + 2) + col;
        }
      }
    }

    for (int i = 0; i < lowPointsCount; i++) {
      var stack = new int[width * height];
      var stackPointer = 1;

      stack[0] = lowPoints[i];

      var area = 0;

      while (stackPointer > 0) {
        var top = stack[--stackPointer];
        if (grid.array()[top] > '8') {
          continue;
        }

        area++;
        var row = top / (width + 2);
        var col = top % (width + 2);

        var left = grid.get(col - 1, row);
        var right = grid.get(col + 1, row);
        var up = grid.get(col, row - 1);
        var down = grid.get(col, row + 1);

        if (left < '9') {
          stack[stackPointer++] = row * (width + 2) + col - 1;
        }
        if (right < '9') {
          stack[stackPointer++] = row * (width + 2) + col + 1;
        }
        if (up < '9') {
          stack[stackPointer++] = (row - 1) * (width + 2) + col;
        }
        if (down < '9') {
          stack[stackPointer++] = (row + 1) * (width + 2) + col;
        }

        grid.set(col, row, '9');
      }

      lowPoints[i] = area;
    }

    Arrays.sort(lowPoints, 0, lowPointsCount);
    return lowPoints[lowPointsCount - 1] * lowPoints[lowPointsCount - 2] * lowPoints[lowPointsCount - 3];
  }
}
