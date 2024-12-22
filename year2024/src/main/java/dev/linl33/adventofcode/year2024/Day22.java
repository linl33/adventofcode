package dev.linl33.adventofcode.year2024;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Day22 extends AdventSolution2024<Long, Integer> {
  public static void main() {
    new Day22().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var sum = 0L;
    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];
      var val = Long.parseLong(line);

      for (int n = 0; n < 2000; n++) {
        val = (val ^ (val * 64)) % 16777216;
        val = (val ^ (val / 32)) % 16777216;
        val = (val ^ (val * 2048)) % 16777216;
      }

      sum += val;
    }

    return sum;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var deltas = new long[lines.length][2000];
    var prices = new long[lines.length][2000];

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];
      var val = Long.parseLong(line);

      deltas[i] = new long[2000];

      for (int n = 0; n < 2000; n++) {
        var valCurr = val;

        val = (val ^ (val * 64L)) % 16777216L;
        val = (val ^ (val / 32L)) % 16777216L;
        val = (val ^ (val * 2048L)) % 16777216L;

        if (val < 0) {
          throw new IllegalStateException();
        }

        deltas[i][n] = (val % 10L) - (valCurr % 10L);
        prices[i][n] = val % 10L;
      }
    }

    var seqs = generateSequences();

    var pMax = seqs.stream()
      .parallel()
      .mapToLong(seq -> {
        var sum = 0L;

        for (var j = 0; j < deltas.length; j++) {
          var deltaSeries = deltas[j];

          for (int k = 0; k < deltaSeries.length - 3; k++) {
            var mismatch = Arrays.mismatch(deltaSeries, k, k + 4, seq, 0, 4);
            if (mismatch == -1) {
              sum += prices[j][k + 3];
              break;
            }
          }
        }

        return sum;
      })
      .filter(v -> v > 0)
      .peek(System.out::println)
      .max()
      .orElseThrow();

    return (int) pMax;
  }

  private static List<long[]> generateSequences() {
    var list = new ArrayList<long[]>();
    generateSequence(new long[4], 0, list::add);

    return list;
  }

  private static void generateSequence(long[] arr, int idx, Consumer<long[]> onGenerated) {
    if (idx == arr.length) {
      onGenerated.accept(arr);
      return;
    }

    for (int i = -10; i <= 10; i++) {
      var arr2 = Arrays.copyOf(arr, arr.length);
      arr2[idx] = i;
      generateSequence(arr2, idx + 1, onGenerated);
    }
  }
}
