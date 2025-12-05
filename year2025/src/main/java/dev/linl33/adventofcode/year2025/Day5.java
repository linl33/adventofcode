package dev.linl33.adventofcode.year2025;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import jdk.incubator.vector.LongVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;

public class Day5 extends AdventSolution2025<Long, Long> {
  private static final VectorSpecies<Long> LONG_SPECIES = LongVector.SPECIES_MAX;

  static void main() {
    new Day5().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    var groups = AdventUtil.readInputGrouped(reader).map(x -> x.toArray(String[]::new)).toArray(String[][]::new);

    var rangeCount = groups[0].length;
    var rangeCountAligned = Math.ceilDiv(rangeCount, LONG_SPECIES.length()) * LONG_SPECIES.length();

    var freshRanges = new long[rangeCountAligned * 2];
    for (var i = 0; i < groups[0].length; i++) {
      var line = groups[0][i];
      var sepIdx = line.indexOf('-');
      var left = Long.parseLong(line, 0, sepIdx, 10);
      var right = Long.parseLong(line, sepIdx + 1, line.length(), 10);
      freshRanges[i] = left;
      freshRanges[i + freshRanges.length / 2] = right;
    }

    var availableIdCount = groups[1].length;
    var alignedAvailableIdCount = Math.ceilDiv(availableIdCount, LONG_SPECIES.length()) * LONG_SPECIES.length();
    var availableIds = new long[alignedAvailableIdCount];
    for (var i = 0; i < groups[1].length; i++) {
      availableIds[i] = Long.parseLong(groups[1][i]);
    }

    var count = 0L;
    for (var i = 0; i < availableIds.length; i += LONG_SPECIES.length()) {
      var vec = LongVector.fromArray(LONG_SPECIES, availableIds, i);
      var mask = LONG_SPECIES.maskAll(false);

      for (var j = 0; j < rangeCount; j++) {
        var lo = LONG_SPECIES.broadcast(freshRanges[j] - 1);
        var hi = LONG_SPECIES.broadcast(freshRanges[j + freshRanges.length / 2] + 1);

        mask = mask.or(vec.compare(VectorOperators.GT, lo).and(vec.compare(VectorOperators.LT, hi)));
      }

      count += mask.trueCount();
    }

    return count;
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    var groups = AdventUtil.readInputGrouped(reader).map(x -> x.toArray(String[]::new)).toArray(String[][]::new);

    var freshRanges = new long[groups[0].length * 2];
    for (var i = 0; i < groups[0].length; i++) {
      var line = groups[0][i];
      var sepIdx = line.indexOf('-');
      var left = Long.parseLong(line, 0, sepIdx, 10);
      var right = Long.parseLong(line, sepIdx + 1, line.length(), 10);
      freshRanges[i * 2] = left;
      freshRanges[i * 2 + 1] = right;
    }

    var copy = new ArrayList<long[]>(freshRanges.length);
    for (var i = 0; i < freshRanges.length; i += 2) {
      copy.add(new long[] { freshRanges[i], freshRanges[i + 1] });
    }

    var rounds = copy.size() - 1;

    var count = 0L;
    for (int r = 0; r < rounds; r++) {
      var allRanges = new ArrayList<long[]>();
      for (var i = 0; i < copy.size(); i++) {
        var newRange = copy.get(i);

        var it = allRanges.listIterator();
        while (it.hasNext()) {
          var range = it.next();
          var loMax = Math.max(newRange[0], range[0]);
          var hiMin = Math.min(newRange[1], range[1]);
          if (loMax <= hiMin) {
            it.remove();
            newRange = new long[] { loMax ^ newRange[0] ^ range[0], hiMin ^ newRange[1] ^ range[1] };
            break;
          }
        }

        allRanges.add(newRange);
      }

      if (allRanges.size() == copy.size()) {
        break;
      }

      copy.clear();
      copy.addAll(allRanges);

      count = 0L;
      for (var i = 0; i < allRanges.size(); i++) {
        var range = allRanges.get(i);
        count += range[1] - range[0] + 1;
      }
    }

    return count;
  }
}
