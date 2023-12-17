package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.graph.GraphUtil;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.function.ToIntFunction;

public class Day17 extends AdventSolution2023<Integer, Integer> {
  private static final int NORTH = 0b00;
  private static final int EAST = 0b01;
  private static final int SOUTH = 0b10;
  private static final int WEST = 0b11;

  public static void main(String[] args) {
    new Day17().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    return minHeatLoss(reader, 0, 3);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    return minHeatLoss(reader, 4, 10);
  }

  private static int minHeatLoss(BufferedReader reader, int turnMin, int turnMax) {
    var grid = new RowArrayGrid(reader);
    var width = grid.width();
    var height = grid.height();

    var min = Integer.MAX_VALUE;

    var starts = new Crucible[] { new Crucible(0, 0, EAST, 0), new Crucible(0, 0, SOUTH, 0) };
    for (var start : starts) {
      var path = GraphUtil.aStar(
        start,
        new Crucible(width - 1, height - 1, -1, -1),
        c -> {
          var candidateDir = new int[] { c.heading, -1, -1 };
          if (c.count >= turnMin) {
            candidateDir[1] = c.heading ^ 0b01;
            candidateDir[2] = c.heading ^ 0b11;
          }

          if (c.count >= turnMax) {
            candidateDir[0] = -1;
          }

          var neighbors = new ArrayList<Crucible>();
          for (int dir : candidateDir) {
            if (dir == -1) {
              continue;
            }

            var newPos = switch (dir) {
              case NORTH -> new Crucible(c.x, c.y - 1, -1, -1);
              case EAST -> new Crucible(c.x + 1, c.y, -1, -1);
              case SOUTH -> new Crucible(c.x, c.y + 1, -1, -1);
              case WEST -> new Crucible(c.x - 1, c.y, -1, -1);
              default -> throw new IllegalStateException();
            };

            if (newPos.x < 0 || newPos.x >= width) {
              continue;
            }

            if (newPos.y < 0 || newPos.y >= height) {
              continue;
            }

            var dirCount = dir == c.heading ? c.count + 1 : 1;
            if (newPos.x == (width - 1) && newPos.y == (height - 1) && dirCount >= turnMin) {
              neighbors.add(newPos);
            } else {
              newPos = new Crucible(newPos.x, newPos.y, dir, dirCount);
              neighbors.add(newPos);
            }
          }

          return neighbors;
        },
        (ToIntFunction<Crucible>) c -> 2 * (width - c.x + height - c.y),
        (_, right) -> grid.get(right.x, right.y) - '0'
      );

      min = Math.min(min, path.orElseThrow().length());
    }

    return min;
  }

  private record Crucible(int x, int y, int heading, int count) {}
}
