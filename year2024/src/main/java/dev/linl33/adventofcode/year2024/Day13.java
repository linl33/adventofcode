package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import dev.linl33.adventofcode.lib.util.VectorIoUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class Day13 extends AdventSolution2024<Long, Long> implements ByteBufferAdventSolution<Long, Long>, NullBufferedReaderSolution<Long, Long> {
  public static void main() {
    new Day13().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part1(identifier);
  }

  @Override
  public Long part2(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part2(identifier);
  }

  @Override
  public Long part1(@NotNull ByteBuffer byteBuffer) {
    return totalTokens(byteBuffer, 0);
  }

  @Override
  public Long part2(@NotNull ByteBuffer byteBuffer) {
    return totalTokens(byteBuffer, 10000000000000L);
  }

  private static long totalTokens(ByteBuffer byteBuffer, long prizeOffset) {
    var counters = new long[3];
    var btnParams = new long[1];

    VectorIoUtil.readLines(byteBuffer, line -> {
      var lineNum = counters[0]++;
      var lineType = lineNum & 0b11;
      if (lineType == 0) {
        btnParams[0] = (long) line.charAt(13) << 56 | (long) line.charAt(12) << 48 | (long) line.charAt(19) << 40 | (long) line.charAt(18) << 32;
      } else if (lineType == 1) {
        // parse all 4 button parameters simultaneously with SWAR
        btnParams[0] = ((btnParams[0] | ((long) line.charAt(13) << 24 | (long) line.charAt(12) << 16 | (long) line.charAt(19) << 8 | (long) line.charAt(18))) - 0x3030303030303030L) * 0x0a01;
      } else if (lineType == 2) {
        var prizeSep = charSequenceIndexOf(line, ',', 12);
        var xPrize = Long.parseLong(line, 9, prizeSep, 10) + prizeOffset;
        var yPrize = Long.parseLong(line, prizeSep + 4, line.length(), 10) + prizeOffset;

        /*
         * let t_a be the number of times to press button A
         * let t_b be the number of times to press button B
         * let x_{a, b, p} be the given x parameter for button A, B, and the prize, respectively
         * let y_{a, b, p} be the given y parameter for button A, B, and the prize, respectively
         *
         * Solve the linear system
         *
         * x_a * t_a + x_b * t_b = x_p
         * y_a * t_a + y_b * t_b = y_p
         *
         * (assume that the equations are independent)
         */

        var xA = btnParams[0] >>> 56;
        var yA = (btnParams[0] >>> 40) & 0xff;

        var xB = (btnParams[0] >>> 24) & 0xff;
        var yB = (btnParams[0] >>> 8) & 0xff;

        var lhs = yA * xB - xA * yB;
        var rhs = yPrize * xB - xPrize * yB;

        if (rhs % lhs == 0) {
          var aCount = rhs / lhs;
          if (aCount < 0) {
            // note that the check is unnecessary, and it is safe to assume the value is positive
            return;
          }

          var prizeXRemaining = xPrize - xA * aCount;
          if (prizeXRemaining % xB == 0) {
            var bCount = prizeXRemaining / xB;
            if (bCount < 0) {
              // note that the check is unnecessary, and it is safe to assume the value is positive
              return;
            }

            counters[1] += aCount;
            counters[2] += bCount;
          }
        }
      }
    });

    return counters[1] * 3 + counters[2];
  }

  private static int charSequenceIndexOf(CharSequence cs, char ch, int fromIndex) {
    for (int i = fromIndex; i < cs.length(); i++) {
      if (cs.charAt(i) == ch) {
        return i;
      }
    }

    return -1;
  }
}
