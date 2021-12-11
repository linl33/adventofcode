package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day11 extends AdventSolution2021<Integer, Integer> {
  public static void main(String[] args) {
    new Day11().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(reader);
    var width = grid.width();
    var height = grid.height();

    var flashes = 0;
    boolean flashed;

    for (int step = 1; step <= 100; step++) {
      for (int i = 0; i < width * height; i++) {
        grid.array()[i] = grid.array()[i] <= 0 ? '1' : grid.array()[i] + 1;
      }

      do {
        flashed = false;
        for (int row = 0; row < height; row++) {
          for (int col = 0; col < width; col++) {
            if (grid.get(col, row) > '9') {
              grid.set(col, row, Integer.MIN_VALUE);
              flashed = true;
              flashes++;

              for (int rowDelta = Math.max(0, row - 1); rowDelta < Math.min(row + 2, height); rowDelta++) {
                for (int colDelta = Math.max(0, col - 1); colDelta < Math.min(col + 2, width); colDelta++) {
                  grid.set(colDelta, rowDelta, grid.get(colDelta, rowDelta) + 1);
                }
              }
            }
          }
        }
      } while (flashed);
    }

    return flashes;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(reader);
    var width = grid.width();
    var height = grid.height();
    var step = 0;
    int flashes;
    boolean flashed;

    do {
      for (int i = 0; i < width * height; i++) {
        grid.array()[i] = grid.array()[i] <= 0 ? '1' : grid.array()[i] + 1;
      }

      step++;
      flashes = 0;

      do {
        flashed = false;
        for (int row = 0; row < height; row++) {
          for (int col = 0; col < width; col++) {
            if (grid.get(col, row) > '9') {
              grid.set(col, row, Integer.MIN_VALUE);
              flashed = true;
              flashes++;

              for (int rowDelta = Math.max(0, row - 1); rowDelta < Math.min(row + 2, height); rowDelta++) {
                for (int colDelta = Math.max(0, col - 1); colDelta < Math.min(col + 2, width); colDelta++) {
                  grid.set(colDelta, rowDelta, grid.get(colDelta, rowDelta) + 1);
                }
              }
            }
          }
        }
      } while (flashed);
    } while (flashes != width * height);

    return step;
  }
}
