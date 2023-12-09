package dev.linl33.adventofcode.year2023;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Day9 extends AdventSolution2023<Long, Long> {
  public static void main(String[] args) {
    new Day9().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    return solve(reader, -1);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    return solve(reader, 0);
  }

  private static long solve(@NotNull BufferedReader reader, int dir) {
    var lines = reader.lines().toArray(String[]::new);

    var sum = 0L;
    for (int i = 0; i < lines.length; i++) {
      sum += extrapolate(lines[i], dir);
    }

    return sum;
  }

  private static long extrapolate(String line, int dir) {
    var nums = Arrays.stream(line.split(" ")).mapToInt(Integer::valueOf).toArray();

    var differences = new ArrayList<int[]>();
    differences.add(nums);

    var doWhile = true;
    while (doWhile) {
      var idx = differences.size() - 1;
      var currDifference = differences.get(idx);
      var nextDifference = new int[currDifference.length - 1];
      differences.add(nextDifference);

      for (int i = 0; i < currDifference.length - 1; i++) {
        nextDifference[i] = currDifference[i + 1] - currDifference[i];
      }

      doWhile = false;
      for (long l : nextDifference) {
        if (l != 0) {
          doWhile = true;
          break;
        }
      }
    }

    var nextNum = 0L;
    for (int i = differences.size() - 1; i >= 0; i--) {
      nextNum = differences.get(i)[Math.floorMod(dir, differences.get(i).length)] + nextNum * (dir == 0 ? -1 : 1);
    }

    return nextNum;
  }
}
