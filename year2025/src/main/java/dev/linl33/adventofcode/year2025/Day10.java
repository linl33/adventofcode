package dev.linl33.adventofcode.year2025;

import dev.linl33.adventofcode.jmh.JmhBenchmarkOption;
import jdk.incubator.vector.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.IntStream;

public class Day10 extends AdventSolution2025<Integer, Integer> {
  private static final VectorSpecies<Integer> INT_SPECIES = IntVector.SPECIES_MAX;
  private static final VectorSpecies<Short> SHORT_SPECIES = ShortVector.SPECIES_256;

  private static final List<int[]> PATTERNS = List.ofLazy(16, (final int i) -> (
    IntStream
      .range(0, 1 << i)
      .boxed()
      .sorted(Comparator.comparingInt(Integer::bitCount))
      .mapToInt(Integer::intValue)
      .toArray()
  ));

//  private static final short[] EMPTY_SHORT_ARR = new short[0];

  static void main() {
//    new Day10().runAndPrintAll();

//    new Day10().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day10test1"));
//    new Day10().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day10"));
//    new Day10().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day10test1"));
//    new Day10().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day10test2"));
//    new Day10().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day10"));

//    new Day10().benchmark(JmhBenchmarkOption.PART_1);
    new Day10().benchmark(JmhBenchmarkOption.PART_2, JmhBenchmarkOption.PERF_PROFILE);
//    new Day10().benchmark(JmhBenchmarkOption.PART_2, JmhBenchmarkOption.PROFILE);
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    final var lines = reader.lines().toArray(String[]::new);

    var total = 0;
    for (var i = 0; i < lines.length; i++) {
      final var line = lines[i];
      final var parts = line.split(" ");

      final var desiredStateStr = parts[0];
      var desiredState = 0;
      for (var j = 1; j < desiredStateStr.length() - 1; j++) {
        var c = desiredStateStr.charAt(j);
        var state = c == '#' ? 1 : 0;
        desiredState |= state << (j - 1);
      }

      final var buttonWiring = new int[parts.length - 2];
      for (var j = 1; j < parts.length - 1; j++) {
        final var btnStr = parts[j];
        var btn = 0;

        for (var k = 1; k < btnStr.length(); k += 2) {
          btn |= 1 << (btnStr.charAt(k) - '0');
        }

        buttonWiring[j - 1] = btn;
      }

      for (var j = 1; j < (1 << buttonWiring.length); j++) {
        final var pattern = PATTERNS.get(buttonWiring.length)[j];

        var result = desiredState;
        for (var btn = 0; btn < buttonWiring.length; btn++) {
          result ^= (-(pattern & (1 << btn)) >> 31) & buttonWiring[btn];
        }

        if (result == 0) {
          total += Integer.bitCount(pattern);
          break;
        }
      }
    }

    return total;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    // adapted from https://www.reddit.com/r/adventofcode/comments/1pk87hl/2025_day_10_part_2_bifurcate_your_way_to_victory/

    final var lines = reader.lines().toArray(String[]::new);

    var total = 0;
    final var cache = new HashMap<String, Integer>();

    final var combination = new short[1 << 16];
    final var oddArr = new short[1 << 16];
    final var sumArr = new short[(1 << 16) * 16];
    final var metadata = new short[(1 << 10) + 1];
    final var keyTmp = new char[SHORT_SPECIES.length()];
    final var btnWiringMasks = new short[SHORT_SPECIES.length()];
    final var seen = new boolean[1 << 10];

    for (var i = 0; i < lines.length; i++) {
      final var line = lines[i];
      final var parts = line.split(" ");

      final var desiredStateStr = parts[parts.length - 1].substring(1, parts[parts.length - 1].length() - 1).split(",");
//      final var desiredState = new short[16];
      for (var j = 0; j < desiredStateStr.length; j++) {
        keyTmp[j] = (char) Integer.parseInt(desiredStateStr[j]);
      }
      Arrays.fill(keyTmp, desiredStateStr.length, SHORT_SPECIES.length(), '\0');

//      final var btnWiring = new int[parts.length - 2][];
//      var btnWiringMasks = new short[parts.length - 2];

      for (var j = 1; j < parts.length - 1; j++) {
        final var btnStr = parts[j];
        var btnMask = 0;

        for (var k = 1; k < btnStr.length(); k += 2) {
          final var b = btnStr.charAt(k) - '0';
          btnMask |= 1 << b;
        }

        btnWiringMasks[j - 1] = (short) btnMask;
      }
      // TODO: mask longSum instead
      Arrays.fill(btnWiringMasks, parts.length - 2, SHORT_SPECIES.length(), (short) 0);

      var longSum = 0L;
      for (var j = 0; j < 16; j++) {
        final var longMask = Long.expand(btnWiringMasks[j], 0x11_11_11_11_11L);
        longSum += longMask;
      }

      var removable = (short) Long.compress(longSum & (~longSum >>> 3) & (~longSum >>> 2) & (~longSum >>> 1), 0x11_11_11_11_11L);

      var btnWiringCount = parts.length - 2;
      while (removable != 0 && btnWiringCount > 0) {
        var btnWiringCountNext = 0;

        for (var j = 0; j < btnWiringCount; j++) {
          final var btnRemovable = btnWiringMasks[j] & removable;
          if (btnRemovable != 0) {
            final var idx = Integer.numberOfTrailingZeros(btnRemovable);
            final var toRemove = keyTmp[idx];
            total += toRemove;

            longSum -= Long.expand(btnWiringMasks[j], 0x11_11_11_11_11L);
            for (var k = 0; k < 10; k++) {
              keyTmp[k] -= (-((btnWiringMasks[j] >> k) & 1) >> 31) & toRemove;
            }
          } else {
            btnWiringMasks[btnWiringCountNext++] = btnWiringMasks[j];
          }
        }

        removable = (short) Long.compress(longSum & (~longSum >>> 3) & (~longSum >>> 2) & (~longSum >>> 1), 0x11_11_11_11_11L);
        btnWiringCount = btnWiringCountNext;
      }

      if (btnWiringCount == 0) {
        continue;
      }

      cache.clear();
      Arrays.fill(btnWiringMasks, btnWiringCount, SHORT_SPECIES.length(), (short) 0);
      generateCombinations(btnWiringCount, btnWiringMasks, combination);
//      genCombinations2(btnWiringCount, btnWiringMasks, combination);

      final var stateLength = parts[0].length() - 2;
      Arrays.fill(metadata, 0, 1 << stateLength, (short) 0);
      generateOddMaskToPatterns(combination, metadata, oddArr, btnWiringCount, stateLength);
      metadata[(1 << stateLength)] = (short) (1 << btnWiringCount);

      Arrays.fill(seen, 0, 1 << stateLength, false);
      total += countBtnPresses(ShortVector.fromCharArray(SHORT_SPECIES, keyTmp, 0), keyTmp, btnWiringMasks, cache, metadata, oddArr, sumArr, seen);
    }

    return total;
  }

  private static int countBtnPresses(
    final ShortVector vDesiredState,
    final char[] keyTmp,
    final short[] btnWiringMasks,
    final Map<String, Integer> cache,
    final short[] metadata,
    final short[] oddArr,
    final short[] allBtnCombinationsSum,
    final boolean[] seen
  ) {
    vDesiredState.intoCharArray(keyTmp, 0);
    final var key = new String(keyTmp);

//    final var key = Arrays.toString(desiredState);
//    final var key = new StateHolder(desiredState);
    if (cache.containsKey(key)) {
      return cache.get(key);
    }

    final int oddMask = (short) ~vDesiredState.and((short) 1).eq(SHORT_SPECIES.zero()).toLong();

    final int sIdx = metadata[oddMask];
    final int eIdx = metadata[oddMask + 1];

    if (!seen[oddMask]) {
      seen[oddMask] = true;

      for (var i = sIdx; i < eIdx; i++) {
        final int pattern = oddArr[i];
        sum2(pattern, btnWiringMasks, allBtnCombinationsSum);
      }
    }

    var min = 0xffff;
    for (var i = sIdx; i < eIdx; i++) {
      final int pattern = oddArr[i];

      // TODO: ??
      if (Integer.bitCount(pattern) > min) {
        continue;
//        break;
      }

//      short s = 0;
//      for (var j = 0; j < desiredState.length; j++) {
//        s |= (desiredState[j] = (short) ((desiredState[j] - allBtnCombinationsSum[pattern * 16 + j]) / 2));
//      }
      final var vResult = (
        vDesiredState
          .sub(ShortVector.fromArray(SHORT_SPECIES, allBtnCombinationsSum, pattern * 16))
          .lanewise(VectorOperators.ASHR, (short) 1)
      );
//      vResult.intoCharArray(tmp, 0);

      final var count = switch (Integer.signum(vResult.reduceLanes(VectorOperators.OR))) {
        case -1 -> 0xffff;
        case 0 -> 0;
        default -> countBtnPresses(vResult, keyTmp, btnWiringMasks, cache, metadata, oddArr, allBtnCombinationsSum, seen);
      };

//      if (sgn == 0) {
//        min = Math.min(Integer.bitCount(pattern), min);
//      }

//      for (var j = 0; j < desiredState.length; j++) {
//        desiredState[j] = (short) (desiredState[j] * 2 + allBtnCombinationsSum[pattern * 16 + j]);
//      }

//      ShortVector.fromArray(SHORT_SPECIES, desiredState, 0)
//        .lanewise(VectorOperators.LSHL, SHORT_ONE)
//        .add(ShortVector.fromArray(SHORT_SPECIES, allBtnCombinationsSum, pattern * 16))
//        .intoArray(desiredState, 0);

      min = Math.min(2 * count + Integer.bitCount(pattern), min);
    }

    cache.put(key, min);
    return min;
  }

//  private static final Vector<Short> SHORT_IOTA = VectorShuffle.iota(SHORT_SPECIES, 0, 1, false).toVector();
//  private static final ShortVector SHORT_ONE = ShortVector.broadcast(SHORT_SPECIES, (short) 1);
//  private static final ShortVector ONE = SHORT_ONE.lanewise(VectorOperators.LSHL, SHORT_IOTA);

  private static void generateOddMaskToPatterns(final short[] allBtnCombinations, final short[] metadata, final short[] oddArr, final int btnWiringCount, final int stateLength) {
    for (var i = 0; i < (1 << btnWiringCount); i++) {
      metadata[allBtnCombinations[i]]++;
    }

    for (var i = 1; i < (1 << stateLength); i++) {
      metadata[i] += metadata[i - 1];
    }

    for (short i = 0; i < (1 << btnWiringCount); i++) {
      final var xor = allBtnCombinations[i];
      final var idx = --metadata[xor];
      oddArr[idx] = i;
    }
  }

  private static void generateCombinations(final int btnWiringCount, final short[] btnWiringMasks, final short[] allBtnCombinations) {
    final var vBtnWiringMasks = ShortVector.fromArray(SHORT_SPECIES, btnWiringMasks, 0);
//    final var ints = PATTERNS.get(Integer.numberOfTrailingZeros(allBtnCombinations.length));

    final var one = ShortVector.broadcast(SHORT_SPECIES, (short) 1).lanewise(VectorOperators.LSHL, VectorShuffle.iota(SHORT_SPECIES, 0, 1, false).toVector());

//    final var vI = ShortVector.broadcast(SHORT_SPECIES, (short) (~0));

    for (short i = ~0; i > ~(1 << btnWiringCount); i--) {
//      final var pattern = ints[i];

      // convert i to a VectorMask
//      final var vPattern = one.and(vI.sub(i)).compare(VectorOperators.EQ, SHORT_SPECIES.zero());
      final var vPattern = one.and(ShortVector.broadcast(SHORT_SPECIES, i)).eq(SHORT_SPECIES.zero());
//      final var vPattern = ONE.and(ShortVector.broadcast(SHORT_SPECIES, (short) ~i)).compare(VectorOperators.EQ, SHORT_SPECIES.zero());
//      final var vPattern = ONE.and(ShortVector.broadcast(SHORT_SPECIES, ~pattern)).compare(VectorOperators.EQ, SHORT_SPECIES.zero());
//      final var vPattern = ONE.and(ShortVector.broadcast(SHORT_SPECIES, (short) ~i)).test(VectorOperators.IS_DEFAULT);
//      final var vPattern = VectorMask.fromLong(SHORT_SPECIES, i);
//      vI = vI.sub(SHORT_ONE);

      allBtnCombinations[~i] = vBtnWiringMasks.reduceLanes(VectorOperators.XOR, vPattern);
    }
  }

  private static void genCombinations2(final int btnWiringCount, final short[] btnWiringMasks, final short[] allBtnCombinations) {
    for (int i = 0; i < (1 << btnWiringCount); i++) {
      var m = 0;

      for (var j = 0; j < 16; j++) {
        m ^= (-((i >> j) & 1) >> 31) & btnWiringMasks[j];
      }

      allBtnCombinations[i] = (short) m;
    }
  }

  private static void sumBtnMasks(final int pattern, final short[] btnWiringMasks, final short[] sums) {
    final var one = ShortVector.broadcast(SHORT_SPECIES, (short) 1).lanewise(VectorOperators.LSHL, VectorShuffle.iota(SHORT_SPECIES, 0, 1, false).toVector());

    final var vPattern = one.and(ShortVector.broadcast(SHORT_SPECIES, (short) pattern)).compare(VectorOperators.EQ, SHORT_SPECIES.zero());
    ShortVector.fromArray(SHORT_SPECIES, btnWiringMasks, 0).blend(SHORT_SPECIES.zero(), vPattern).intoArray(sums, pattern * 16);
//    ShortVector.fromArray(SHORT_SPECIES, btnWiringMasks, 0).lanewise(VectorOperators.AND, ShortVector.zero(SHORT_SPECIES), vPattern).intoArray(sums, pattern * 16);
//    ((ShortVector) SHORT_SPECIES.zero()).intoArray(sums, pattern * 16);
//    ShortVector.fromArray(SHORT_SPECIES, btnWiringMasks, 0).intoArray(sums, pattern * 16, vPattern.not());

    var s = ShortVector.zero(SHORT_SPECIES);
    for (var i = 0; i < 16; i++) {
      s = s.add((short) 1, one.and(ShortVector.broadcast(SHORT_SPECIES, sums[pattern * 16 + i])).compare(VectorOperators.EQ, SHORT_SPECIES.zero()).not());
    }
    s.intoArray(sums, pattern * 16);
  }

  private static void sum2(final int pattern, final short[] btnWiringMasks, final short[] sums) {
    var longSum = 0L;

    for (var i = 0; i < 16; i++) {
      final var shortMask = (-((pattern >> i) & 1) >> 31) & btnWiringMasks[i];
      final var longMask = Long.expand(shortMask, 0x11_11_11_11_11L);
      longSum += longMask;
    }

    for (var i = 0; i < 10; i++) {
      sums[pattern * 16 + i] = (short) ((longSum >> (i * 4)) & 0b1111);
    }
  }
}
