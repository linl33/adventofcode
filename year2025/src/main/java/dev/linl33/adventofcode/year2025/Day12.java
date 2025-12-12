package dev.linl33.adventofcode.year2025;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day12 extends AdventSolution2025<Integer, Void> {
  static void main() {
    new Day12().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var groups = AdventUtil.readInputGrouped(reader).map(x -> x.toArray(String[]::new)).toArray(String[][]::new);

    var shapeArea = new int[groups.length - 1];
    for (var i = 0; i < shapeArea.length; i++) {
      var shapeStrArr = groups[i];

      for (int y = 0; y < 3; y++) {
        for (int x = 0; x < 3; x++) {
          if (shapeStrArr[y + 1].charAt(x) == '#') {
            shapeArea[i]++;
          }
        }
      }
    }

    var count = 0;
    var regions = groups[groups.length - 1];
    for (var i = 0; i < regions.length; i++) {
      var regionStr = regions[i];
      var xSep = regionStr.indexOf('x');
      var cSep = regionStr.indexOf(':', xSep + 2);

      var width = Integer.parseInt(regionStr, 0, xSep, 10);
      var height = Integer.parseInt(regionStr, xSep + 1, cSep, 10);

      var splits = regionStr.split(" ");
      var quantities = new int[splits.length - 1];
      for (var j = 1; j < splits.length; j++) {
        quantities[j - 1] = Integer.parseInt(splits[j]);
      }

      var presentAreaTotal = 0;
      for (var j = 0; j < quantities.length; j++) {
        presentAreaTotal += quantities[j] * shapeArea[j];
      }

      if (presentAreaTotal > (width * height)) {
        continue;
      }

      count++;
    }

    // for the puzzle input,
    // the presents fit into any grid whose size is >= the sum of the presents' area
    // (test input doesn't follow this pattern)
    return count;
  }

  @Override
  public Void part2(@NotNull BufferedReader reader) {
    throw new UnsupportedOperationException();
  }
}
