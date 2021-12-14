package dev.linl33.adventofcode.year2021;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14 extends AdventSolution2021<Long, Long> {
  private static final int HASH_MAP_DEFAULT_CAP = 100;

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

    var insertionRules = new HashMap<String, String[]>(HASH_MAP_DEFAULT_CAP, 1);
    reader
        .lines()
        .forEach(rule -> insertionRules.put(
            rule.substring(0, 2),
            new String[]{
                rule.charAt(0) + rule.substring(6, 7),
                rule.substring(6, 7) + rule.charAt(1),
            }
        ));

    var pairs = IntStream
        .range(0, polymerTemplate.length() - 1)
        .mapToObj(i -> polymerTemplate.substring(i, i + 2))
        .collect(Collectors.groupingBy(Function.identity(), () -> new HashMap<>(HASH_MAP_DEFAULT_CAP), Collectors.counting()));
    var pairsNext = new HashMap<String, Long>(HASH_MAP_DEFAULT_CAP);

    var freq = new long[26];
    for (char c : polymerTemplate.toCharArray()) {
      freq[c - 'A']++;
    }

    for (int step = 0; step < steps; step++) {
      pairsNext.clear();
      var finalPairsNext = pairsNext;
      pairs.forEach((pair, count) -> {
        // note that every pair should have a rule
        // so insertionRules.containsKey(pair) check is not needed
        var toInsert = insertionRules.get(pair);

        var left = toInsert[0];
        var right = toInsert[1];

        finalPairsNext.compute(left, (__, v) -> v == null ? count : v + count);
        finalPairsNext.compute(right, (__, v) -> v == null ? count : v + count);

        freq[right.charAt(0) - 'A'] += count;
      });

      var tmp = pairs;
      pairs = pairsNext;
      pairsNext = tmp;
    }

    var max = 0L;
    var min = Long.MAX_VALUE;
    for (var v : freq) {
      if (v == 0) {
        continue;
      }

      max = Math.max(max, v);
      min = Math.min(min, v);
    }

    return max - min;
  }
}
