package dev.linl33.adventofcode.year2023;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.function.ToIntBiFunction;

public class Day2 extends AdventSolution2023<Integer, Integer> {
  private static final int RED_ID = colorToId('r');
  private static final int GREEN_ID = colorToId('g');
  private static final int BLUE_ID = colorToId('b');

  private static final int[] COLOR_LENGTH = new int[] {
      0, 0, 0,
      7, 6, 5,
  };

  public static void main(String[] args) {
    new Day2().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    return solve(reader, (i, map) -> (map[GREEN_ID] <= 13 && map[BLUE_ID] <= 14 && map[RED_ID] <= 12) ? (i + 1) : 0);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    return solve(reader, (_, map) -> map[3] * map[4] * map[5]);
  }

  private static int solve(@NotNull BufferedReader reader, ToIntBiFunction<Integer, int[]> mapper) {
    var lines = reader.lines().toArray(String[]::new);
    var sum = 0;

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      var map = requiredCubes(line);

      sum += mapper.applyAsInt(i, map);
    }

    return sum;
  }

  private static int[] requiredCubes(String game) {
    var map = new int[6];

    var curr = game.indexOf(':', 6) + 2;
    var length = game.length();
    do {
      var cubeCount = game.charAt(curr++) - '0';
      if (game.charAt(curr++) != ' ') {
        cubeCount = 10 * cubeCount + (game.charAt(curr++ - 1) - '0');
      }

      var cubeColor = colorToId(game.charAt(curr));
      map[cubeColor] = Math.max(map[cubeColor], cubeCount);

      curr += COLOR_LENGTH[cubeColor];
    } while (curr < length);

    return map;
  }

  private static int colorToId(char c) {
    return Integer.bitCount(c + 1);
  }
}
