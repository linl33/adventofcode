package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.grid.Grid;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Day3 extends AdventSolution2023<Integer, Integer> {
  public static void main(String[] args) {
    new Day3().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var grid = new RowArrayGrid(reader);
    var sum = 0;

    for (int y = 0; y < grid.height(); y++) {
      for (int x = 0; x < grid.width(); x++) {
        var curr = grid.get(x, y);
        if (curr >= '0' && curr <= '9') {
          var numberEnd = seekEnd(grid, x, y);
          var hasSymbol = checkEdge(grid, x, y, -1);

          var i = x;
          while (!hasSymbol && i < numberEnd) {
            hasSymbol = checkEdge(grid, i++, y, 0);
          }

          if (!hasSymbol) {
            hasSymbol = checkEdge(grid, numberEnd - 1, y, 1);
          }

          if (hasSymbol) {
            sum += parseNumber(grid, y, x, numberEnd);
          }

          x = numberEnd;
        }
      }
    }

    return sum;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var grid = new RowArrayGrid(reader);
    var sum = 0;

    @SuppressWarnings("unchecked")
    var numbers = (ArrayList<int[]>[]) Array.newInstance(ArrayList.class, grid.height());

    for (int y = 0; y < grid.height(); y++) {
      numbers[y] = new ArrayList<>();
      for (int x = 0; x < grid.width(); x++) {
        var curr = grid.get(x, y);
        if (curr >= '0' && curr <= '9') {
          var numberEnd = seekEnd(grid, x, y);
          numbers[y].add(new int[] { parseNumber(grid, y, x, numberEnd), x, numberEnd });
          x = numberEnd;
        }
      }
    }

    var partIds = new HashMap<Integer, Integer>();

    for (int y = 0; y < grid.height(); y++) {
      for (int x = 0; x < grid.width(); x++) {
        var curr = grid.get(x, y);
        if (curr == '*') {
          partIds.clear();

          for (int yDelta = -1; yDelta <= 1; yDelta++) {
            for (int xDelta = -1; xDelta <= 1; xDelta++) {
              if (yDelta == 0 && xDelta == 0) {
                continue;
              }

              var currX = x + xDelta;
              var currY = y + yDelta;

              if (!grid.isWithinBounds(currX, currY)) {
                continue;
              }

              var rowNumbers = numbers[currY];
              for (int i = 0; i < rowNumbers.size(); i++) {
                var number = rowNumbers.get(i);
                var numXStart = number[1];
                var numXEnd = number[2];

                if (currX >= numXStart && currX < numXEnd) {
                  partIds.put(currY * 100 + i, number[0]);
                }
              }
            }
          }

          if (partIds.size() == 2) {
            var values = partIds.values().iterator();
            sum += values.next() * values.next();
          }
        }
      }
    }

    return sum;
  }

  private static int seekEnd(Grid grid, int x, int y) {
    int curr;
    do {
      curr = grid.get(x, y);
    } while (curr >= '0' && curr <= '9' && ++x < grid.width());

    return x;
  }

  private static int parseNumber(Grid grid, int y, int xStart, int xEnd) {
    int num = grid.get(xStart++, y) - '0';
    for (; xStart < xEnd; xStart++) {
      num *= 10;
      num += grid.get(xStart, y) - '0';
    }

    return num;
  }

  private static boolean isSymbol(Grid grid, int x, int y) {
    var val = grid.getOrDefault(x, y, '.');
    return val != '.' && (val < '0' || val > '9');
  }

  private static boolean checkEdge(Grid grid, int x, int y, int xOffset) {
    for (int yDelta = -1; yDelta <= 1; yDelta++) {
      var xCurr = x + xOffset;
      var yCurr = y + yDelta;
      if (grid.isWithinBounds(xCurr, yCurr) && isSymbol(grid, xCurr, yCurr)) {
        return true;
      }
    }

    return false;
  }
}
