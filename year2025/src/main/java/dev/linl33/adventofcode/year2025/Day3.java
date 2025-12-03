package dev.linl33.adventofcode.year2025;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;

public class Day3 extends AdventSolution2025<Long, Long> {
  static void main() {
    new Day3().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    return calculateTotalJoltage(lines, 2);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    return calculateTotalJoltage(lines, 12);
  }

  private static long calculateTotalJoltage(final CharSequence[] batteryBanks, final int batteries) {
    final var batteryBankWidth = batteryBanks[0].length();
    final var alignedBatteryBankWidth = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(batteryBankWidth - 1));
    final var cache = new long[(batteries + 1) * alignedBatteryBankWidth];

    var sum = 0L;
    for (var i = 0; i < batteryBanks.length; i++) {
      var bank = batteryBanks[i];
      for (var j = 0; j < batteryBankWidth; j++) {
        cache[j] = bank.charAt(j) - '0';
      }

      Arrays.fill(cache, alignedBatteryBankWidth, cache.length, 0);
      sum += calculateJoltage(cache, batteryBankWidth - 1, batteries, alignedBatteryBankWidth);
    }

    return sum;
  }

  private static long calculateJoltage(final long[] cache, final int startIdx, final int depth, final int width) {
    final var key = depth * width + startIdx;
    if (cache[key] != 0) {
      return cache[key];
    }

    var max = -1L;

    for (var i = startIdx; i >= depth - 1; i--) {
      var joltage = cache[i];

      if (depth == 1) {
        max = Math.max(max, joltage);
      } else {
        var recursive = calculateJoltage(cache, i - 1, depth - 1, width);
        max = Math.max(max, recursive * 10 + joltage);
      }
    }

    cache[key] = max;
    return max;
  }
}
