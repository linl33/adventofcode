package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;

public class Day14 extends AdventSolution2023<Integer, Integer> {
  public static void main(String[] args) {
    new Day14().runAndPrintAll();
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

    var hSegments = new int[height * width * 4];
    var hSegmentsSize = 0;

    for (int y = 0; y < height; y++) {
      var state = false;

      for (int x = 0; x < width; x++) {
        var curr = grid.get(x, y) != '#';

        if (state != curr) {
          hSegments[hSegmentsSize++] = x;
          hSegments[hSegmentsSize++] = y;
          state = !state;
        }
      }

      if (state) {
        hSegments[hSegmentsSize++] = width;
        hSegments[hSegmentsSize++] = y;
      }
    }

    var rowToColumn = new int[hSegmentsSize / 4][];
    for (int i = 0; i < hSegmentsSize; i += 4) {
      rowToColumn[i / 4] = new int[hSegments[i + 2] - hSegments[i]];
    }

    var columnToRow = new int[height * width][];

    var rowsSize = hSegmentsSize / 4;
    var rows = new byte[rowsSize];
    var columns = new byte[height * width];
    var columnsSize = 0;

    for (int x = 0; x < width; x++) {
      var state = false;
      var startInclusive = 0;

      for (int y = 0; y < height; y++) {
        var curr = grid.get(x, y);

        if (curr == 'O') {
          columns[columnsSize]++;
        }

        if (!state && curr != '#') {
          startInclusive = y;
          state = true;
        } else if (state && curr == '#') {
          var columnId = columnsSize++;
          columnToRow[columnId] = new int[y - startInclusive];

          var colYCurr = startInclusive;

          for (int i = 0; i < hSegmentsSize; i += 4) {
            var hSegmentY = hSegments[i + 1];
            if (hSegmentY != colYCurr) {
              continue;
            }

            var hSegmentXLo = hSegments[i];
            var hSegmentXHi = hSegments[i + 2];
            if (x >= hSegmentXLo && x < hSegmentXHi) {
              columnToRow[columnId][colYCurr - startInclusive] = i / 4;
              rowToColumn[i / 4][x - hSegmentXLo] = columnId;

              colYCurr++;
              if (colYCurr == y) {
                break;
              }
            }
          }

          state = false;
        }
      }

      if (state) {
        var columnId = columnsSize++;
        columnToRow[columnId] = new int[height - startInclusive];

        var colYCurr = startInclusive;

        for (int i = 0; i < hSegmentsSize; i += 4) {
          var hSegmentY = hSegments[i + 1];
          if (hSegmentY != colYCurr) {
            continue;
          }

          var hSegmentXLo = hSegments[i];
          var hSegmentXHi = hSegments[i + 2];
          if (x >= hSegmentXLo && x < hSegmentXHi) {
            columnToRow[columnId][colYCurr - startInclusive] = i / 4;
            rowToColumn[i / 4][x - hSegmentXLo] = columnId;

            colYCurr++;
            if (colYCurr == height) {
              break;
            }
          }
        }
      }
    }

    var window = new byte[128][];
    var beamLoadNorth = new int[window.length];

    var columnsNext = new byte[columnsSize];

    for (int cycle = 0; cycle < 1_000_000_000; cycle++) {
      // tilt north then west
      Arrays.fill(rows, 0, rowsSize, (byte) 0);
      Arrays.fill(columnsNext, 0, columnsSize, (byte) 0);
      for (int i = 0; i < columnsSize; i++) {
        var col = columns[i];

        for (byte j = 0; j < col; j++) {
          var northRow = columnToRow[i][j];
          var westCol = rowToColumn[northRow][rows[northRow]++];
          columnsNext[westCol]++;
        }
      }

      // tilt south then east
      Arrays.fill(rows, 0, rowsSize, (byte) 0);
      Arrays.fill(columns, 0, columnsSize, (byte) 0);
      for (int i = 0; i < columnsSize; i++) {
        var col = columnsNext[i];

        for (byte j = 0; j < col; j++) {
          var southRow = columnToRow[i][columnToRow[i].length - j - 1];
          var eastCol = rowToColumn[southRow][rowToColumn[southRow].length - (rows[southRow]++) - 1];
          columns[eastCol]++;
        }
      }

      var currBeamLoadNorth = 0;
      for (int i = 1; i < hSegmentsSize; i += 4) {
        var h = height - hSegments[i];
        currBeamLoadNorth += h * rows[i / 4];
      }

      for (int i = window.length - 1; i >= 0; i--) {
        if (currBeamLoadNorth == beamLoadNorth[i] && Arrays.equals(window[i], rows)) {
          var cycleLength = window.length - i - 1;
          var start = (cycle + 1) - (cycleLength + 1);
          var cycleOffset = (1_000_000_000 - start) % (cycleLength + 1);

          return beamLoadNorth[i + cycleOffset];
        }
      }

      // TODO: consider using a ring buffer
      System.arraycopy(window, 1, window, 0, window.length - 1);
      window[window.length - 1] = Arrays.copyOf(rows, rows.length);

      System.arraycopy(beamLoadNorth, 1, beamLoadNorth, 0, beamLoadNorth.length - 1);
      beamLoadNorth[beamLoadNorth.length - 1] = currBeamLoadNorth;
    }

    // should not happen
    return -1;
  }
}
