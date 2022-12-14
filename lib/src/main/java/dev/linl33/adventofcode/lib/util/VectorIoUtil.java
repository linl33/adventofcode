package dev.linl33.adventofcode.lib.util;

import dev.linl33.adventofcode.lib.ByteBufferAsCharSequence;
import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.jetbrains.annotations.NotNull;

import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

public final class VectorIoUtil {
  private static final VectorSpecies<Byte> BYTE_SPECIES = ByteVector.SPECIES_MAX;

  public static void readLines(@NotNull ByteBuffer byteBuffer, @NotNull Consumer<CharSequence> consumer) {
    var charSequence = new ByteBufferAsCharSequence(byteBuffer);

    var lineStart = byteBuffer.position();
    var lineEnd = byteBuffer.position();
    var limit = byteBuffer.limit();

    var memSegment = MemorySegment.ofBuffer(byteBuffer);
    var newlineVector = ByteVector.broadcast(BYTE_SPECIES, '\n');

    for (int i = byteBuffer.position(); i < limit; i += BYTE_SPECIES.length()) {
      var mask = BYTE_SPECIES.indexInRange(i, limit);
      var vector = ByteVector.fromMemorySegment(BYTE_SPECIES, memSegment, i, byteBuffer.order(), mask);

      var newlines = vector.compare(VectorOperators.EQ, newlineVector).toLong();
      var newlineCount = Long.bitCount(newlines);
      if (newlineCount != 0) {
        var last = Long.SIZE - Long.numberOfLeadingZeros(newlines);
        for (int j = Long.numberOfTrailingZeros(newlines); j < last; j++) {
          if ((newlines & (1L << j)) != 0) {
            lineEnd = i + j;

            charSequence.buffer().limit(lineEnd).position(lineStart);
            consumer.accept(charSequence);

            lineStart = lineEnd + 1;
          }
        }
      }
    }
  }
}
