package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day11 extends AdventSolution2021<Integer, Integer> {
  private static final int GRID_WIDTH = 10;
  private static final int GRID_HEIGHT = 10;

  public static void main(String[] args) {
    new Day11().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(reader);

    var flashes = 0;

    for (int step = 1; step <= 100; step++) {
      incrementGrid(grid.array());
      flashes += simulateFlashing(grid.array());
    }

    return flashes;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(reader);
    var step = 0;
    int flashes;

    do {
      incrementGrid(grid.array());

      step++;
      flashes = simulateFlashing(grid.array());
    } while (flashes != GRID_WIDTH * GRID_HEIGHT);

    return step;
  }

  private static void incrementGrid(@NotNull int[] grid) {
    for (int i = 0; i < GRID_WIDTH * GRID_HEIGHT; i++) {
      grid[i] = grid[i] <= 0 ? '1' : grid[i] + 1;
    }
  }

  private static int simulateFlashing(@NotNull int[] grid) {
    var flashes = 0;

    var toVisit = new int[GRID_WIDTH * GRID_HEIGHT];
    var toVisitNext = new int[GRID_WIDTH * GRID_HEIGHT];
    int toVisitPointer = 0;

    for (int i = 0; i < GRID_WIDTH * GRID_HEIGHT; i++) {
      if (grid[i] > '9') {
        grid[i] = Integer.MIN_VALUE;
        flashes++;

        toVisitPointer = flashNeighbors(grid, i, toVisit, toVisitPointer);
      }
    }

    while (toVisitPointer != 0) {
      var toVisitPointerCopy = toVisitPointer;
      toVisitPointer = 0;

      for (int i = 0; i < toVisitPointerCopy; i++) {
        var pos = toVisit[i];

        if (grid[pos] > '9') {
          grid[pos] = Integer.MIN_VALUE;
          flashes++;

          toVisitPointer = flashNeighbors(grid, pos, toVisitNext, toVisitPointer);
        }
      }

      var tmp = toVisit;
      toVisit = toVisitNext;
      toVisitNext = tmp;
    }

    return flashes;
  }

  private static int flashNeighbors(@NotNull int[] grid, int pos, @NotNull int[] toVisit, int toVisitPointer) {
    var row = pos / GRID_WIDTH;
    var col = pos % GRID_WIDTH;

    for (int neighborRow = Math.max(0, row - 1); neighborRow < Math.min(row + 2, GRID_HEIGHT); neighborRow++) {
      for (int neighborCol = Math.max(0, col - 1); neighborCol < Math.min(col + 2, GRID_WIDTH); neighborCol++) {
        var neighborPos = neighborRow * GRID_WIDTH + neighborCol;
        var neighborVal = ++grid[neighborPos];

        if (neighborVal == ('9' + 1)) {
          toVisit[toVisitPointer++] = neighborPos;
        }
      }
    }

    return toVisitPointer;
  }
}
