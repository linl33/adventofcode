package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.grid.ColumnArrayGrid;
import dev.linl33.adventofcode.lib.grid.Grid;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;

public class Day14 extends AdventSolution2022<Integer, Integer> {
  private static final int MAX_HEIGHT = 200;
  private static final int MAX_WIDTH = MAX_HEIGHT + 500;

  public static void main(String[] args) {
    new Day14().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var grid = new ColumnArrayGrid(MAX_HEIGHT, MAX_WIDTH);
    var floor = buildGrid(reader, grid);

    for (int x = 0; x < grid.width(); x++) {
      grid.set(x, floor + 1, '#');
    }

    var sand = 0;
    Point2D sandPos;

    do {
      sandPos = addSand(grid);
      sand++;
    } while (sandPos.y() < floor);

    return sand - 1;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(MAX_HEIGHT, MAX_WIDTH);
    var floor = buildGrid(reader, grid);

    for (int x = 0; x < grid.width(); x++) {
      grid.set(x, floor + 2, '#');
    }

    var sand = 0;
    Point2D sandPos;

    do {
      sandPos = addSand(grid);
      sand++;
    } while (sandPos.y() > 0);

    return sand;
  }

  private static int buildGrid(@NotNull BufferedReader reader, Grid grid) {
    var input = reader.lines().toList();

    var floor = -1;

    for (String line : input) {
      var endpoints = line.split(" -> ");

      for (int i = 0; i < endpoints.length - 1; i++) {
        var left = Arrays.stream(endpoints[i].split(",")).mapToInt(Integer::valueOf).toArray();
        var right = Arrays.stream(endpoints[i + 1].split(",")).mapToInt(Integer::valueOf).toArray();

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
        } else {
          throw new IllegalArgumentException();
        }

        floor = Math.max(floor, left[1]);
        floor = Math.max(floor, right[1]);
      }
    }

    return floor;
  }

  private static Point2D addSand(Grid grid) {
    var next = new Point2D(500, 0);
    var curr = next;

    do {
      curr = next;
      var below = curr.translate(0, 1);
      var left = curr.translate(-1, 1);
      var right = curr.translate(1, 1);

      next = below;
      if (grid.get(next) != 0) {
        next = left;

        if (grid.get(next) != 0) {
          next = right;

          if (grid.get(next) != 0) {
            next = null;
          }
        }
      }
    } while (next != null);

    grid.set(curr.x(), curr.y(), 'o');
    return curr;
  }
}
