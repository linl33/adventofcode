package dev.linl33.adventofcode.year2022;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day3 extends AdventSolution2022<Integer, Integer> {
  public static final int ALPHA_WIDTH = 'a' - 'A';

  public static void main(String[] args) {
    new Day3().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var masks = new long[2];

    return reader
        .lines()
        .mapToInt(line -> {
          masks[0] = 0L;
          masks[1] = 0L;

          var rucksackSize = line.length();
          var compartmentSize = rucksackSize / 2;
          for (int mask = 0; mask < 2; mask++) {
            var from = compartmentSize * mask;
            for (int i = from; i < from + compartmentSize; i++) {
              var item = line.charAt(i) - 'a';
              masks[mask] |= 1L << item;
            }
          }

          return calcPriority(masks[0] & masks[1]);
        })
        .sum();
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var inputInts = reader
        .lines()
        .mapToLong(line -> line
            .chars()
            .mapToLong(c -> 1L << (c - 'a'))
            .reduce((acc, next) -> acc | next)
            .orElseThrow()
        )
        .toArray();

    var sum = 0;
    for (int i = 0; i < inputInts.length; i += 3) {
      sum += calcPriority(inputInts[i] & inputInts[i + 1] & inputInts[i + 2]);
    }

    return sum;
  }

  private static int calcPriority(long mask) {
    var commonItem = Long.numberOfTrailingZeros(mask);
    // 1 if upper, 0 otherwise
    var upperOrLower = commonItem / ALPHA_WIDTH;

    return (commonItem % ALPHA_WIDTH) + 1 + upperOrLower * 26;
  }
}
