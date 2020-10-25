package dev.linl33.adventofcode.year2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.IntStream;

public class Day4 extends AdventSolution2019<Long, Long> {
  public static void main(String[] args) {
    new Day4().runAndPrintAll();
  }

  @Override
  public Long part1(BufferedReader reader) throws IOException {
    String[] input = reader.readLine().split("-");
    var leftBound = Integer.parseInt(input[0]);
    var rightBound = Integer.parseInt(input[1]);

    return generateCandidates(leftBound, rightBound)
        .filter(Day4::checkCriteriaPart1)
        .count();
  }

  @Override
  public Long part2(BufferedReader reader) throws IOException {
    String[] input = reader.readLine().split("-");
    var leftBound = Integer.parseInt(input[0]);
    var rightBound = Integer.parseInt(input[1]);

    return generateCandidates(leftBound, rightBound)
        .filter(Day4::checkCriteriaPart2)
        .count();
  }

  private static IntStream generateCandidates(int start, int end) {
    var builder = IntStream.builder();
    int mostSignificantDigitMax = end / 100000;

    for (int i1 = 1; i1 <= mostSignificantDigitMax; i1++) {
      for (int i2 = i1; i2 <= 9; i2++) {
        for (int i3 = i2; i3 <= 9; i3++) {
          for (int i4 = i3; i4 <= 9; i4++) {
            for (int i5 = i4; i5 <= 9; i5++) {
              for (int i6 = i5; i6 <= 9; i6++) {
                int next = i1 * 100000 + i2 * 10000 + i3 * 1000 + i4 * 100 + i5 * 10 + i6;

                if (next >= start && next <= end) {
                  builder.add(next);
                }
              }
            }
          }
        }
      }
    }

    return builder.build();
  }

  public static boolean checkCriteriaPart1(int input) {
    String inputStr = Integer.toString(input);

    for (int i = 0; i < inputStr.length() - 1; i++) {
      if (inputStr.charAt(i) == inputStr.charAt(i + 1)) {
        return true;
      }
    }

    return false;
  }

  public static boolean checkCriteriaPart2(int input) {
    String inputStr = Integer.toString(input);

    for (int i = 0; i < inputStr.length() - 1; i++) {
      if (inputStr.charAt(i) != inputStr.charAt(i + 1)) {
        continue;
      }

      if (i > 0) {
        if (inputStr.charAt(i - 1) == inputStr.charAt(i)) {
          input = input + 2;
          continue;
        }
      }

      if (i < inputStr.length() - 2) {
        if (inputStr.charAt(i + 2) == inputStr.charAt(i)) {
          input = input + 2;
          continue;
        }
      }

      return true;
    }

    return false;
  }
}
