package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.jmh.JmhBenchmarkOption;
import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import dev.linl33.adventofcode.lib.util.AdventUtil;
import dev.linl33.adventofcode.lib.util.PrintUtil;
import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorSpecies;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

public class Day7 extends AdventSolution2023<Integer, Integer> {
  private static final VectorSpecies<Byte> SPECIES = ByteVector.SPECIES_PREFERRED;
  private static final VectorMask<Byte> HAND_MASK = VectorMask.fromLong(SPECIES, 0b11111);
  private static final byte[] CARD_STRENGTH = {
      0, 2, 3, 4, 5, 6, 7, 8, 9, 0,
      0, 0, 0, 0, 0, 0, 14, 0, 0, 0,
      0, 0, 0, 0, 0, 11, 13, 0, 0, 0,
      0, 0, 12, 0, 0, 10,
  };

  public static void main(String[] args) {
    System.out.println(CARD_STRENGTH['A' - '1']);
    System.out.println(CARD_STRENGTH['K' - '1']);
    System.out.println(CARD_STRENGTH['Q' - '1']);
    System.out.println(CARD_STRENGTH['J' - '1']);
    System.out.println(CARD_STRENGTH['T' - '1']);
    System.out.println(CARD_STRENGTH['9' - '1']);

//    new Day7().runAndPrintAll();
//    new Day7().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day7test1"));
//    new Day7().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day7"));
    new Day7().benchmark(JmhBenchmarkOption.PART_1);
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var hands = new int[lines.length * 7 + 13];
    var handTypeCount = new int[8];

    for (int i = 0; i < lines.length; i++) {
      // TODO: remove
      var codePoints = lines[i].codePoints().toArray();
      var v = ByteVector.fromArray(
          SPECIES,
          CARD_STRENGTH,
          -'1',
          codePoints,
          0,
          HAND_MASK
      );

      var val = Long.reverseBytes(v.reinterpretAsLongs().lane(0)) >>> (Long.SIZE - (5 * 8 + 10));
      var bid = 0;
      for (int j = 6; j < codePoints.length; j++) {
        bid = 10 * bid + (codePoints[j] & 0xF);
      }
      val |= bid;

      var hand = (int) Long.compress(val, 0b00001111_00001111_00001111_00001111_00001111_1111_1111_11L);
//      var hand = (int) Long.compress(val, 0b00001111_00001111_00001111_00001111_00001111_00000000_0000L);
//      hand |= bid;
      var handType = findType(hand, false) & 0b111;

//      var ref = findType(lines[i].substring(0, 5), false);
//      if (handType != ref) {
//        throw new IllegalArgumentException();
//      }

      hands[handType * lines.length + handTypeCount[handType]++] = hand;
//      System.out.println(STR."\{lines[i]} type \{handType} hand \{Integer.toBinaryString(hand)}");
    }

    for (int i = 1; i < 8; i++) {
      Arrays.sort(hands, i * lines.length, i * lines.length + handTypeCount[i]);
    }

    var sum = 0;
    var rank = 1; // TODO: replace with hand type size sum
    for (int i = 1; i < 8; i++) {
      var size = handTypeCount[i];
      for (int j = 0; j < size; j++) {
        var bid = hands[i * lines.length + j] & 0b1111111111;
//        System.out.println(STR."\{hands[i * lines.length + j] & 0b1111_1111_1111_1111_1111_0000_0000_0000} type \{i} bid \{bid} rank \{rank}");
        sum += bid * rank++;
      }
    }
//    PrintUtil.enhancedPrint(handTypeCount);

    return sum;
//    return calculateTotalWinnings(reader, false);
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

//      System.out.println(STR."bid \{bid} rank \{rank}");
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

  private static int findType(int hand, boolean joker) {
    var tally = new int[16];
    var tallyMask = 0;

//    System.out.println(Integer.toBinaryString(hand & 0b1111_1111_1111_1111_1111_0000_0000_0000));
    for (int i = 0; i < 5; i++) {
      var card = (hand >>> (10 + i * 4)) & 0b1111;
//      System.out.println(Integer.toBinaryString(card));
      tally[card]++;
      tallyMask |= 1 << card;
    }

    var tallySize = Integer.bitCount(tallyMask);
//    System.out.println(tallySize);
//    System.out.println(Arrays.toString(tally));
    if (tallySize == 1) return 7;
    if (tallySize == 4) return 2;
    if (tallySize == 5) return 1;

    var maxCount = 0;
    for (int i = 0; i < tallySize; i++) {
      var lowestBit = Integer.numberOfTrailingZeros(tallyMask) & 0xF;
      maxCount = Math.max(maxCount, tally[lowestBit]);
      tallyMask &= ~(1 << lowestBit);
    }

    return maxCount + (4 - tallySize);

//    if (tallySize == 2) {
//      // 4 or 3
//      return maxCount == 4 ? 6 : 5;
//    }
//
//    if (tallySize == 3) {
//      // 3 or 2
//      return maxCount == 3 ? 4 : 3;
//    }
//
//    throw new IllegalArgumentException();
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
