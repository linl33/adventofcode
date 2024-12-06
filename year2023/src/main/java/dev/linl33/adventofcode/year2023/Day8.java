package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.jmh.JmhBenchmarkOption;
import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import dev.linl33.adventofcode.lib.util.MathUtil;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class Day8 extends AdventSolution2023<Integer, Long> {
  private static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

  public static void main(String[] args) {
//    new Day8().runAndPrintAll();
    new Day8().benchmark(JmhBenchmarkOption.PART_2, JmhBenchmarkOption.PERF_PROFILE);

//    new Day8().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day8test3"));
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

    // TODO: vector size check

    // Vector API limitation,
    if (startingNodes.length != SPECIES.length()) {
      startingNodes = Arrays.copyOf(startingNodes, SPECIES.length());
    }

    var mask = (1L << startingNodesSize) - 1;

    IntVector v = null;
//    VectorMask<Integer> reachedExitMask;
    var lcm = 1L;

    var nodeSteps = 0;
    while (startingNodesSize > 0) {
      var vMask = VectorMask.fromLong(SPECIES, mask);
//      System.out.println(vMask);
//      System.out.println(Arrays.toString(startingNodes));
      var reachedExitMask = 0L;

      do {
        for (int i = 0; i < nav.length; i++) {
//          System.out.println(i);

          v = IntVector
            .fromArray(SPECIES, map, 0, startingNodes, 0, vMask)
            ;
//            .lanewise(VectorOperators.COMPRESS_BITS, nav[i]);

          if (nav[i] == 0xffff0000) {
            v = v.lanewise(VectorOperators.LSHR, 16);
          }

          v = v.and(0xffff);

          v.intoArray(startingNodes, 0, vMask);
        }

        nodeSteps += nav.length;
//        System.out.println(nodeSteps);
        reachedExitMask = v.and(nodeMask).eq(exitNode).and(vMask).toLong();
      } while (reachedExitMask == 0);

      startingNodesSize -= Long.bitCount(reachedExitMask);
      mask &= ~reachedExitMask;
      lcm = MathUtil.lcm(lcm, nodeSteps);

//      System.out.println(nodeSteps);
//      System.out.println(Long.toBinaryString(reachedExitMask));
//      System.out.println(Long.toBinaryString(mask));
    }

//    for (int i = 0; i < startingNodesSize; i++) {
//      var curr = startingNodes[i] & ((1 << 15) - 1);
//      var nodeSteps = 0;
//
//      // exit nodes are only reached at nodeSteps == 0 (mod instruction length)
//      while ((curr & nodeMask) != exitNode) {
//        for (int j = 0; j < nav.length; j++) {
//          curr = Integer.compress(map[curr], nav[j]) & ((1 << 15) - 1);
//        }
//
//        nodeSteps += nav.length;
//      }
//
//      lcm = MathUtil.lcm(lcm, nodeSteps);
//    }

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
