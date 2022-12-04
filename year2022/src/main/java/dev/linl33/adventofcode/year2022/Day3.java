package dev.linl33.adventofcode.year2022;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day3 extends AdventSolution2022<Integer, Integer> {
  public static final int ALPHA_WIDTH = 'a' - 'A';
  public static final int COMPARTMENTS = 2;
  public static final int GROUP_SIZE = 3;

  public static void main(String[] args) {
    new Day3().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var rucksackCompartments = reader
        .lines()
        .<String>mapMulti((line, buffer) -> {
          var compartmentSize = line.length() / COMPARTMENTS;
          for (int i = 0; i < COMPARTMENTS; i++) {
            var compartmentStart = i * compartmentSize;
            buffer.accept(line.substring(compartmentStart, compartmentStart + compartmentSize));
          }
        })
        .mapToLong(Day3::stringToMask)
        .toArray();

    return reduceAndSumMasks(rucksackCompartments, COMPARTMENTS);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var rucksacks = reader
        .lines()
        .mapToLong(Day3::stringToMask)
        .toArray();

    return reduceAndSumMasks(rucksacks, GROUP_SIZE);
  }

  private static long stringToMask(String line) {
    return line
        .chars()
        .mapToLong(c -> 1L << (c - 'a'))
        .reduce((acc, next) -> acc | next)
        .orElseThrow();
  }

  private static int reduceAndSumMasks(long[] masks, int chunkSize) {
    var sum = 0;
    for (int i = 0; i < masks.length; i += chunkSize) {
      var reducedMask = masks[i];
      for (int j = 1; j < chunkSize; j++) {
        reducedMask &= masks[i + j];
      }

      sum += calcPriority(reducedMask);
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
