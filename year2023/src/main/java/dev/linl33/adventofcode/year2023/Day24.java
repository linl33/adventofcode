package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayDeque;
import java.util.Arrays;

public class Day24 extends AdventSolution2023<Integer, Long> {
  public static void main(String[] args) {
//    new Day24().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day24test1"));
//    new Day24().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day24"));
//    new Day24().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day24test1"));
    new Day24().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day24"));
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

//    var testMin = 7L;
    var testMin = 200000000000000L;
//    var testMax = 27L;
    var testMax = 400000000000000L;

    var stones = new double[lines.length][][];

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      var pos = Arrays.stream(line.split(" @ ")[0].split(", ")).map(String::trim).mapToDouble(Long::parseLong).toArray();
      var velocity = Arrays.stream(line.split(" @ ")[1].split(", ")).map(String::trim).mapToDouble(Long::parseLong).toArray();

      stones[i] = new double[][] { pos, velocity };
    }

    var count = 0;
    for (int i = 0; i < stones.length - 1; i++) {
      var posA = stones[i][0];
      var velA = stones[i][1];
      var slopeA = velA[1] / velA[0];

      for (int j = i + 1; j < stones.length; j++) {
        var posB = stones[j][0];
        var velB = stones[j][1];
        var slopeB = velB[1] / velB[0];

        var x = (-slopeB * posB[0] + posB[1] + slopeA * posA[0] - posA[1]) / (slopeA - slopeB);
        var y = slopeA * (x - posA[0]) + posA[1];

        var t1 = (x - posA[0]) / velA[0];
        var t2 = (x - posB[0]) / velB[0];

        if (x >= testMin && x <= testMax && y >=testMin && y <= testMax && t1 > 0 && t2 > 0) {
          count++;
        }
      }
    }

    return count;
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var stones = new double[lines.length][][];

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      var pos = Arrays.stream(line.split(" @ ")[0].split(", ")).map(String::trim).mapToDouble(Long::parseLong).toArray();
      var velocity = Arrays.stream(line.split(" @ ")[1].split(", ")).map(String::trim).mapToDouble(Long::parseLong).toArray();

      stones[i] = new double[][] { pos, velocity };
    }

    var choices = new double[lines.length][][];

    for (int i = 0; i < stones.length; i++) {
      var posA = stones[i][0];
      var velA = stones[i][1];
//      var slopeA = velA[1] / velA[0];

      var ranges = new double[2][4];
      ranges[0][0] = posA[0];
      ranges[0][1] = 1L << 52;
      ranges[0][2] = -10_000;
      ranges[0][3] = velA[0];

      ranges[1][0] = 0;
      ranges[1][1] = posA[0];
      ranges[1][2] = velA[0];
      ranges[1][3] = 10_000;

      choices[i] = ranges;
      System.out.println(STR."\{Arrays.toString(ranges[0])} \{Arrays.toString(ranges[1])}");
//      if (velA[0] > 0) {
//        ranges[0][0] = posA[0] + 1;
//        ranges[0][1] = 1L << 52;
//        ranges[0][2] = -10_000;
//        ranges[0][3] = velA[0];
//
//        ranges[1][0] = 0;
//        ranges[1][1] = posA[0];
//        ranges[1][2] = velA[0] + 1;
//        ranges[1][3] = 10_000;
//      } else {
//        ranges[0][0] = posA[0] + 1;
//        ranges[0][1] = 1L << 52;
//        ranges[0][2] = -10_000;
//        ranges[0][3] = velA[0];
//
//        ranges[1][0] = 0;
//        ranges[1][1] = posA[0];
//        ranges[1][2] = velA[0] + 1;
//        ranges[1][3] = 10_000;
//      }
    }

    var stack = new ArrayDeque<Integer>();
    var rangeStack = new ArrayDeque<double[]>();

    stack.push(1);
    stack.push(1);
    rangeStack.push(choices[0][0]);
    rangeStack.push(choices[0][1]);
    while (!stack.isEmpty()) {
      int currIdx = stack.pop();
      var currRange = rangeStack.pop();

      var currPosMin = currRange[0];
      var currPosMax = currRange[1];
      var currVelMin = currRange[2];
      var currVelMax = currRange[3];

      if (currIdx == choices.length) {
        // TODO:
        System.out.println("done");
        System.out.println(STR."\{Arrays.toString(currRange)} \{Math.abs(currRange[0] - currRange[1])} \{Math.abs(currRange[2] - currRange[3])}");
        continue;
      }

//      System.out.println(currIdx);
      for (double[] choice : choices[currIdx]) {
        var choicePosMin = choice[0];
        var choicePosMax = choice[1];
        var choiceVelMin = choice[2];
        var choiceVelMax = choice[3];

        var updatedPosMin = Math.max(currPosMin, choicePosMin);
        var updatedPosMax = Math.min(currPosMax, choicePosMax);
        var updatedVelMin = Math.max(currVelMin, choiceVelMin);
        var updatedVelMax = Math.min(currVelMax, choiceVelMax);

        if (updatedPosMax > updatedPosMin && updatedVelMax > updatedVelMin) {
          stack.push(currIdx + 1);
          rangeStack.push(new double[] { updatedPosMin, updatedPosMax, updatedVelMin, updatedVelMax });
        }
      }
    }

    // TODO: solve 9x9 for T, {X, Y, Z}_STONE, V_STONE_{X, Y, Z}
    //       X_0 + V_0^X * T = X_STONE + V_STONE^X * T
    //       Y_0 + V_0^Y * T = Y_STONE + V_STONE^Y * T
    //       Z_0 + V_0^Z * T = Z_STONE + V_STONE^Z * T

    return -1L;
  }
}
