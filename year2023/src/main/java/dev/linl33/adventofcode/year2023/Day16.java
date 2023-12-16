package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.grid.Grid;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.point.Point3D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayDeque;
import java.util.HashSet;

public class Day16 extends AdventSolution2023<Integer, Integer> {
  private static final int NORTH = 0;
  private static final int EAST = 1;
  private static final int SOUTH = 2;
  private static final int WEST = 3;

  public static void main(String[] args) {
    new Day16().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(reader);
    return count(grid, new Point3D(-1, 0, EAST));
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(reader);
    var width = grid.width();
    var height = grid.height();

    var max = -1;

    var xSet = new int[] { -1, width };
    for (int y = 0; y < height; y++) {
      var start = new Point3D(xSet[0], y, EAST);
      var start2 = new Point3D(xSet[1], y, WEST);

      max = Math.max(max, count(grid, start));
      max = Math.max(max, count(grid, start2));
    }

    var ySet = new int[] { -1, height };
    for (int x = 0; x < width; x++) {
      var start = new Point3D(x, ySet[0], SOUTH);
      var start2 = new Point3D(x, ySet[1], NORTH);

      max = Math.max(max, count(grid, start));
      max = Math.max(max, count(grid, start2));
    }

    return max;
  }

  private static int count(Grid grid, Point3D start) {
    var visited = new HashSet<Point2D>();
    var vectorVisited = new HashSet<Point3D>();

    var stack = new ArrayDeque<Point3D>();
    stack.push(start);

    var sum = 0;
    while (!stack.isEmpty()) {
      var curr = stack.pop();
      var vAdded = vectorVisited.add(curr);
      if (!vAdded) {
        continue;
      }

      var next = switch (curr.z()) {
        case NORTH -> curr.translate(0, -1, 0);
        case EAST -> curr.translate(1, 0, 0);
        case SOUTH -> curr.translate(0, 1, 0);
        case WEST -> curr.translate(-1, 0, 0);
        default -> throw new IllegalStateException();
      };

      if (!grid.isWithinBounds(next.x(), next.y())) {
        continue;
      }

      var added = visited.add(new Point2D(next.x(), next.y()));
      if (added) {
        sum++;
      }

      var nextVal = grid.get(next.x(), next.y());
      if (nextVal == '.') {
        stack.push(next);
      } else if (nextVal == '\\') {
        if (next.z() == NORTH) {
          // north -> west
          next = next.translate(0, 0, 3);
        } else if (next.z() == EAST) {
          // east -> south
          next = next.translate(0, 0, 1);
        } else if (next.z() == SOUTH) {
          // south -> east
          next = next.translate(0, 0, -1);
        } else if (next.z() == WEST) {
          // west -> north
          next = next.translate(0, 0, -3);
        }
        stack.push(next);
      } else if (nextVal == '/') {
        if (next.z() == NORTH) {
          // north -> east
          next = next.translate(0, 0, 1);
        } else if (next.z() == EAST) {
          // east -> north
          next = next.translate(0, 0, -1);
        } else if (next.z() == SOUTH) {
          // south -> west
          next = next.translate(0, 0, 1);
        } else if (next.z() == WEST) {
          // west -> south
          next = next.translate(0, 0, -1);
        }
        stack.push(next);
      } else if (nextVal == '-') {
        if (next.z() == EAST || next.z() == WEST) {
          stack.push(next);
          continue;
        }

        stack.push(new Point3D(next.x(), next.y(), 1));
        stack.push(new Point3D(next.x(), next.y(), 3));
      } else if (nextVal == '|') {
        if (next.z() == NORTH || next.z() == SOUTH) {
          stack.push(next);
          continue;
        }

        stack.push(new Point3D(next.x(), next.y(), 0));
        stack.push(new Point3D(next.x(), next.y(), 2));
      }
    }

    return sum;
  }
}
