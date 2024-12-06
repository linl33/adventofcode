package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day5 extends AdventSolution2024<Integer, Integer> {
  public static void main() {
    new Day5().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var groups = AdventUtil.readInputGrouped(reader).map(Stream::toList).toList();
    var rules = groups.get(0);
    var updates = groups.get(1);

    var rulesParsed = new ArrayList<int[]>();
    for (String r : rules) {
      rulesParsed.add(new int[] {
        Integer.parseInt(r, 0, 2, 10),
        Integer.parseInt(r, 3, 5, 10),
      });
    }

    var total = 0;
    for (var update : updates) {
      var length = update.length() / 3 + 1;
      var p = new int[length];
      for (var i = 0; i < length; i++) {
        p[i] = Integer.parseInt(update, i * 3, i * 3 + 2, 10);
      }
      var parts = Arrays.stream(p).boxed().toList();

      if (isCorrect(rulesParsed, parts)) {
        total += parts.get(parts.size() / 2);
      }
    }

    return total;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var groups = AdventUtil.readInputGrouped(reader).map(Stream::toList).toList();
    var rules = groups.get(0);
    var updates = groups.get(1);

    var rulesParsed = new ArrayList<int[]>();
    for (String r : rules) {
      rulesParsed.add(new int[] {
        Integer.parseInt(r, 0, 2, 10),
        Integer.parseInt(r, 3, 5, 10),
      });
    }

    var total = 0;
    for (var update : updates) {
      var parts = new ArrayList<>(Arrays.stream(update.split(",")).map(Integer::parseInt).toList());

      if (isCorrect(rulesParsed, parts)) {
        continue;
      }

      do {
        for (var rule : rulesParsed) {
          var left = rule[0];
          var right = rule[1];

          var leftIdx = parts.indexOf(left);
          var rightIdx = parts.indexOf(right);

          if (leftIdx == -1 || rightIdx == -1) {
            continue;
          }

          if (leftIdx >= rightIdx) {
            parts.add(leftIdx, right);
            parts.remove(leftIdx + 1);
            parts.add(rightIdx, left);
            parts.remove(rightIdx + 1);
          }
        }
      } while (!isCorrect(rulesParsed, parts));

      total += parts.get(parts.size() / 2);
    }

    return total;
  }

  private static boolean isCorrect(List<int[]> rules, List<Integer> parts) {
    for (var rule : rules) {
      var left = rule[0];
      var right = rule[1];

      var leftIdx = parts.indexOf(left);
      var rightIdx = parts.indexOf(right);

      if (leftIdx == -1 || rightIdx == -1) {
        continue;
      }

      if (leftIdx >= rightIdx) {
        return false;
      }
    }

    return true;
  }
}
