package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Day11 extends AdventSolution2024<Long, Long> {
  public static void main() {
    new Day11().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws IOException {
    return simulate(reader, 25);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws IOException {
    return simulate(reader, 75);
  }

  private static long simulate(BufferedReader reader, int rounds) throws IOException {
    var line = reader.readLine();
    var stones = AdventUtil.buildFreqMap(Arrays.stream(line.split(" ")).map(Long::parseLong).toList());

    for (int i = 0; i < rounds; i++) {
      var newStones = new HashMap<Long, Long>();

      stones.forEach((num, freq) -> {
        if (num == 0) {
          newStones.compute(1L, (_, v) -> v == null ? freq : v + freq);
        } else if (integerLength(num) % 2 == 0) {
          var divisor = (long) Math.pow(10, integerLength(num) / 2);

          newStones.compute(num / divisor, (_, v) -> v == null ? freq : v + freq);
          newStones.compute(num % divisor, (_, v) -> v == null ? freq : v + freq);
        } else {
          newStones.compute(num * 2024, (_, v) -> v == null ? freq : v + freq);
        }
      });

      stones = newStones;
    }

    return stones.values().stream().mapToLong(x -> x).sum();
  }

  private static int integerLength(long num) {
    return (int) Math.log10(num) + 1;
  }
}
