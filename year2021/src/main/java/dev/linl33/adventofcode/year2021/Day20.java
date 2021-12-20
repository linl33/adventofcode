package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;

public class Day20 extends AdventSolution2021<Integer, Integer> {
  public static void main(String[] args) {
    new Day20().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    return solve(reader, 2);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    return solve(reader, 50);
  }

  private static int solve(@NotNull BufferedReader reader, int enhancementRounds) throws IOException {
    var enhancementAlgorithm = reader.readLine();
    // skip empty line
    reader.read();

    var offset = enhancementRounds + 1;

    var smallGrid = new RowArrayGrid(reader);
    var largeGrid = new RowArrayGrid(smallGrid.height() + (2 * offset), smallGrid.width() + (2 * offset));

    for (int y = 0; y < smallGrid.height(); y++) {
      for (int x = 0; x < smallGrid.width(); x++) {
        largeGrid.set(x + offset, y + offset, smallGrid.get(x, y) == '#' ? 1 : 0);
      }
    }

    var gridNext = new RowArrayGrid(largeGrid.height(), largeGrid.width());
    System.arraycopy(largeGrid.array(), 0, gridNext.array(), 0, largeGrid.array().length);
    for (int i = 0; i < enhancementRounds; i++) {
      for (int y = 1; y < largeGrid.height() - 1; y++) {
        for (int x = 1; x < largeGrid.width() - 1; x++) {
          var idx = 0;

          for (int subY = y - 1; subY <= y + 1; subY++) {
            for (int subX = x - 1; subX <= x + 1; subX++) {
              idx = (idx << 1) | largeGrid.get(subX, subY);
            }
          }

          gridNext.set(x, y, enhancementAlgorithm.charAt(idx) == '#' ? 1 : 0);
        }
      }

      var reference = gridNext.get(1, 1);
      for (int y = 0; y < gridNext.height(); y++) {
        gridNext.set(0, y, reference);
        gridNext.set(gridNext.width() - 1, y, reference);
      }

      for (int x = 0; x < gridNext.width(); x++) {
        gridNext.set(x, 0, reference);
        gridNext.set(x, gridNext.height() - 1, reference);
      }

      var tmp = largeGrid;
      largeGrid = gridNext;
      gridNext = tmp;
    }

    var counter = 0;
    for (int i = 0; i < largeGrid.array().length; i++) {
      counter += largeGrid.array()[i];
    }

    return counter;
  }
}
