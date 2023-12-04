package dev.linl33.adventofcode.year2023;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.function.IntBinaryOperator;

public class Day4 extends AdventSolution2023<Integer, Integer> {
  public static void main(String[] args) {
    new Day4().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    return scoreScratchCards(reader, (_, matches) -> (1 << matches) / 2);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var multiplier = new int[256];
    Arrays.fill(multiplier, 1);

    return scoreScratchCards(reader, (i, matches) -> {
      var currMultiplier = multiplier[i];
      for (int j = i + 1; j <= i + matches; j++) {
        multiplier[j] += currMultiplier;
      }

      return currMultiplier;
    });
  }

  private static int scoreScratchCards(@NotNull BufferedReader reader, IntBinaryOperator scoreOp) {
    var lines = reader.lines().toArray(String[]::new);

    var startIdx = lines[0].indexOf(':', 6) + 2;
    var pipeIdx = lines[0].indexOf('|');
    var lineLength = lines[0].length();

    var masks = new long[2];
    var sum = 0;

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];

      masks[0] = 0L;
      masks[1] = 0L;

      var curr = startIdx;
      for (; curr < pipeIdx - 1; curr += 3) {
        var number = 10 * (line.codePointAt(curr) & 0xF) + (line.codePointAt(curr + 1) & 0xF);
        // mask index with 1 for better JIT code gen
        masks[(number >> 6) & 1] |= 1L << (number & 63);
      }

      curr += 2;
      var count = 0;
      for (; curr < lineLength; curr += 3) {
        var number = 10 * (line.codePointAt(curr) & 0xF) + (line.codePointAt(curr + 1) & 0xF);
        // mask index with 1 for better JIT code gen
        count += (masks[(number >> 6) & 1] & (1L << (number & 63))) >>> (number & 63);
      }

      sum += scoreOp.applyAsInt(i, count);
    }

    return sum;
  }
}
