package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.util.MathUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;

public class Day8 extends AdventSolution2023<Integer, Long> {
  public static void main(String[] args) {
    new Day8().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws IOException {
    return (int) stepsToZ(
      reader,
      0b00001_00001_00001,
      (26 << 10) | (26 << 5) | 26,
      0b11111_11111_11111
    );
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws IOException {
    return stepsToZ(reader, 1, 26, 0b11111);
  }

  private static long stepsToZ(
    @NotNull BufferedReader reader,
    final int startingNode,
    final int exitNode,
    final int nodeMask
  ) throws IOException  {
    var nav = parseNavInstructions(reader.readLine());
    reader.readLine();

    var lines = reader.lines().toArray(String[]::new);
    var map = new int[1 << 15];
    var startingNodes = new int[lines.length / 2];
    var startingNodesSize = 0;

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];

      var key = 0;
      for (int j = 0; j < 3; j++) {
        key = (key << 5) | (line.charAt(j) & 0b11111);
      }
      key &= (1 << 15) - 1;

      for (int j = 7; j < 10; j++) {
        map[key] = (map[key] << 5) | (line.charAt(j) & 0b11111);
      }
      map[key] <<= 1;

      for (int j = 12; j < 15; j++) {
        map[key] = (map[key] << 5) | (line.charAt(j) & 0b11111);
      }

      if ((key & nodeMask) == startingNode) {
        startingNodes[startingNodesSize++] = key;
      }
    }

    var lcm = 1L;
    for (int i = 0; i < startingNodesSize; i++) {
      var curr = startingNodes[i] & ((1 << 15) - 1);
      var nodeSteps = 0;

      // exit nodes are only reached at nodeSteps == 0 (mod instruction length)
      while ((curr & nodeMask) != exitNode) {
        for (int j = 0; j < nav.length; j++) {
          curr = Integer.compress(map[curr], nav[j]) & ((1 << 15) - 1);
        }

        nodeSteps += nav.length;
      }

      lcm = MathUtil.lcm(lcm, nodeSteps);
    }

    return lcm;
  }

  private static int[] parseNavInstructions(String instructions) {
    var parsedInstructions = new int[instructions.length()];

    for (int i = 0; i < parsedInstructions.length; i++) {
      parsedInstructions[i] = instructions.codePointAt(i) == 'L' ? 0xffff0000 : 0xffff;
    }

    return parsedInstructions;
  }
}
