package dev.linl33.adventofcode.year2020;

import java.io.BufferedReader;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class Day9 extends AdventSolution2020<Long, Long> {
  private static final int DEFAULT_PREAMBLE_LENGTH = 25;

  public static void main(String[] args) {
    new Day9().runAndPrintAll();
  }

  @Override
  public Long part1(BufferedReader reader) {
    return solvePart1(reader, DEFAULT_PREAMBLE_LENGTH);
  }

  public static long solvePart1(BufferedReader reader, int preambleLength) {
    var input = reader
        .lines()
        .mapToLong(Long::parseLong)
        .toArray();

    return solvePart1(input, preambleLength);
  }

  private static long solvePart1(long[] input, int preambleLength) {
    return IntStream
        .range(preambleLength, input.length)
        .dropWhile(i -> isValid(input, preambleLength, i))
        .limit(1)
        .mapToLong(i -> input[i])
        .findAny()
        .orElseThrow();
  }

  @Override
  public Long part2(BufferedReader reader) {
    return solvePart2(reader, DEFAULT_PREAMBLE_LENGTH);
  }

  public static long solvePart2(BufferedReader reader, int preambleLength) {
    var input = reader
        .lines()
        .mapToLong(Long::parseLong)
        .toArray();

    var sumTarget = solvePart1(input, preambleLength);
    var initSum = new AtomicLong(input[0]);

    return IntStream
        .rangeClosed(2, input.length)
        .mapToLong(size -> {
          var sum = initSum.getPlain() + input[size - 1];

          if (sum == sumTarget) {
            return sumMinMaxInRange(input, 0, size);
          }

          initSum.setPlain(sum);

          for (int i = size; i <= input.length - size; i++) {
            // drop the first term and add a new term
            var term = input[i];
            sum += term - input[i - size];

            // this is safe since the input is roughly ordered
            if (term > sumTarget) {
              return -1;
            }

            if (sum == sumTarget) {
              return sumMinMaxInRange(input, i - size + 1, size);
            }
          }

          return -1;
        })
        .dropWhile(l -> l < 0)
        .limit(1)
        .findAny()
        .orElseThrow();
  }

  private static boolean isValid(long[] list, int length, int checkIdx) {
    var checkVal = list[checkIdx];

    for (int i = checkIdx - length; i < checkIdx - 1; i++) {
      var firstVal = list[i];
      var diff = checkVal - firstVal;

      if (diff < 0 || diff == firstVal) {
        continue;
      }

      for (int j = i + 1; j < checkIdx; j++) {
        if (list[j] == diff) {
          return true;
        }
      }
    }

    return false;
  }

  private static long sumMinMaxInRange(long[] list, int start, int length) {
    var max = Long.MIN_VALUE;
    var min = Long.MAX_VALUE;

    for (int i = start; i < start + length; i++) {
      var term = list[i];
      max = Math.max(max, term);
      min = Math.min(min, term);
    }

    return max + min;
  }
}
