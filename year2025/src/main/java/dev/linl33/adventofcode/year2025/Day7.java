package dev.linl33.adventofcode.year2025;

import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.HashSet;

public class Day7 extends AdventSolution2025<Long, Long> {
  static void main() {
    new Day7().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var firstLine = lines[0];
    var start = firstLine.indexOf('S');

    var splitters = new HashSet<Point2D>();

    for (var y = 0; y < lines.length; y++) {
      var line = lines[y];
      for (var x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '^') {
          splitters.add(new Point2D(x, y));
        }
      }
    }

    var splitCount = 0;
    var beamPositions = new boolean[lines[0].length()];
    beamPositions[start] = true;

    for (var y = 1; y < lines.length; y++) {
      var beamPositionsNext = new boolean[lines[0].length()];

      for (var x = 0; x < beamPositions.length; x++) {
        if (beamPositions[x]) {
          var pt = new Point2D(x, y);
          if (splitters.contains(pt)) {
            splitCount++;
            beamPositionsNext[x - 1] = true;
            beamPositionsNext[x + 1] = true;
          } else {
            beamPositionsNext[x] = true;
          }
        }
      }

      beamPositions = beamPositionsNext;
    }

    return (long) splitCount;
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var firstLine = lines[0];
    var start = firstLine.indexOf('S');

    var splitters = new HashSet<Point2D>();

    for (var y = 0; y < lines.length; y++) {
      var line = lines[y];
      for (var x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '^') {
          splitters.add(new Point2D(x, y));
        }
      }
    }

    var beamPositions = new boolean[lines[0].length()];
    beamPositions[start] = true;

    var choices = new long[lines[0].length()];
    choices[start] = 1;

    for (var y = 2; y < lines.length; y += 2) {
      var beamPositionsNext = new boolean[lines[0].length()];

      for (var x = 0; x < beamPositions.length; x++) {
        if (beamPositions[x]) {
          var pt = new Point2D(x, y);
          if (splitters.contains(pt)) {
            beamPositionsNext[x - 1] = true;
            beamPositionsNext[x + 1] = true;

            choices[x - 1] += choices[x];
            choices[x + 1] += choices[x];
            choices[x] = 0;
          } else {
            beamPositionsNext[x] = beamPositions[x];
          }
        }
      }

      beamPositions = beamPositionsNext;
    }

    var sum = 0L;
    for (var i = 0; i < choices.length; i++) {
      sum += choices[i];
    }

    return sum;
  }
}
