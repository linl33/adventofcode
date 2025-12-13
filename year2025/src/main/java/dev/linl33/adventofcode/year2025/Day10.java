package dev.linl33.adventofcode.year2025;

import dev.linl33.adventofcode.jmh.JmhBenchmarkOption;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.IntStream;

public class Day10 extends AdventSolution2025<Integer, Integer> {
  private static final List<int[]> PATTERNS = List.ofLazy(16, (final int i) -> (
    IntStream
      .range(0, 1 << i)
      .boxed()
      .sorted(Comparator.comparingInt(Integer::bitCount))
      .mapToInt(Integer::intValue)
      .toArray()
  ));

  static void main() {
//    new Day10().runAndPrintAll();

//    new Day10().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day10test1"));
//    new Day10().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day10"));
//    new Day10().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day10test1"));
//    new Day10().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day10test2"));
//    new Day10().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day10"));

//    new Day10().benchmark(JmhBenchmarkOption.PART_1);
    new Day10().benchmark(JmhBenchmarkOption.PART_2);
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    final var lines = reader.lines().toArray(String[]::new);

    var total = 0;
    for (var i = 0; i < lines.length; i++) {
      final var line = lines[i];
      final var parts = line.split(" ");

      final var desiredStateStr = parts[0];
      var desiredState = 0;
      for (var j = 1; j < desiredStateStr.length() - 1; j++) {
        var c = desiredStateStr.charAt(j);
        var state = c == '#' ? 1 : 0;
        desiredState |= state << (j - 1);
      }

      final var buttonWiring = new int[parts.length - 2];
      for (var j = 1; j < parts.length - 1; j++) {
        final var btnStr = parts[j];
        var btn = 0;

        for (var k = 1; k < btnStr.length(); k += 2) {
          btn |= 1 << (btnStr.charAt(k) - '0');
        }

        buttonWiring[j - 1] = btn;
      }

      for (var j = 1; j < (1 << buttonWiring.length); j++) {
        final var pattern = PATTERNS.get(buttonWiring.length)[j];

        var result = desiredState;
        for (var btn = 0; btn < buttonWiring.length; btn++) {
          result ^= (-(pattern & (1 << btn)) >> 31) & buttonWiring[btn];
        }

        if (result == 0) {
          total += Integer.bitCount(pattern);
          break;
        }
      }
    }

    return total;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    // adapted from https://www.reddit.com/r/adventofcode/comments/1pk87hl/2025_day_10_part_2_bifurcate_your_way_to_victory/

    var lines = reader.lines().toArray(String[]::new);

    var total = 0;

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];
      var parts = line.split(" ");

      var desiredStateStr = parts[parts.length - 1].substring(1, parts[parts.length - 1].length() - 1).split(",");
      var desiredState = new int[parts[0].length() - 2];
      for (var j = 0; j < desiredStateStr.length; j++) {
        desiredState[j] = Integer.parseInt(desiredStateStr[j]);
      }

      var btnWiring = new int[parts.length - 2][];
      var btnWiringMasks = new int[parts.length - 2];

      for (var j = 1; j < parts.length - 1; j++) {
        var btnStr = parts[j];
        var btn = new int[parts[0].length() - 2];
        var btnMask = 0;

        for (var k = 1; k < btnStr.length(); k += 2) {
          var b = btnStr.charAt(k) - '0';
          btn[b] = 1;
          btnMask |= 1 << b;
        }

        btnWiring[j - 1] = btn;
        btnWiringMasks[j - 1] = btnMask;
      }

      var cache = new HashMap<String, Integer>();
      var count = countBtnPresses(desiredState, btnWiring, btnWiringMasks, cache);
      total += count;
    }

    return total;
  }

  private static int countBtnPresses(int[] desiredState, int[][] btnWiring, int[] btnWiringMasks, Map<String, Integer> cache) {
    var key = Arrays.toString(desiredState);
    if (cache.containsKey(key)) {
      return cache.get(key);
    }

    if (Arrays.stream(desiredState).anyMatch(x -> x < 0)) {
      return 0xffff;
    }

    if (Arrays.stream(desiredState).allMatch(x -> x == 0)) {
      return 0;
    }

    var oddMask = 0;
    for (var i = 0; i < desiredState.length; i++) {
      oddMask |= (desiredState[i] % 2) << i;
    }

//    if (oddMask == 0) {
//      if (Arrays.stream(desiredState).anyMatch(x -> x % 2 != 0)) {
//        throw new IllegalStateException();
//      }
//
//      var copy = Arrays.copyOf(desiredState, desiredState.length);
//
//      var t = 0;
//      for (var i = 0; i < desiredState.length; i++) {
//        t |= (copy[i] /= 2);
//      }
//
//      if (t == 0) {
//        if (Arrays.stream(desiredState).anyMatch(x -> x != 0)) {
//          throw new IllegalStateException();
//        }
//
//        cache.put(key, 0);
//        return 0;
//      }
//
//      var c = 2 * countBtnPresses(copy, btnWiring, btnWiringMasks, cache);
//      cache.put(key, c);
//      return c;
//    }

    var min = 0xffff;
    patternLoop:
    for (var i = 0; i < (1 << btnWiringMasks.length); i++) {
      final var pattern = PATTERNS.get(btnWiringMasks.length)[i];

      var result = oddMask;
      for (var btn = 0; btn < btnWiringMasks.length; btn++) {
        result ^= (-(pattern & (1 << btn)) >> 31) & btnWiringMasks[btn];
      }

      if (result == 0) {
        var copy = Arrays.copyOf(desiredState, desiredState.length);

        for (var btn = 0; btn < btnWiring.length; btn++) {
          if ((pattern & (1 << btn)) != 0) {
            var wiring = btnWiring[btn];
            for (var j = 0; j < wiring.length; j++) {
              copy[j] -= wiring[j];
              if (copy[j] < 0) {
                continue patternLoop;
              }
            }
          }
        }

        for (var j = 0; j < copy.length; j++) {
          copy[j] /= 2;
        }

        var count = 2 * countBtnPresses(copy, btnWiring, btnWiringMasks, cache);
        min = Math.min(min, count + Integer.bitCount(pattern));
      }
    }

    cache.put(key, min);
    return min;
  }
}
