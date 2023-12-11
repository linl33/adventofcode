package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Day11 extends AdventSolution2023<Long, Long> {
  public static void main(String[] args) {
    new Day11().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    return solve(reader, 2);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    return solve(reader, 1_000_000);
  }

  private static long solve(BufferedReader reader, final int emptySpaceScale) {
    var lines = reader.lines().toArray(String[]::new);
    var dim = lines.length;

    var galaxies = new ArrayList<Point2D>();
    var vAdjust = 0;
    var emptyCols = new boolean[dim];
    Arrays.fill(emptyCols, true);

    for (int y = 0; y < dim; y++) {
      var line = lines[y];

      var rowEmpty = true;
      for (int x = 0; x < dim; x++) {
        var pt = line.codePointAt(x);

        if (pt == '#') {
          galaxies.add(new Point2D(x, y + vAdjust));
          rowEmpty = false;
          emptyCols[x] = false;
        }
      }

      if (rowEmpty) {
        vAdjust += emptySpaceScale - 1;
      }
    }

    var hAdjust = new int[dim];
    var hAdjustCurr = 0;
    for (int x = 0; x < dim; x++) {
      hAdjust[x] = hAdjustCurr;
      if (emptyCols[x]) {
        hAdjustCurr += emptySpaceScale - 1;
      }
    }

    var sum = 0L;
    for (int idxA = 0; idxA < galaxies.size(); idxA++) {
      var left = galaxies.get(idxA);

      for (int idxB = idxA + 1; idxB < galaxies.size(); idxB++) {
        var right = galaxies.get(idxB);

        var hDist = Math.abs(right.x() - left.x()) + Math.abs(hAdjust[right.x()] - hAdjust[left.x()]);
        var vDist = right.y() - left.y();

        sum += hDist + vDist;
      }
    }

    return sum;
  }
}
