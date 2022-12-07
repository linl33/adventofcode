package dev.linl33.adventofcode.year2022;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.function.LongConsumer;

public class Day7 extends AdventSolution2022<Long, Long> {
  private static final int STACK_SIZE = 20;
  private static final int SMALL_DIR_THRESHOLD = 100_000;
  private static final long TOTAL_SPACE = 70_000_000L;
  private static final long REQUIRED_SPACE = 30_000_000L;

  public static void main(String[] args) {
    new Day7().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    var sum = new long[1];

    visitDirectoryTree(
        reader,
        __ -> {},
        dirSize -> {
          if (dirSize <= SMALL_DIR_THRESHOLD) {
            sum[0] += dirSize;
          }
        }
    );

    return sum[0];
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    var required = new long[] { REQUIRED_SPACE - TOTAL_SPACE };
    var potential = new ArrayList<Long>();

    visitDirectoryTree(
        reader,
        fileSize -> required[0] += fileSize,
        dirSize -> {
          if (dirSize >= required[0]) {
            potential.add(dirSize);
          }
        }
    );

    var min = Long.MAX_VALUE;
    for (var size : potential) {
      if (size > required[0]) {
        min = Math.min(min, size);
      }
    }

    return min;
  }

  private static void visitDirectoryTree(@NotNull BufferedReader reader, LongConsumer onFileVisit, LongConsumer onDirectoryVisit) {
    var input = reader.lines().toArray(String[]::new);

    var stack = new long[STACK_SIZE];
    var stackPointer = stack.length / 2;

    for (int i = input.length - 1; i >= 0; i--) {
      var line = input[i];
      var firstChar = line.charAt(0);
      var thirdChar = line.charAt(2);

      if (firstChar >= '0' && firstChar <= '9') {
        var fileSize = Integer.parseInt(line, 0, line.indexOf(" "), 10);
        stack[stackPointer] += fileSize;
        onFileVisit.accept(fileSize);
      } else if (firstChar == '$' && thirdChar == 'c') {
        if (line.equals("$ cd ..")) {
          stack[++stackPointer] = 0;
        } else {
          var dirSizeTotal = stack[stackPointer--];
          stack[stackPointer] += dirSizeTotal;
          onDirectoryVisit.accept(dirSizeTotal);
        }
      }
    }
  }
}
