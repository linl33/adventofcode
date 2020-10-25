package dev.linl33.adventofcode.year2018;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.stream.IntStream;

public class Day11 extends AdventSolution2018<String, String> {
  public static void main(String[] args) {
    new Day11().runAndPrintAll();
  }

  @Override
  public String part1(BufferedReader reader) throws IOException {
    var highestPower = findHighestPower(Integer.parseInt(reader.readLine()), 3, null);
    return highestPower.x() + "," + highestPower.y();
  }

  @Override
  public String part2(BufferedReader reader) throws IOException {
    var largestSize = findLargestSize(Integer.parseInt(reader.readLine()));
    return largestSize.x() + "," + largestSize.y() + "," + largestSize.size();
  }

  public static GridSquare findHighestPower(int serial, int size, int[] cache) {
    var powerGrid = buildPowerGrid(serial);

    var cacheCounter = 0;
    var maxPower = 0;
    var maxX = 0;
    var maxY = 0;

    for (var x = 1; x <= 300 - size + 1; x++) {
      for (var y = 1; y <= 300 - size + 1; y++) {
        var cacheIdx = (x - 1) * (300 - size + 2) + (y - 1);
        var power = cache == null ? 0 : cache[cacheIdx];

        if (cache == null || size == 1) {
          for (int xSub = 0; xSub < size; xSub++) {
            for (int ySub = 0; ySub < size; ySub++) {
              power += powerGrid[x + xSub][y + ySub];
            }
          }
        } else {
          for (int ySub = 0; ySub < size; ySub++) {
            power += powerGrid[x + size - 1][y + ySub];
          }

          for (int xSub = 0; xSub < size - 1; xSub++) {
            power += powerGrid[x + xSub][y + size - 1];
          }
        }

        if (cache != null) {
          cache[cacheCounter++] = power;
        }

        if (power > maxPower) {
          maxPower = power;
          maxX = x;
          maxY = y;
        }
      }
    }

    return new GridSquare(maxX, maxY, size, maxPower);
  }

  public static GridSquare findLargestSize(int serial) {
    var powerCache = new int[301 * 301];

    return IntStream
        .rangeClosed(1, 300)
        .mapToObj(size -> findHighestPower(serial, size, powerCache))
        .max(Comparator.comparing(GridSquare::power))
        .orElseThrow();
  }

  private static int[][] buildPowerGrid(int serial) {
    var powerGrid = new int[301][301];
    for (var x = 1; x <= 300; x++) {
      for (var y = 1; y <= 300; y++) {
        powerGrid[x][y] = getPower(x, y, serial);
      }
    }

    return powerGrid;
  }

  private static int getPower(int x, int y, int serial) {
    var rackId = x + 10;
    var power = rackId * y + serial;
    power *= rackId;
    return getHundreds(power) - 5;
  }

  private static int getHundreds(int input) {
    return input / 100 % 10;
  }

  public static record GridSquare(int x, int y, int size, int power) {}
}
