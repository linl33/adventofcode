package dev.linl33.adventofcode.year2022;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day11 extends AdventSolution2022<Long, Long> {
  private static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;

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

    if (monkeyCount > SPECIES.length()) {
      // shouldn't happen, don't want to handle it
      throw new IllegalArgumentException();
    }

    var items = new float[MAX_ITEMS];
    var itemsPosition = new int[MAX_ITEMS];
    var itemsPointer = 0;

    var offsets = new float[monkeyCount];
    var multipliers = new float[monkeyCount];

    var moduli = new float[monkeyCount];
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

    var mask = SPECIES.indexInRange(0, monkeyCount);
    var modV = FloatVector.fromArray(SPECIES, moduli, 0, mask);

    for (int i = 0; i < itemsPointer; i++) {
      var worriesVector = FloatVector.broadcast(SPECIES, items[i]);
      var pos = itemsPosition[i];

      for (int round = 0; round < PART_2_ROUNDS; round++) {
        inspectionCounts[pos]++;

        worriesVector = worriesVector
            .fma(
                pos == squareMonkey ? worriesVector : FloatVector.broadcast(SPECIES, multipliers[pos]),
                FloatVector.broadcast(SPECIES, offsets[pos])
            );

        // compute mod with using a - (a/d) * d
        var tmp = worriesVector
            .div(modV)
            .convert(VectorOperators.F2I, 0)
            .convert(VectorOperators.I2F, 0)
            .mul(modV);
        worriesVector = worriesVector.sub(tmp);

        var posNext = worriesVector.test(VectorOperators.IS_DEFAULT).laneIsSet(pos) ? trueValues[pos] : falseValues[pos];
        if (posNext > pos) {
          round--;
        }
        pos = posNext;
      }
    }

    return calculateLevel(inspectionCounts);
  }

  private static long calculateLevel(int[] inspectionCounts) {
    var highest = -1;
    var secondHighest = -1;
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

    return Math.multiplyFull(highest, secondHighest);
  }
}
