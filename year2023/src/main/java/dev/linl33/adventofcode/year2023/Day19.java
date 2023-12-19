package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.Function;

public class Day19 extends AdventSolution2023<Long, Long> {
  public static void main(String[] args) {
    new Day19().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    var parts = AdventUtil.readInputGrouped(reader).map(s -> s.toArray(String[]::new)).toArray(String[][]::new);

    var workflow = parts[0];
    var ratings = parts[1];

    var ratingsMaps = new ArrayList<Map<String, Integer>>();

    for (var rating : ratings) {
      var map = new HashMap<String, Integer>();
      ratingsMaps.add(map);

      var counts = rating.substring(1, rating.length() - 1).split(",");
      for (String c : counts) {
        var split = c.split("=");
        map.put(split[0], Integer.parseInt(split[1]));
      }
    }

    var workflowParsed = new HashMap<String, Function<Map<String, Integer>, String>>();
    for (var w : workflow) {
      var delim = w.indexOf('{');
      var name = w.substring(0, delim);
      var dTree = w.substring(delim + 1, w.length() - 1).split(",");

      Function<Map<String, Integer>, String> f = part -> {
        for (String s : dTree) {
          var pair = s.split(":");
          if (pair.length == 1) {
            return pair[0];
          }

          var criterion = pair[0].splitWithDelimiters("[><]", 2);

          var criterionLeft = criterion[0];
          var cLeftVal = part.get(criterionLeft);
          if (cLeftVal == null) {
            throw new IllegalStateException();
          }

          var criterionRight = Integer.parseInt(criterion[2]);
          var op = criterion[1].charAt(0);
          if (op == '>') {
            if (cLeftVal > criterionRight) {
              return pair[1];
            }
          } else if (op == '<') {
            if (cLeftVal < criterionRight) {
              return pair[1];
            }
          } else {
            throw new IllegalStateException();
          }
        }

        throw new IllegalStateException();
      };

      workflowParsed.put(name, f);
    }

    var sum = 0L;

    for (var map : ratingsMaps) {
      var verdict = "in";
      while (!verdict.equals("A") && !verdict.equals("R")) {
        verdict = workflowParsed.get(verdict).apply(map);
      }

      if (verdict.equals("A")) {
        var partSum = 0L;
        partSum = map.values().stream().mapToInt(i -> i).sum();

        sum += partSum;
      }
    }
    return sum;
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    var parts = AdventUtil.readInputGrouped(reader).map(s -> s.toArray(String[]::new)).toArray(String[][]::new);

    var workflow = parts[0];
    var workflowParsed = new HashMap<String, String[]>();

    for (var w : workflow) {
      var delim = w.indexOf('{');
      var name = w.substring(0, delim);
      var dTree = w.substring(delim + 1, w.length() - 1).split(",");
      workflowParsed.put(name, dTree);
    }

    var queue = new ArrayDeque<String>();
    var combinationQueue = new ArrayDeque<int[]>();

    queue.add("in");
    combinationQueue.add(new int[] { 1, 4000, 1, 4000, 1, 4000, 1, 4000 });

    var valid = new ArrayList<int[]>();

    while (!queue.isEmpty()) {
      var next = queue.remove();
      var combinationNext = combinationQueue.remove();

      if (next.equals("A")) {
        valid.add(Arrays.copyOf(combinationNext, 8));
        continue;
      } else if (next.equals("R")) {
        continue;
      }

      var w = workflowParsed.get(next);
      var inverses = new int[(w.length - 1) * 2];
      var invCat = new int[w.length - 1];

      wTreeLoop:
      for (int i = 0; i < w.length; i++) {
        var child = w[i];
        var newRangeNew = Arrays.copyOf(combinationNext, 8);

        var split = child.split(":");
        if (split.length != 1) {
          var criterion = split[0].splitWithDelimiters("[><]", 3);

          var category = criterion[0].charAt(0);
          var op = criterion[1].charAt(0);
          var val = Integer.parseInt(criterion[2]);

          var categoryId = switch (category) {
            case 'x' -> 0;
            case 'm' -> 2;
            case 'a' -> 4;
            case 's' -> 6;
            default -> throw new IllegalStateException();
          };
          invCat[i] = categoryId;

          var cRange = new int[2];
          if (op == '>') {
            cRange = new int[]{val + 1, 4000};

            inverses[i * 2] = 1;
            inverses[i * 2 + 1] = val;
          } else if (op == '<') {
            cRange = new int[]{1, val - 1};

            inverses[i * 2] = val;
            inverses[i * 2 + 1] = 4000;
          } else {
            throw new IllegalStateException();
          }

          var cMin = cRange[0];
          var cMax = cRange[1];

          if (cMin < 1 || cMax > 4000 || cMin > cMax) {
            throw new IllegalStateException();
          }

          var rangeMin = combinationNext[categoryId];
          var rangeMax = combinationNext[categoryId + 1];
          var newRange = new int[]{Math.max(cMin, rangeMin), Math.min(cMax, rangeMax)};

          if (newRange[0] > newRange[1]) {
            System.out.println("hit");
            continue;
          }

          newRangeNew[categoryId] = newRange[0];
          newRangeNew[categoryId + 1] = newRange[1];
        }

        for (int j = 0; j < i; j++) {
          newRangeNew[invCat[j]] = Math.max(newRangeNew[invCat[j]], inverses[j * 2]);
          newRangeNew[invCat[j] + 1] = Math.min(newRangeNew[invCat[j] + 1], inverses[j * 2 + 1]);

          if (newRangeNew[invCat[j]] > newRangeNew[invCat[j] + 1]) {
            System.out.println("hit");
            continue wTreeLoop;
          }
        }

        queue.add(split.length == 1 ? split[0] : split[1]);
        combinationQueue.add(newRangeNew);
      }
    }

    var total = 0L;
    for (var combination : valid) {
      var subTotal = 1L;
      for (int i = 0; i < 4; i++) {
        subTotal *= combination[i * 2 + 1] - combination[i * 2] + 1;
      }
      total += subTotal;
    }

    return total;
  }
}
