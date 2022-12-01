package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Comparator;

public class Day1 extends AdventSolution2022<Integer, Integer> {
  public static void main(String[] args) {
    new Day1().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var input = AdventUtil.readInputGrouped(reader);

    return input
        .mapToInt(group -> group.mapToInt(Integer::parseInt).sum())
        .max()
        .orElseThrow();
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var input = AdventUtil.readInputGrouped(reader);

    return input
        .map(group -> group.mapToInt(Integer::parseInt).sum())
        .sorted(Comparator.reverseOrder())
        .limit(3)
        .mapToInt(i -> i)
        .sum();
  }
}
