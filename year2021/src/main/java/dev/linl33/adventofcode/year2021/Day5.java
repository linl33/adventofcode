package dev.linl33.adventofcode.year2021;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;

public class Day5 extends AdventSolution2021<Integer, Integer> {
  public static void main(String[] args) {
    new Day5().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var grid = new int[1000][1000];

    for (var line : lines) {
      var pairs = line.split(" -> ");
      var left = Arrays.stream(pairs[0].split(",")).mapToInt(Integer::parseInt).toArray();
      var right = Arrays.stream(pairs[1].split(",")).mapToInt(Integer::parseInt).toArray();

      if (left[0] == right[0]) {
        var x = left[0];

        var start = Math.min(left[1], right[1]);
        var end = Math.max(left[1], right[1]);

        for (int i = start; i <= end; i++) {
          grid[i][x]++;
        }
      } else if (left[1] == right[1]) {
        var y = left[1];

        var start = Math.min(left[0], right[0]);
        var end = Math.max(left[0], right[0]);

        for (int i = start; i <= end; i++) {
          grid[y][i]++;
        }
      }
    }

    return sumGrid(grid);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var grid = new int[1000][1000];

    for (var line : lines) {
      var pairs = line.split(" -> ");
      var left = Arrays.stream(pairs[0].split(",")).mapToInt(Integer::parseInt).toArray();
      var right = Arrays.stream(pairs[1].split(",")).mapToInt(Integer::parseInt).toArray();

      if (left[0] == right[0]) {
        var x = left[0];

        var start = Math.min(left[1], right[1]);
        var end = Math.max(left[1], right[1]);

        for (int i = start; i <= end; i++) {
          grid[i][x]++;
        }
      } else if (left[1] == right[1]) {
        var y = left[1];

        var start = Math.min(left[0], right[0]);
        var end = Math.max(left[0], right[0]);

        for (int i = start; i <= end; i++) {
          grid[y][i]++;
        }
      } else {
        if (right[0] > left[0] && right[1] > left[1]) {
          for (int i = 0; i < (right[0] - left[0] + 1); i++) {
            grid[left[1] + i][left[0] + i]++;
          }
        } else if (right[0] < left[0] && right[1] < left[1]) {
          for (int i = 0; i < (left[0] - right[0] + 1); i++) {
            grid[right[1] + i][right[0] + i]++;
          }
        } else if (right[0] > left[0] && right[1] < left[1]) {
          for (int i = 0; i < (right[0] - left[0] + 1); i++) {
            grid[left[1] - i][left[0] + i]++;
          }
        } else if (right[0] < left[0] && right[1] > left[1]) {
          for (int i = 0; i < (left[0] - right[0] + 1); i++) {
            grid[left[1] + i][left[0] - i]++;
          }
        }
      }
    }

    return sumGrid(grid);
  }

  private static int sumGrid(@NotNull int[][] grid) {
    var sum = 0;
    for (var row : grid) {
      for (var i : row) {
        sum += i >= 2 ? 1 : 0;
      }
    }

    return sum;
  }
}
