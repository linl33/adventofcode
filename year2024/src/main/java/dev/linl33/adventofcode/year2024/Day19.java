package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class Day19 extends AdventSolution2024<Integer, Long> {
  public static void main() {
    new Day19().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var groups = AdventUtil.readInputGrouped(reader).toList();
    var inv = groups.get(0).findAny().orElseThrow().split(", ");

    return (int) groups
      .get(1)
      .filter(design -> isPossible(design, 0, inv))
      .count();
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    var groups = AdventUtil.readInputGrouped(reader).toList();

    var inv = groups.get(0).findAny().orElseThrow().split(", ");
    var memo = new HashMap<String, Long>();

    return groups
      .get(1)
      .mapToLong(design -> countPatterns(design, 0, inv, memo))
      .sum();
  }

  private static boolean isPossible(String design, int startIdx, String[] inv) {
    if (startIdx == design.length()) {
      return true;
    }

    for (var i = 0; i < inv.length; i++) {
      var towel = inv[i];
      if (design.startsWith(towel, startIdx)) {
        if (isPossible(design, startIdx + towel.length(), inv)) {
          return true;
        }
      }
    }

    return false;
  }

  private static long countPatterns(String design, int startIdx, String[] inv, Map<String, Long> memo) {
    if (startIdx == design.length()) {
      return 1;
    }

    var designRem = design.substring(startIdx);
    if (memo.containsKey(designRem)) {
      return memo.get(designRem);
    }

    var patterns = 0L;
    for (var i = 0; i < inv.length; i++) {
      var towel = inv[i];
      if (design.startsWith(towel, startIdx)) {
        patterns += countPatterns(design, startIdx + towel.length(), inv, memo);
      }
    }

    memo.put(designRem, patterns);
    return patterns;
  }
}
