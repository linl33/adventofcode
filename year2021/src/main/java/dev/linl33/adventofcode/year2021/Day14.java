package dev.linl33.adventofcode.year2021;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14 extends AdventSolution2021<Long, Long> {
  public static void main(String[] args) {
    new Day14().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    return solve(reader, 10);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    return solve(reader, 40);
  }

  private static long solve(@NotNull BufferedReader reader, int steps) throws IOException {
    var polymerTemplate = reader.readLine();
    // skip empty line
    reader.skip(1);

    var insertionRules = new HashMap<String, String>();
    reader
        .lines()
        .forEach(rule -> insertionRules.put(rule.substring(0, 2), rule.substring(6, 7)));

    var pairs = IntStream
        .range(0, polymerTemplate.length() - 1)
        .mapToObj(i -> polymerTemplate.substring(i, i + 2))
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (int step = 0; step < steps; step++) {
      var pairsNext = new HashMap<String, Long>();

      for (var kv : pairs.entrySet()) {
        if (!insertionRules.containsKey(kv.getKey())) {
          pairsNext.put(kv.getKey(), kv.getValue());
          continue;
        }

        var toInsert = insertionRules.get(kv.getKey());
        var left = kv.getKey().charAt(0) + toInsert;
        var right = toInsert + kv.getKey().charAt(1);

        pairsNext.compute(left, (k, v) -> v == null ? kv.getValue() : v + kv.getValue());
        pairsNext.compute(right, (k, v) -> v == null ? kv.getValue() : v + kv.getValue());
      }

      pairs = pairsNext;
    }

    var freq = new long[26];
    for (var entry : pairs.entrySet()) {
      entry
          .getKey()
          .chars()
          .forEach(c -> freq[c - 'A'] += entry.getValue());
    }

    freq[polymerTemplate.charAt(0) - 'A']++;
    freq[polymerTemplate.charAt(polymerTemplate.length() - 1) - 'A']++;

    var max = 0L;
    var min = Long.MAX_VALUE;
    for (long v : freq) {
      if (v == 0) {
        continue;
      }

      max = Math.max(max, v);
      min = Math.min(min, v);
    }

    return (max - min) >> 1;
  }
}
