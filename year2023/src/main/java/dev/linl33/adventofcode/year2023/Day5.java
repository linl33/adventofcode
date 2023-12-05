package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

public class Day5 extends AdventSolution2023<Integer, Integer> {
  public static void main(String[] args) {
    new Day5().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    return solve(reader, seeds -> {
      var seedArr = new long[seeds.length][];
      for (int i = 0; i < seeds.length; i++) {
        seedArr[i] = new long[] { seeds[i], seeds[i], 1 };
      }
      Arrays.sort(seedArr, Comparator.comparingLong(arr -> arr[0]));
      return seedArr;
    });

    // TODO: forward traversal slightly faster for part 1
//    var groups = AdventUtil.readInputGrouped(reader).map(Stream::toList).toList();
//
//    var seeds = Arrays.stream(groups.getFirst().getFirst().substring(7).split(" ")).mapToLong(Long::valueOf).toArray();
//    var maps = groups
//        .stream()
//        .skip(1)
//        .map(s -> s
//            .stream()
//            .skip(1)
//            .map(line -> Arrays.stream(line.split(" ", 3)).mapToLong(Long::valueOf).toArray())
//            .toArray(long[][]::new)
//        )
//        .toArray(long[][][]::new);
//
//    var min = Long.MAX_VALUE;
//
//    for (var seed : seeds) {
//      var curr = seed;
//
//      for (int i = 0; i < maps.length; i++) {
//        var map = maps[i];
//
//        for (int j = 0; j < map.length; j++) {
//          var mapping = map[j];
//          var start = mapping[1];
//          var offset = mapping[0] - start;
//          var length = mapping[2];
//
//          if (curr >= start && curr < (start + length)) {
//            curr += offset;
//            break;
//          }
//        }
//      }
//
//      min = Math.min(min, curr);
//    }
//
//    return (int) min;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    return solve(reader, seeds -> {
      var seedArr = new long[seeds.length / 2][];
      for (int i = 0; i < seeds.length; i += 2) {
        seedArr[i / 2] = new long[] { seeds[i], seeds[i], seeds[i + 1] };
      }
      Arrays.sort(seedArr, Comparator.comparingLong(arr -> arr[0]));
      return seedArr;
    });
  }

  private static int solve(@NotNull BufferedReader reader, Function<long[], long[][]> seedParser) {
    var groups = AdventUtil.readInputGrouped(reader).map(Stream::toList).toList();

    var seeds = Arrays.stream(groups.getFirst().getFirst().substring(7).split(" ")).mapToLong(Long::valueOf).toArray();
    var maps = groups
        .stream()
        .skip(1)
        .map(s -> s
            .stream()
            .skip(1)
            .map(line -> Arrays.stream(line.split(" ")).mapToLong(Long::valueOf).toArray())
            .toArray(long[][]::new)
        )
        .toArray(long[][][]::new);

    for (int i = 0; i < maps.length; i++) {
      var map = maps[i];
      Arrays.sort(map, Comparator.comparingLong(arr -> arr[0]));

      var discontinuities = 0;
      var end = 0L;

      for (int j = 0; j < map.length; j++) {
        var startNext = map[j][0];
        var endNext = startNext + map[j][2];

        if (end != startNext) {
          discontinuities++;

          // TODO: remove
          if (end > startNext) {
            throw new IllegalStateException();
          }
        }

        end = endNext;
      }

      if (discontinuities > 0) {
        map = Arrays.copyOf(map, map.length + discontinuities);
        end = 0L;

        for (int j = 0; j < map.length; j++) {
          var startNext = map[j][0];
          var endNext = startNext + map[j][2];

          if (end == startNext) {
            end = endNext;
            continue;
          }

          var newDst = end;
          var newLength = startNext - end;

          System.arraycopy(map, j, map, j + 1, map.length - j - 1);
          map[j] = new long[] { newDst, newDst, newLength };

          end = startNext;
        }

        maps[i] = map;
      }
    }

    return (int) findFirstLocation(
        maps,
        seedParser.apply(seeds),
        maps[maps.length - 1][0][0],
        maps[maps.length - 1][0][1],
        maps[maps.length - 1][0][2],
        maps.length - 2
    );
  }

  private static long findFirstLocation(
      long[][][] maps,
      long[][] seeds,
      long locationRangeStart,
      long layerRangeStart,
      long rangeLength,
      int layer
  ) {
    var layerRangeEnd = layerRangeStart + rangeLength;

    if (layer < 0) {
      for (int i = 0; i < seeds.length; i++) {
        var seedRange = seeds[i];
        if ((seedRange[0] >= layerRangeStart && seedRange[0] < layerRangeEnd)
            || (layerRangeStart >= seedRange[0] && layerRangeStart < (seedRange[0] + seedRange[2]))
        ) {
          var offset = Math.max(seedRange[0], layerRangeStart) - layerRangeStart;
          return locationRangeStart + offset;
        }
      }

      return -1L;
    }

    var map = maps[layer];

    var maskStart = indexOfMapMask(layerRangeStart, map);
    var maskEnd = indexOfMapMask(layerRangeEnd - 1, map);

    // this can only happen if locationRangeStart is greater than the highest mapping in map
    if (maskStart == 0L) {
      return findFirstLocation(maps, seeds, locationRangeStart, layerRangeStart, rangeLength, layer - 1);
    }

    var firstMapping = Long.numberOfTrailingZeros(maskStart);
    var lastMapping = Math.min(Long.numberOfTrailingZeros(maskEnd), map.length - 1);

    var locationSubRangeStart = locationRangeStart;
    var layerSubRangeStart = layerRangeStart;
    for (int i = firstMapping; i <= lastMapping; i++) {
      var mapping = map[i];

      var offset = layerSubRangeStart - mapping[0];
      var nextLayerRangeStart = mapping[1] + offset;
      var remainingRangeLength = rangeLength - (locationSubRangeStart - locationRangeStart);
      var subRangeLength = Math.min(remainingRangeLength, mapping[2] - offset);

      var firstLocation = findFirstLocation(
          maps,
          seeds,
          locationSubRangeStart,
          nextLayerRangeStart,
          subRangeLength,
          layer - 1
      );
      if (firstLocation >= 0) {
        return firstLocation;
      }

      locationSubRangeStart += subRangeLength;
      layerSubRangeStart += subRangeLength;
    }

    if (maskEnd == 0L) {
      var firstLocation = findFirstLocation(
          maps,
          seeds,
          locationSubRangeStart,
          layerRangeStart,
          rangeLength - (locationSubRangeStart - locationRangeStart),
          layer - 1
      );
      if (firstLocation >= 0) {
        return firstLocation;
      }
    }

    return -1;
  }

  private static long indexOfMapMask(long num, long[][] map) {
    var mask = 0L;

    for (var i = 0; i < map.length; i++) {
      var mapping = map[i];
      var start = mapping[0];
      var end = start + mapping[2];

      var inRange = num >= start && num < end;
      mask |= (inRange ? 1L : 0L) << i;
    }

    return mask;
  }
}
