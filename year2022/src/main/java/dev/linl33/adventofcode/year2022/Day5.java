package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day5 extends AdventSolution2022<String, String> {
  public static void main(String[] args) {
    new Day5().runAndPrintAll();
  }

  @Override
  public String part1(@NotNull BufferedReader reader) {
    return simulateCrane(reader, 1);
  }

  @Override
  public String part2(@NotNull BufferedReader reader) {
    return simulateCrane(reader, Integer.MAX_VALUE);
  }

  private static String simulateCrane(@NotNull BufferedReader reader, int maxChunkSize) {
    var input = AdventUtil.readInputGrouped(reader).map(Stream::toList).toList();

    var initState = input.get(0);
    var instructions = input.get(1);

    var stackCount = countStacks(initState);
    var stacks = parseStacks(initState);
    var tmpStack = new byte[Math.min(stacks.length - stackCount, maxChunkSize)];

    for (String instruction : instructions) {
      var offset = instruction.length() % 2;
      var cratesToMove = Integer.parseInt(instruction, 5, 5 + offset + 1, 10);
      var from = instruction.codePointAt(12 + offset) - '0' - 1;
      var to = instruction.codePointAt(17 + offset) - '0' - 1;

      var left = Math.min(from, to);
      var right = left ^ from ^ to;

      var chunkSize = Math.min(maxChunkSize, cratesToMove);
      var moveDir = Integer.signum(from - to) * chunkSize;
      var copyLengthOffset = Math.min(0, moveDir);

      for (int i = 0; i < cratesToMove; i += chunkSize) {
        var srcPointer = stacks[from];
        var dstPointer = stacks[to];
        System.arraycopy(stacks, srcPointer - chunkSize + 1, tmpStack, 0, chunkSize);

        var leftPointer = stacks[left];
        System.arraycopy(stacks, leftPointer + 1, stacks, leftPointer + 1 + moveDir, Math.abs(srcPointer - dstPointer) - chunkSize - copyLengthOffset);
        System.arraycopy(tmpStack, 0, stacks, dstPointer + 1 + copyLengthOffset, chunkSize);

        for (int j = left; j < right; j++) {
          stacks[j] += moveDir;
        }
      }
    }

    return peekTopOfStacks(stacks, stackCount);
  }

  private static int countStacks(List<String> initState) {
    var stackLabels = initState.get(initState.size() - 1);
    return Integer.parseInt(stackLabels, stackLabels.length() - 2, stackLabels.length() - 1, 10);
  }

  private static byte[] parseStacks(List<String> initState) {
    var stackLabels = initState.get(initState.size() - 1);
    var stackCount = Integer.parseInt(stackLabels, stackLabels.length() - 2, stackLabels.length() - 1, 10);

    // Deque is slow, use array
    // first n entries are used for stack size
    var stacks = new byte[stackCount * (initState.size() - 1) + initState.size()];
    for (int i = 0; i < stackCount; i++) {
      stacks[i] = (byte) (stackCount + (i + 1) * (initState.size() - 1) - 1);
    }

    for (int stateLine = 0; stateLine < initState.size() - 1; stateLine++) {
      var state = initState.get(stateLine);
      for (int i = 0; i < stackCount; i++) {
        var pos = 4 * i + 1;
        var crate = (byte) state.charAt(pos);
        var stackPointer = stacks[i];

        if (crate == ' ') {
          System.arraycopy(stacks, stackPointer + 1, stacks, stackPointer, (stacks.length - 1) - (stackPointer + 1) + 1);
          for (int j = i; j < stackCount; j++) {
            stacks[j]--;
          }
          continue;
        }

        var currDeviation = ((stackCount + (i + 1) * (initState.size() - 1) - 1) - stackPointer);
        var prevDeviation = i == 0 ? 0 : ((stackCount + (i - 1 + 1) * (initState.size() - 1) - 1) - stacks[i - 1]);
        var offset = stateLine - (currDeviation - prevDeviation);
        stacks[stackPointer - offset] = crate;
      }
    }

    // trim stacks to exact size
    stacks = Arrays.copyOf(stacks, stacks[stackCount - 1] + 1);
    return stacks;
  }

  private static String peekTopOfStacks(byte[] stacks, int stackCount) {
    var sb = new StringBuilder(stackCount);
    for (int i = 0; i < stackCount; i++) {
      sb.append((char) stacks[stacks[i]]);
    }

    return sb.toString();
  }
}
