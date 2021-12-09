package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class Day8 extends AdventSolution2021<Integer, Integer>
    implements ByteBufferAdventSolution<Integer, Integer>, NullBufferedReaderSolution<Integer, Integer> {
  private static final int UNAMBIGUOUS_MASK = 0b100111000;
  private static final int[] LENGTH_XOR_LOOKUP = new int[] { 4, 0, 1, 0, 0, 7, 9, 3, 0, 8, 0, 5, 0, 0, 6, 2 };

  public static void main(String[] args) {
    new Day8().runAndPrintAll();
  }

  @Override
  public Integer part1(ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part1(identifier);
  }

  @Override
  public Integer part2(ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part2(identifier);
  }

  @Override
  public Integer part1(@NotNull ByteBuffer byteBuffer) {
    var total = 0;
    while (byteBuffer.hasRemaining()) {
      var start = byteBuffer.position() + 61;
      byteBuffer.position(start);

      byte next;
      while ((next = byteBuffer.get()) != '\n') {
        if (next != ' ') {
          continue;
        }

        var segmentLength = byteBuffer.position() - start;
        if ((UNAMBIGUOUS_MASK & (1 << segmentLength)) != 0) {
          total++;
        }

        start = byteBuffer.position();
      }

      var segmentLength = byteBuffer.position() - start;
      if ((UNAMBIGUOUS_MASK & (1 << segmentLength)) != 0) {
        total++;
      }
    }

    return total;
  }

  @Override
  public Integer part2(@NotNull ByteBuffer byteBuffer) {
    var total = 0;

    while (byteBuffer.hasRemaining()) {
      var digitStart = byteBuffer.position();
      var lineStart = byteBuffer.position() + 61;
      // 6 xor 3 xor 5 = 0
      // 1 and 4 are all we need to figure out the rest of the digits
      var decoderDone = 6;
      var oneEncoded = 0;
      var fourEncoded = 0;
      do {
        if (byteBuffer.get() != ' ') {
          continue;
        }

        var digitLength = byteBuffer.position() - digitStart;
        if (digitLength == 3 || digitLength == 5) {
          var encoded = 0;
          var end = byteBuffer.position() - 1;
          for (int i = digitStart; i < end; i++) {
            encoded |= 1 << (byteBuffer.get(i) - 'a');
          }

          decoderDone ^= digitLength;
          oneEncoded ^= encoded;
          fourEncoded = encoded;
        }

        digitStart = byteBuffer.position();
      } while (decoderDone != 0);
      oneEncoded ^= fourEncoded;

      byteBuffer.position(lineStart);
      var encodedDigit = 0;
      var lineTotal = 0;
      byte next;
      while ((next = byteBuffer.get()) != '\n') {
        var wire = next - 'a';
        if (wire != (' ' - 'a')) {
          encodedDigit |= 1 << wire;
          continue;
        }

        lineTotal = 10 * lineTotal + decode(encodedDigit, oneEncoded, fourEncoded);
        encodedDigit = 0;
      }

      total += 10 * lineTotal + decode(encodedDigit, oneEncoded, fourEncoded);
    }

    return total;
  }

  private static int decode(int encodedDigit, int oneEncoded, int fourEncoded) {
    return LENGTH_XOR_LOOKUP[2 * Integer.bitCount(oneEncoded ^ encodedDigit) + 2 * Integer.bitCount(fourEncoded ^ encodedDigit) - Integer.bitCount(encodedDigit)];
  }
}
