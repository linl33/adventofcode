package dev.linl33.adventofcode.year2022;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day11 extends AdventSolution2022<Long, Long> {
  private static final int MAX_ITEMS = 50;
  private static final int PART_1_ROUNDS = 20;
  private static final int PART_2_ROUNDS = 10_000;

  public static void main(String[] args) {
    new Day11().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    var input = reader.lines().toArray(String[]::new);
    var monkeyCount = (input.length + 1) / 7;

    var items = new int[MAX_ITEMS];
    var itemsPosition = new int[MAX_ITEMS];
    var itemsPointer = 0;

    var offsets = new int[monkeyCount];
    var multipliers = new int[monkeyCount];

    var moduli = new int[monkeyCount];
    var trueValues = new int[monkeyCount];
    var falseValues = new int[monkeyCount];

    var squareMonkey = 0;

    for (int m = 0, i = 0; m < monkeyCount; m++, i += 7) {
      // all items are 2-digit
      for (int j = 18; j < input[i + 1].length(); j += 4) {
        itemsPosition[itemsPointer] = m;
        items[itemsPointer++] = Integer.parseInt(input[i + 1], j, j + 2, 10);
      }

      var modulus = Integer.parseInt(input[i + 3], 21, input[i + 3].length(), 10);
      moduli[m] = modulus;

      var trueVal = Integer.parseInt(input[i + 4], 29, input[i + 4].length(), 10);
      trueValues[m] = trueVal;

      var falseVal = Integer.parseInt(input[i + 5], 30, input[i + 5].length(), 10);
      falseValues[m] = falseVal;

      var op = input[i + 2].charAt(23);
      if (op == '+') {
        var opVal = Integer.parseInt(input[i + 2], 25, input[i + 2].length(), 10);
        offsets[m] = opVal;
        multipliers[m] = 1;
      } else {
        if (input[i + 2].charAt(25) == 'o') {
          // handle new = old * old
          squareMonkey = m;
        } else {
          var opVal = Integer.parseInt(input[i + 2], 25, input[i + 2].length(), 10);
          multipliers[m] = opVal;
        }
      }
    }

    var inspectionCounts = new int[monkeyCount];

    for (int i = 0; i < itemsPointer; i++) {
      var worry = items[i];

      var pos = itemsPosition[i];
      for (int round = 0; round < PART_1_ROUNDS; round++) {
        inspectionCounts[pos]++;

        worry = (worry * (pos == squareMonkey ? worry : multipliers[pos]) + offsets[pos]) / 3;
        var posNext = worry % moduli[pos] == 0 ? trueValues[pos] : falseValues[pos];
        if (posNext > pos) {
          round--;
        }
        pos = posNext;
      }
    }

    return calculateLevel(inspectionCounts);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    var input = reader.lines().toArray(String[]::new);
    var monkeyCount = (input.length + 1) / 7;

    var items = new int[MAX_ITEMS];
    var itemsPosition = new int[MAX_ITEMS];
    var itemsPointer = 0;

    var offsets = new int[monkeyCount * monkeyCount];
    var multipliers = new int[monkeyCount * monkeyCount];

    var moduli = new int[monkeyCount];
    var trueValues = new int[monkeyCount];
    var falseValues = new int[monkeyCount];

    var squareMonkey = 0;

    for (int m = 0, i = 0; m < monkeyCount; m++, i += 7) {
      // all items are 2-digit
      for (int j = 18; j < input[i + 1].length(); j += 4) {
        itemsPosition[itemsPointer] = m;
        items[itemsPointer++] = Integer.parseInt(input[i + 1], j, j + 2, 10);
      }

      var modulus = Integer.parseInt(input[i + 3], 21, input[i + 3].length(), 10);
      moduli[m] = modulus;

      var trueVal = Integer.parseInt(input[i + 4], 29, input[i + 4].length(), 10);
      trueValues[m] = trueVal;

      var falseVal = Integer.parseInt(input[i + 5], 30, input[i + 5].length(), 10);
      falseValues[m] = falseVal;

      var op = input[i + 2].charAt(23);
      if (op == '+') {
        var opVal = Integer.parseInt(input[i + 2], 25, input[i + 2].length(), 10);
        offsets[m * monkeyCount] = opVal;
        multipliers[m * monkeyCount] = 1;
      } else {
        if (input[i + 2].charAt(25) == 'o') {
          // handle new = old * old
          squareMonkey = m;
        } else {
          var opVal = Integer.parseInt(input[i + 2], 25, input[i + 2].length(), 10);
          multipliers[m * monkeyCount] = opVal;
        }
      }
    }

    for (int i = 0; i < offsets.length; i++) {
      var monkeyIndex = (i / monkeyCount) * monkeyCount;
      var modulus = moduli[i % monkeyCount];
      offsets[i] = offsets[monkeyIndex] % modulus;
      multipliers[i] = multipliers[monkeyIndex] % modulus;
    }

    var inspectionCounts = new int[monkeyCount];
    var worries = new int[monkeyCount];

    for (int i = 0; i < itemsPointer; i++) {
      var item = items[i];
      for (int m = 0; m < monkeyCount; m++) {
        worries[m] = item % moduli[m];
      }

      var pos = itemsPosition[i];
      for (int round = 0; round < PART_2_ROUNDS; round++) {
        inspectionCounts[pos]++;
        if (pos == squareMonkey) {
          for (int j = 0, mulIndex = squareMonkey * monkeyCount; j < monkeyCount; j++, mulIndex++) {
            multipliers[mulIndex] = worries[j] % moduli[j];
          }
        }

        for (int m = 0, opIndex = pos * monkeyCount; m < monkeyCount; m++, opIndex++) {
          worries[m] = (worries[m] * multipliers[opIndex] + offsets[opIndex]) % moduli[m];
        }

        var posNext = worries[pos] == 0 ? trueValues[pos] : falseValues[pos];
        if (posNext > pos) {
          round--;
        }
        pos = posNext;
      }
    }

    return calculateLevel(inspectionCounts);
  }

  private static long calculateLevel(int[] inspectionCounts) {
    var highest = -1L;
    var secondHighest = -1L;
    for (int value : inspectionCounts) {
      if (value > secondHighest) {
        if (value < highest) {
          secondHighest = value;
        } else {
          secondHighest = highest;
          highest = value;
        }
      }
    }

    return highest * secondHighest;
  }
}
