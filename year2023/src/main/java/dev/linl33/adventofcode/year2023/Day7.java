package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Comparator;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

public class Day7 extends AdventSolution2023<Integer, Integer> {
  public static void main(String[] args) {
    new Day7().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    return calculateTotalWinnings(reader, false);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    return calculateTotalWinnings(reader, true);
  }

  private static int calculateTotalWinnings(@NotNull BufferedReader reader, boolean joker) {
    var hands = reader.lines()
        .map(line -> new String[] { line.substring(0, 5), line.substring(6) })
        .sorted(
            IntStream.range(0, 5)
                .<ToIntFunction<String[]>>mapToObj(i -> (pair -> cardToStrength(pair[0].charAt(i), joker)))
                .reduce(
                    Comparator.comparingInt(pair -> findType(pair[0], joker)),
                    Comparator::thenComparingInt,
                    Comparator::thenComparing
                )
        )
        .toArray(String[][]::new);

    var sum = 0;
    for (int i = 0; i < hands.length; i++) {
      var rank = i + 1;
      var bid = Integer.parseInt(hands[i][1]);
      sum += rank * bid;
    }

    return sum;
  }

  private static int findType(String hand, boolean joker) {
    var map = AdventUtil.buildFreqMap(hand.codePoints().mapToObj(i -> (char) i).toList());

    if (joker && map.containsKey('J')) {
      var jCount = map.remove('J');
      if (map.isEmpty()) {
        return 7;
      }

      map.compute(AdventUtil.argMax(map), (_, val) -> val + jCount);
    }

    if (map.size() == 1) {
      return 7;
    }

    if (map.size() == 4) {
      return 2;
    }

    if (map.size() == 5) {
      return 1;
    }

    var maxCount = map.values().stream().mapToLong(i -> i).max().orElseThrow();
    if (map.size() == 2) {
      return maxCount == 4 ? 6 : 5;
    }

    if (map.size() == 3) {
      return maxCount == 3 ? 4 : 3;
    }

    throw new IllegalArgumentException();
  }

  private static int cardToStrength(char card, boolean joker) {
    return switch (card) {
      case 'A' -> 13;
      case 'K' -> 12;
      case 'Q' -> 11;
      case 'J' -> joker ? 0 : 10;
      case 'T' -> 9;
      case '9' -> 8;
      case '8' -> 7;
      case '7' -> 6;
      case '6' -> 5;
      case '5' -> 4;
      case '4' -> 3;
      case '3' -> 2;
      case '2' -> 1;
      default -> throw new IllegalArgumentException();
    };
  }
}
