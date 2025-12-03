package dev.linl33.adventofcode.year2025;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;

public class Day3 extends AdventSolution2025<Long, Long> {
  static void main() {
    new Day3().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    return calculateTotalJoltage(lines, 2);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    return calculateTotalJoltage(lines, 12);
  }

  private static long calculateTotalJoltage(final CharSequence[] batteryBanks, final int batteries) {
    final var batteryBankWidth = batteryBanks[0].length();

    final var alignedBitsetWidth = getAlignedBitsetWidth(batteryBankWidth);
    final var rangeMask = new long[alignedBitsetWidth];
    final var rangeMaskCopy = new long[alignedBitsetWidth];
    final var endPos = batteryBankWidth - batteries;
    // range mask presents the batteries in the battery bank eligible for selection
    buildInitialRangeMask(rangeMask, endPos);

    final var batteryBankBitsets = new long[9 * rangeMask.length];

    var sum = 0L;
    for (var i = 0; i < batteryBanks.length; i++) {
      System.arraycopy(rangeMask, 0, rangeMaskCopy, 0, rangeMask.length);

      final var batteryBank = batteryBanks[i];
      batteryBankToBitsets(batteryBankBitsets, batteryBank, rangeMask);

      sum += calculateJoltage(batteryBankBitsets, batteryBank, batteries, rangeMaskCopy);
    }

    return sum;
  }

  // greedily pick the batteries with the highest joltage rating, from left-to-right
  private static long calculateJoltage(final long[] batteryBankBitsets, final CharSequence batteryBank, final int batteries, final long[] rangeMask) {
    final var maxJoltage = getMaxSingleBatteryJoltage(batteryBankBitsets, rangeMask);
    var selectedBatteries = 0L;

    batteryLoop:
    for (int i = 0; i < batteries; i++) {
      for (int j = maxJoltage; j < 9; j++) {
        for (var k = 0; k < rangeMask.length; k++) {
          // compiler doesn't realize length is a power of 2, must manually convert to bitshift
          final var shift = Integer.numberOfTrailingZeros(rangeMask.length);
          final var masked = rangeMask[k] & batteryBankBitsets[(j << shift) + k];
          if (masked == 0) {
            continue;
          }

          final var max = 9L - j;
          selectedBatteries = (selectedBatteries << 4) | max;
          final var lowestOne = Long.numberOfTrailingZeros(masked);
          final var maxIdx = lowestOne + k * Long.SIZE;
          updateRangeMask(rangeMask, maxIdx + 1, batteryBank.length() - batteries + i + 1);

          continue batteryLoop;
        }
      }
    }

    return batteriesToJoltage(batteries, selectedBatteries << (Long.SIZE - 4 * batteries));
  }

  private static long batteriesToJoltage(final int batteries, long selectedBatteries) {
    var bankJoltage = 0L;
    for (var i = 0; i < batteries; i++) {
      selectedBatteries = Long.rotateLeft(selectedBatteries, 4);
      final var digit = selectedBatteries & 0b1111;
      bankJoltage = bankJoltage * 10L + digit;
    }
    return bankJoltage;
  }

  private static int getAlignedBitsetWidth(final int batteryBankWidth) {
    final var bitsetWidth = Math.ceilDiv(batteryBankWidth, Long.SIZE);
    return 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(bitsetWidth - 1));
  }

  private static void buildInitialRangeMask(final long[] rangeMask, final int endPos) {
    final var endIdx = endPos / Long.SIZE;
    Arrays.fill(rangeMask, 0, endIdx, -1L);

    final var endBit = endPos % Long.SIZE;
    rangeMask[endIdx] = -1L >>> -(endBit + 1);
  }

  private static void updateRangeMask(final long[] rangeMask, final int startIdx, final int endIdx) {
    // unset everything before startIdx
    final var sIdx = startIdx / Long.SIZE;
    Arrays.fill(rangeMask, 0, sIdx, 0L);
    final var sBit = startIdx % Long.SIZE;
    rangeMask[sIdx] &= -(1L << sBit);

    // extend mask to the new endIdx
    final var e2Idx = endIdx / Long.SIZE;
    final var e2Bit = endIdx % Long.SIZE;
    rangeMask[e2Idx] |= 1L << e2Bit;
  }

  private static void batteryBankToBitsets(final long[] batteryBankBitsets, final CharSequence batteryBank, final long[] rangeMask) {
    Arrays.fill(batteryBankBitsets, 0L);
    for (var j = 0; j < batteryBank.length(); j++) {
      final int joltage = 9 - (batteryBank.charAt(j) - '0');
      final var bitsetIdx = j / Long.SIZE;
      final var bitsetBit = j % Long.SIZE;
      // compiler doesn't realize length is a power of 2, must manually convert to bitshift
      final var shift = Integer.numberOfTrailingZeros(rangeMask.length);
      batteryBankBitsets[(joltage << shift) + bitsetIdx] |= 1L << bitsetBit;
    }
  }

  private static int getMaxSingleBatteryJoltage(final long[] batteryBankBitsets, final long[] rangeMask) {
    for (int i = 0; i < batteryBankBitsets.length; i++) {
      if (batteryBankBitsets[i] != 0) {
        // compiler doesn't realize length is a power of 2, must manually convert to bitshift
        final var shift = Integer.numberOfTrailingZeros(rangeMask.length);
        return i >> shift;
      }
    }

    // should not happen
    return 9;
  }
}
