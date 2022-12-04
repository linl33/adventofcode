package dev.linl33.adventofcode.year2022;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day4 extends AdventSolution2022<Integer, Integer> {
  public static void main(String[] args) {
    new Day4().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    return (int) reader
        .lines()
        .filter(line -> {
          var leftDashPos = line.indexOf('-');
          var commaPos = line.indexOf(',', leftDashPos + 1);
          var rightDashPos = line.indexOf('-', commaPos + 1);

          var leftLo = Integer.parseInt(line, 0, leftDashPos, 10);
          var rightLo = Integer.parseInt(line, commaPos + 1, rightDashPos, 10);

          if (leftLo == rightLo) {
            return true;
          }

          var leftHi = Integer.parseInt(line, leftDashPos + 1, commaPos, 10);
          var rightHi = Integer.parseInt(line, rightDashPos + 1, line.length(), 10);

          if (leftHi == rightHi) {
            return true;
          }

          return leftLo > rightLo ? (leftLo <= rightHi && leftHi <= rightHi) : (rightLo <= leftHi && rightHi <= leftHi);
        })
        .count();
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    return (int) reader
        .lines()
        .filter(line -> {
          var leftDashPos = line.indexOf('-');
          var commaPos = line.indexOf(',', leftDashPos + 1);
          var rightDashPos = line.indexOf('-', commaPos + 1);

          var leftLo = Integer.parseInt(line, 0, leftDashPos, 10);
          var rightLo = Integer.parseInt(line, commaPos + 1, rightDashPos, 10);

          if (leftLo == rightLo) {
            return true;
          }

          var leftHi = Integer.parseInt(line, leftDashPos + 1, commaPos, 10);
          var rightHi = Integer.parseInt(line, rightDashPos + 1, line.length(), 10);

          if (leftHi == rightHi) {
            return true;
          }

          return leftLo > rightLo ? (leftLo <= rightHi) : (rightLo <= leftHi);
        })
        .count();
  }
}
