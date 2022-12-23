package dev.linl33.adventofcode.year2022;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.ShortVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.function.IntConsumer;

public class Day23 extends AdventSolution2022<Integer, Integer> {
  private static final VectorSpecies<Integer> INT_128_SPECIES = IntVector.SPECIES_128;
  private static final VectorSpecies<Short> SHORT_128_SPECIES = ShortVector.SPECIES_128;

  private static final int PART_1_ROUNDS = 10;
  private static final int PART_2_ROUNDS = 10_000;

  private static final int[] NEIGHBORS_MASK = new int[] {
      0b000_00_111,
      0b111_00_000,
      0b001_01_001,
      0b100_10_100,

      0b000_00_111,
      0b111_00_000,
      0b001_01_001,
  };
  private static final int[] MOVEMENT_DELTA_X = new int[] {
      0, 0, -1, 1,
      0, 0, -1,
  };
  private static final int[] MOVEMENT_DELTA_Y = new int[] {
      -1, 1, 0, 0,
      -1, 1, 0,
  };

  public static void main(String[] args) {
    new Day23().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var gridDim = new int[1];
    var grid = parseGrid(reader, gridDim);
    var range = new int[] { Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE };

    simulate(
        grid,
        gridDim[0],
        PART_1_ROUNDS,
        elfPosNext -> {
          var x = elfPosNext % gridDim[0];
          var y = elfPosNext / gridDim[0];

          range[0] = Math.min(range[0], x);
          range[1] = Math.max(range[1], x);
          range[2] = Math.min(range[2], y);
          range[3] = Math.max(range[3], y);
        }
    );

    return (range[1] - range[0] + 1) * (range[3] - range[2] + 1) - grid.length;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var gridDim = new int[1];
    var grid = parseGrid(reader, gridDim);
    return simulate(grid, gridDim[0], PART_2_ROUNDS, null);
  }

  private static int[] parseGrid(@NotNull BufferedReader reader, int[] gridDimensionOut) {
    var lines = reader.lines().toArray(String[]::new);
    var height = lines.length;
    var width = lines[0].length();

    var gridDimension = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(Math.max(height, width) * 2 - 1));
    gridDimensionOut[0] = gridDimension;

    var offset = (gridDimension / 2) * gridDimension + gridDimension / 2;

    var elves = new int[height * width];
    var elfCount = 0;

    for (int y = 0, yOffset = offset; y < lines.length; y++, yOffset += gridDimension) {
      var line = lines[y];

      for (int x = 0, pos = yOffset; x < line.length(); x++, pos++) {
        if (line.charAt(x) != '#') {
          continue;
        }

        elves[elfCount++] = pos;
      }
    }

    var elvesTrimmed = new int[elfCount];
    System.arraycopy(elves, 0, elvesTrimmed, 0, elfCount);

    return elvesTrimmed;
  }

  private static int simulate(int[] elves, int gridDimension, int maxRounds, IntConsumer onElfMove) {
    var grid = new short[gridDimension * gridDimension];
    var proposedGrid = new int[grid.length];
    var proposedList = new int[elves.length];

    for (int elf : elves) {
      grid[elf] = 1;
    }

    var neighborOffsets = new int[] {
        -gridDimension - 1, -gridDimension, -gridDimension + 1,
        -1, 1,
        gridDimension - 1, gridDimension, gridDimension + 1,
    };

    for (short round = 1; round <= maxRounds; round++) {
      var directionOffset = (round - 1) % 4;
      var masksVector = IntVector.fromArray(INT_128_SPECIES, NEIGHBORS_MASK, directionOffset);

      var elfIdxNext = 0;
      var propositionIdx = elves.length;
      for (int elfIdx = 0; elfIdx < elves.length; elfIdx++) {
        var elf = elves[elfIdx];

        var neighborsVector = ShortVector.fromArray(SHORT_128_SPECIES, grid, elf, neighborOffsets, 0);
        var neighborsMask = neighborsVector.compare(VectorOperators.GE, round);
        var neighbors = (int) neighborsMask.toLong();

        if (neighbors != 0 && Integer.bitCount(neighbors) <= 5) {
          var direction = IntVector
              .broadcast(INT_128_SPECIES, neighbors)
              .and(masksVector)
              .test(VectorOperators.IS_DEFAULT)
              .firstTrue();

          if (direction < 4) {
            direction += directionOffset;
            var offset = MOVEMENT_DELTA_Y[direction] * gridDimension + MOVEMENT_DELTA_X[direction];

            var proposedPos = elf + offset;
            var added = proposedGrid[proposedPos] == round;
            proposedGrid[proposedPos] = round;
            proposedList[--propositionIdx] = proposedPos;
            if (added) {
              // 2 elves have proposed the same location
              // note that it is impossible for more than 2 elves to propose the same location
              // and that the other elf has to be 1 block away from the proposed location (in the direction this elf is moving)

              // find the previous entry in proposedList and remove
              for (var idx = propositionIdx + 1; idx < elves.length; idx++) {
                if (proposedList[idx] == proposedPos) {
                  System.arraycopy(proposedList, propositionIdx + 1, proposedList, propositionIdx + 2, idx - propositionIdx - 1);
                  propositionIdx += 2;
                  break;
                }
              }

              grid[(elves[elfIdxNext++] = elf)]++;
              grid[(elves[elfIdxNext++] = proposedPos + offset)]++;
            }

            continue;
          }
        }

        grid[(elves[elfIdxNext++] = elf)]++;
      }

      if (propositionIdx == elves.length) {
        return round;
      }

      System.arraycopy(proposedList, propositionIdx, elves, elfIdxNext, elves.length - propositionIdx);

      short roundNext = (short) (round + 1);
      for (int i = propositionIdx; i < elves.length; i++) {
        grid[proposedList[i]] = roundNext;
      }

      if (onElfMove != null) {
        for (int i = propositionIdx; i < elves.length; i++) {
          onElfMove.accept(proposedList[i]);
        }
      }
    }

    return maxRounds;
  }
}
