package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.util.PrintUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day17 extends AdventSolution2021<Integer, Integer> {
  public static void main(String[] args) {
    new Day17().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var line = reader.readLine();
    var parts = line.substring(13).split(", ");
    var x = parts[0];
    var y = parts[1];

    // TODO: improve the solution

    var xRange = parseRange(x);
    var yRange = parseRange(y);

    PrintUtil.enhancedPrint(xRange);
    PrintUtil.enhancedPrint(yRange);

    var maxMaxY = Integer.MIN_VALUE;
    var steps = 1_000;

    for (int initVecX = 1; initVecX <= 1_000; initVecX++) {
      for (int initVecY = 1; initVecY <= 1_000; initVecY++) {
        var currX = 0;
        var currY = 0;

        var vecX = initVecX;
        var vexY = initVecY;

        var maxY = Integer.MIN_VALUE;

        var step = 0;
        while (step < steps && !isWithinRange(currX, currY, xRange, yRange)) {
          // move by vec
          currX += vecX;
          currY += vexY;

          // account for drag
          vecX -= Integer.signum(vecX);
          vexY--;

          maxY = Math.max(maxY, currY);

          step++;
        }

        if (isWithinRange(currX, currY, xRange, yRange)) {
          maxMaxY = Math.max(maxMaxY, maxY);
        }
      }
    }

    return maxMaxY;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var line = reader.readLine();
    var parts = line.substring(13).split(", ");
    var x = parts[0];
    var y = parts[1];

    var xRange = parseRange(x);
    var yRange = parseRange(y);

    PrintUtil.enhancedPrint(xRange);
    PrintUtil.enhancedPrint(yRange);

    var steps = 1_000;

    var solutions = 0;

    for (int initVecX = 1; initVecX <= 1_000; initVecX++) {
      for (int initVecY = -1_000; initVecY <= 1_000; initVecY++) {
        var currX = 0;
        var currY = 0;

        var vecX = initVecX;
        var vexY = initVecY;

        var step = 0;
        while (step < steps && !isWithinRange(currX, currY, xRange, yRange)) {
          // move by vec
          currX += vecX;
          currY += vexY;

          // account for drag
          vecX -= Integer.signum(vecX);
          vexY--;

          step++;
        }

        if (isWithinRange(currX, currY, xRange, yRange)) {
          solutions++;
        }
      }
    }

    return solutions;
  }

  private static int[] parseRange(String range) {
    var points = range.substring(2).split("\\.\\.");
    return new int[] { Integer.parseInt(points[0]), Integer.parseInt(points[1]) };
  }

  private static boolean isWithinRange(int x, int y, int[] xRange, int[] yRange) {
    return x >= xRange[0] && x <= xRange[1] && y >= yRange[0] && y <= yRange[1];
  }
}
