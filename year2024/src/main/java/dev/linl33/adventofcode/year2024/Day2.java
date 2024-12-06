package dev.linl33.adventofcode.year2024;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;

public class Day2 extends AdventSolution2024<Integer, Integer> {
  public static void main() {
    new Day2().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    return (int) reader
      .lines()
      .map(line -> Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray())
      .filter(Day2::isSafe)
      .count();
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var count = 0;

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];
      var numbers = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();

      if (isSafe(numbers)) {
        count++;
      } else {
        var copy = new int[numbers.length - 1];

        for (var j = 0; j < numbers.length; j++) {
          for (var k = 0; k < numbers.length; k++) {
            if (k == j) {
              continue;
            }

            copy[k < j ? k : k - 1] = numbers[k];
          }

          if (isSafe(copy)) {
            count++;
            break;
          }
        }
      }
    }

    return count;
  }

  private static boolean isSafe(int[] numbers) {
    var sign = numbers[0] - numbers[1];

    var prev = numbers[0];
    for (int i = 1; i < numbers.length; i++) {
      var curr = numbers[i];
      var diff = prev - curr;

      // return false if the most significant bit of sign and diff are different
      if ((sign ^ diff) < 0) {
        return false;
      }

      if (diff == 0 || Math.abs(diff) > 3) {
        return false;
      }

      prev = curr;
    }

    return true;
  }
}
