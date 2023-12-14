package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;

public class Day14 extends AdventSolution2023<Integer, Integer> {
  public static void main(String[] args) {
    new Day14();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var grid = new RowArrayGrid(reader);
    var width = grid.width();
    var height = grid.height();

    for (int y = 1; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (grid.get(x, y) != 'O') {
          continue;
        }

        var prevY = y;
        while (--prevY >= 0 && grid.get(x, prevY) == '.');

        grid.set(x, y, '.');
        grid.set(x, prevY + 1, 'O');
      }
    }

    var sum = 0;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (grid.get(x, y) != 'O') {
          continue;
        }


        var weight = height - y;
        sum += weight;
      }
    }
    return sum;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var grid = new RowArrayGrid(reader);
    var width = grid.width();
    var height = grid.height();

    var window = new int[32][];
    var beamLoadNorth = new int[window.length];

    for (int cycle = 0; cycle < 1_000_000_000; cycle++) {
      // north
      for (int y = 1; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (grid.get(x, y) != 'O') {
            continue;
          }

          var prevY = y;
          while (--prevY >= 0 && grid.get(x, prevY) == '.');

          grid.set(x, y, '.');
          grid.set(x, prevY + 1, 'O');
        }
      }

      // west
      for (int x = 1; x < width; x++) {
        for (int y = 0; y < height; y++) {
          if (grid.get(x, y) != 'O') {
            continue;
          }

          var prevX = x;
          while (--prevX >= 0 && grid.get(prevX, y) == '.');

          grid.set(x, y, '.');
          grid.set(prevX + 1, y, 'O');
        }
      }

      // south
      for (int y = height - 2; y >= 0; y--) {
        for (int x = 0; x < width; x++) {
          if (grid.get(x, y) != 'O') {
            continue;
          }

          var prevY = y;
          while (++prevY < height && grid.get(x, prevY) == '.');

          grid.set(x, y, '.');
          grid.set(x, prevY - 1, 'O');
        }
      }

      // east
      for (int x = width - 2; x >= 0; x--) {
        for (int y = 0; y < height; y++) {
          if (grid.get(x, y) != 'O') {
            continue;
          }

          var prevX = x;
          while (++prevX < width && grid.get(prevX, y) == '.');

          grid.set(x, y, '.');
          grid.set(prevX - 1, y, 'O');
        }
      }

      var currBeamLoadNorth = 0;
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (grid.get(x, y) != 'O') {
            continue;
          }

          var weight = height - y;
          currBeamLoadNorth += weight;
        }
      }

      for (int i = window.length - 1; i >= 0; i--) {
        if (currBeamLoadNorth == beamLoadNorth[i] && Arrays.equals(window[i], grid.array())) {
          var cycleLength = window.length - i - 1;
          var start = (cycle + 1) - (cycleLength + 1);
          var cycleOffset = (1_000_000_000 - start) % (cycleLength + 1);

          return beamLoadNorth[i + cycleOffset];
        }
      }

      // TODO: consider using a ring buffer
      System.arraycopy(window, 1, window, 0, window.length - 1);
      window[window.length - 1] = Arrays.copyOf(grid.array(), grid.array().length);

      System.arraycopy(beamLoadNorth, 1, beamLoadNorth, 0, beamLoadNorth.length - 1);
      beamLoadNorth[beamLoadNorth.length - 1] = currBeamLoadNorth;
    }

    // should not happen
    return -1;
  }
}
