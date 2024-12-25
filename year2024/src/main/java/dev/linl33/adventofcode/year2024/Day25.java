package dev.linl33.adventofcode.year2024;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day25 extends AdventSolution2024<Integer, Void> {
  private static final int WIDTH = 5;
  private static final int HEIGHT = 7;

  public static void main() {
    new Day25().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    // TODO: SIMD should help a lot here

    var lines = reader.lines().toArray(String[]::new);

    var locksAndKeys = new int[(lines.length + 1) / (HEIGHT + 1)];
    var lockIdx = 0;
    var keyIdx = locksAndKeys.length - 1;

    for (var i = 0; i < lines.length; i += HEIGHT + 1) {
      var shape = 0;
      for (var row = 1; row < HEIGHT - 1; row++) {
        for (var col = 0; col < WIDTH; col++) {
          // '#' & 1 == 1, '.' & 1 == 0
          var bit = lines[i + row].codePointAt(col) & 1;
          shape |= bit << (row * WIDTH + col);
        }
      }

      locksAndKeys[lines[i].codePointAt(0) == '#' ? lockIdx++ : keyIdx--] = shape;
    }

    var count = 0;
    for (int i = 0; i < lockIdx; i++) {
      var lock = locksAndKeys[i];

      for (int j = lockIdx; j < locksAndKeys.length; j++) {
        var key = locksAndKeys[j];

        if ((key & lock) == 0) {
          count++;
        }
      }
    }

    return count;
  }

  @Override
  public Void part2(@NotNull BufferedReader reader) {
    throw new UnsupportedOperationException();
  }
}
