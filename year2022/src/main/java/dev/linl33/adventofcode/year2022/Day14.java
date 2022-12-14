package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.grid.Grid;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.regex.Pattern;

public class Day14 extends AdventSolution2022<Integer, Integer> {
  private static final int MAX_HEIGHT = 200;
  private static final int SAND_SOURCE_X = 100;
  private static final int SAND_SOURCE_X_DELTA = SAND_SOURCE_X - 500;

  private static final int MAX_WIDTH = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(SAND_SOURCE_X * 2 + 1 - 1));

  private static final Pattern ARROW_REGEX = Pattern.compile(" -> ");

  public static void main(String[] args) {
    new Day14().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(MAX_HEIGHT, MAX_WIDTH);
    var gridArray = grid.array();
    var floor = buildGrid(reader, grid);

    var stack = new int[MAX_HEIGHT];
    var stackPointer = 0;
    stack[0] = SAND_SOURCE_X;

    var sand = 0;
    while (stackPointer >= 0) {
      var currIdx = stack[stackPointer];

      if (currIdx > floor * grid.width()) {
        return sand;
      }

      var nextIdx = currIdx + grid.width();
      if (gridArray[nextIdx] == 0) {
        stack[++stackPointer] = nextIdx;
        continue;
      }

      nextIdx = nextIdx - 1;
      if (gridArray[nextIdx] == 0) {
        stack[++stackPointer] = nextIdx;
        continue;
      }

      nextIdx = nextIdx + 2;
      if (gridArray[nextIdx] == 0) {
        stack[++stackPointer] = nextIdx;
        continue;
      }

      stackPointer--;
      gridArray[currIdx] = 'o';
      sand++;
    }

    return sand;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(MAX_HEIGHT, MAX_WIDTH);
    var gridArray = grid.array();
    var floor = buildGrid(reader, grid);

    var minX = SAND_SOURCE_X;
    var maxX = SAND_SOURCE_X;

    for (int y = floor, offset = floor * grid.width(); y > 0; y--, offset -= grid.width()) {
      for (int x = SAND_SOURCE_X - y + 1, p = offset + x; x < minX; x++, p++) {
        if (gridArray[p] != 0) {
          minX = x;
          break;
        }
      }

      for (int x = SAND_SOURCE_X + y - 1, p = offset + x; x > maxX; x--, p--) {
        if (gridArray[p] != 0) {
          maxX = x;
          break;
        }
      }
    }

    var stack = new int[MAX_HEIGHT];
    var stackPointer = 0;
    stack[0] = SAND_SOURCE_X;

    var sand = 0;
    while (stackPointer >= 0) {
      var currIdx = stack[stackPointer];

      if (currIdx < (floor + 1) * grid.width()) {
        var nextIdx = currIdx + grid.width();
        if (gridArray[nextIdx] == 0) {
          stack[++stackPointer] = nextIdx;
          continue;
        }

        var x = currIdx % grid.width();
        nextIdx = nextIdx - 1;
        if (x >= minX && gridArray[nextIdx] == 0) {
          stack[++stackPointer] = nextIdx;
          continue;
        }

        nextIdx = nextIdx + 2;
        if (x <= maxX && gridArray[nextIdx] == 0) {
          stack[++stackPointer] = nextIdx;
          continue;
        }
      }

      stackPointer--;
      gridArray[currIdx] = 'o';
      sand++;

      if (currIdx < grid.width()) {
        break;
      }
    }

    var sandMin = SAND_SOURCE_X - (floor + 1);
    var sandMax = SAND_SOURCE_X + (floor + 1);

    var leftWidth = (minX - 2) - sandMin + 1;
    var rightWidth = sandMax - (maxX + 2) + 1;

    var leftArea = leftWidth * (leftWidth + 1) / 2;
    var rightArea = rightWidth * (rightWidth + 1) / 2;

    return sand + leftArea + rightArea;
  }

  private static int buildGrid(@NotNull BufferedReader reader, Grid grid) {
    var input = reader.lines().toArray(String[]::new);

    var floor = 0;
    var left = new int[2];
    var right = new int[2];

    for (var line : input) {
      var endpoints = ARROW_REGEX.split(line);
      parsePair(endpoints[0], left);
      var next = right;

      for (int i = 1; i < endpoints.length; i++) {
        parsePair(endpoints[i], next);

        if (left[0] == right[0]) {
          var x = left[0];
          var yMin = Math.min(left[1], right[1]);
          var yMax = Math.max(left[1], right[1]);

          for (int y = yMin; y <= yMax; y++) {
            grid.set(x, y, '#');
          }
        } else if (left[1] == right[1]) {
          var y = left[1];
          var xMin = Math.min(left[0], right[0]);
          var xMax = Math.max(left[0], right[0]);

          for (int x = xMin; x <= xMax; x++) {
            grid.set(x, y, '#');
          }

          floor = Math.max(floor, y);
        } else {
          throw new IllegalArgumentException();
        }

        next = next == left ? right : left;
      }
    }

    return floor;
  }

  private static void parsePair(String pairStr, int[] pair) {
    pair[0] = Integer.parseInt(pairStr, 0, 3, 10) + SAND_SOURCE_X_DELTA;
    pair[1] = Integer.parseInt(pairStr, 4, pairStr.length(), 10);
  }
}
