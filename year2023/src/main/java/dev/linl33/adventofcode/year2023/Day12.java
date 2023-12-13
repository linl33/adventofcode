package dev.linl33.adventofcode.year2023;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 extends AdventSolution2023<Long, Long> {
  public static void main(String[] args) {
    new Day12().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);

    var sum = 0L;
    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      sum += countArrangements(line);
    }

    return sum;
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);

    var sum = 0L;
    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      var space = line.indexOf(' ');

      var finalLine = line;
      var repeat = 5;
      var replace = "?";
      line = STR."\{Stream.generate(() -> finalLine.substring(0, space)).limit(repeat).collect(Collectors.joining(replace))} \{Stream.generate(() -> finalLine.substring(space + 1)).limit(repeat).collect(Collectors.joining(","))}";
      sum += countArrangements(line);
    }

    return sum;
  }

  private static long countArrangements(String line) {
    var space = line.indexOf(' ');
    var initState = line.substring(0, space);

    var groups = Arrays.stream(line.substring(space + 1).split(",")).mapToInt(Integer::parseInt).toArray();
    return countArrangementsRecursive(initState.codePoints().toArray(), groups, new long[(1 << (5 + 7)) - 1]);
  }

  private static long countArrangementsRecursive(int[] curr, int[] groups, long[] cache) {
    var currGroups = new int[groups.length];

    var statePtr = 0;
    var groupPtr = -1;
    var seek = '\0';
    var cacheKey = statePtr;

    for (; statePtr < curr.length; statePtr++) {
      if (seek == '\0') {
        var cachedValue = cache[cacheKey];
        if (cachedValue != 0) {
          return cachedValue - 1;
        }
      }

      var c = curr[statePtr];

      if (seek != '\0' && c != '?' && c != seek) {
        break;
      }

      if (c == '?') {
        if (seek == '\0') {
          curr[statePtr] = '#';
          var left = countArrangementsRecursive(curr, groups, cache);

          curr[statePtr] = '.';
          var right = countArrangementsRecursive(curr, groups, cache);

          curr[statePtr] = '?';

          cache[cacheKey] = left + right + 1;
          return left + right;
        } else {
          c = seek;
        }
      }

      if (c == '.') {
        cacheKey = statePtr | ((groupPtr + 1) << 7);
        seek = '\0';
      } else if (c == '#') {
        if (seek == '\0') {
          groupPtr++;
          if (groupPtr == currGroups.length) {
            break;
          }
        }

        currGroups[groupPtr]++;
        seek = currGroups[groupPtr] == groups[groupPtr] ? '.' : '#';
      }
    }

    return statePtr == curr.length && groupPtr == groups.length - 1 && currGroups[groupPtr] == groups[groupPtr] ? 1 : 0;
  }
}
