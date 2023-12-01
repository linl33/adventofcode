package dev.linl33.adventofcode.year2023;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day1 extends AdventSolution2023<Integer, Integer> {
  private static final String[] DIGITS = new String[] {
      null,
      "one",
      "two",
      "three",
      "four",
      "five",
      "six",
      "seven",
      "eight",
      "nine",
  };

  public static void main(String[] args) {
    new Day1().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    return reader
        .lines()
        .mapToInt(line -> parseCalibrationValue(line, false))
        .sum();
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    return reader
        .lines()
        .mapToInt(line -> parseCalibrationValue(line, true))
        .sum();
  }

  private static int parseCalibrationValue(String line, boolean parseLetters) {
    var calibrationValue = 0;
    var length = line.length();

    for (int i = 0; i < length; i++) {
      var digit = parseDigit(line, i, parseLetters);
      if (digit != 0) {
        calibrationValue += 10 * digit;
        break;
      }
    }

    for (int i = length - 1; i >= 0; i--) {
      var digit = parseDigit(line, i, parseLetters);
      if (digit != 0) {
        calibrationValue += digit;
        break;
      }
    }

    return calibrationValue;
  }

  private static int parseDigit(String line, int offset, boolean parseLetters) {
    if (line.charAt(offset) <= '9') {
      return line.charAt(offset) - '0';
    }

    if (parseLetters) {
      for (int i = 1; i <= 9; i++) {
        if (line.startsWith(DIGITS[i], offset)) {
          return i;
        }
      }
    }

    return 0;
  }
}
