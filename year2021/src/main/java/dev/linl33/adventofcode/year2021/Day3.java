package dev.linl33.adventofcode.year2021;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;

public class Day3 extends AdventSolution2021<Integer, Integer> {
  public static void main(String[] args) {
    new Day3().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader
        .lines()
        .toArray(String[]::new);

    var width = lines[0].length();
    var counter = new int[width];

    for (var line : lines) {
      for (int i = 0; i < width; i++) {
        counter[i] += line.charAt(i) == '1' ? 1 : -1;
      }
    }

    var gamma = 0;
    for (var c : counter) {
      gamma = (gamma << 1) + (c >= 0 ? 1 : 0);
    }

    // epsilon is the bitwise inverse of gamma
    var epsilon = gamma ^ ((1 << width) - 1);

    return gamma * epsilon;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws IOException {
    var firstLine = reader.readLine();
    var width = firstLine.length();
    var firstNum = binaryToInt(firstLine, width);

    var nums = reader
        .lines()
        .mapToInt(l -> binaryToInt(l, width))
        .toArray();

    var lineCount = nums.length + 1;

    var remaining = new int[lineCount * 2];
    remaining[0] = firstNum;
    System.arraycopy(nums, 0, remaining, 1, nums.length);
    System.arraycopy(remaining, 0, remaining, lineCount, lineCount);

    var pos = width - 1;

    var o2Found = lineCount;
    var co2Found = lineCount;

    while (o2Found > 1 || co2Found > 1) {
      var o2Count = countCommonBit(remaining, o2Found, pos, 0);
      var co2Count = countCommonBit(remaining, co2Found, pos, lineCount);

      o2Found = filterRemaining(remaining, o2Found, pos, o2Count >= 0, 0);
      co2Found = filterRemaining(remaining, co2Found, pos, co2Count < 0, lineCount);
      pos--;
    }

    return remaining[0] * remaining[lineCount];
  }

  private static int filterRemaining(int[] remaining, int remainingCount, int pos, boolean commonBit, int offset) {
    var remainingCountNext = offset;

    for (int i = offset; i < remainingCount + offset; i++) {
      var num = remaining[i];

      var isSet = (num & (1 << pos)) != 0;
      if (isSet == commonBit) {
        remaining[remainingCountNext++] = num;
      }
    }

    return remainingCountNext - offset;
  }

  private static int countCommonBit(int[] remaining, int remainingCount, int pos, int offset) {
    var o2Count = 0;
    for (int i = 0; i < remainingCount; i++) {
      o2Count += (remaining[i + offset] & (1 << pos)) != 0 ? 1 : -1;
    }

    return o2Count;
  }

  private static int binaryToInt(@NotNull String binary, int size) {
    var result = 0;
    for (int i = 0; i < size; i++) {
      result = (result << 1) + (binary.charAt(i) - '0');
    }

    return result;
  }
}
