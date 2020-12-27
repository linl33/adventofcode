package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;

import java.nio.ByteBuffer;

public class Day18 extends AdventSolution2020<Long, Long> implements ByteBufferAdventSolution<Long, Long>, NullBufferedReaderSolution<Long, Long> {
  private static final byte ADD = '+';
  private static final byte MUL = '*';
  private static final byte PAREN_START = '(';
  private static final byte PAREN_END = ')';
  private static final int ALLOC_SIZE = 1 << 7;
  private static final long ZERO = '0';

  public static void main(String[] args) {
    new Day18().runAndPrintAll();
  }

  @Override
  public Long part1(ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part1(identifier);
  }

  @Override
  public Long part2(ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part2(identifier);
  }

  @Override
  public Long part1(ByteBuffer byteBuffer) throws Exception {
    return eval(byteBuffer, false);
  }

  @Override
  public Long part2(ByteBuffer byteBuffer) throws Exception {
    return eval(byteBuffer, true);
  }

  private static long eval(ByteBuffer byteBuffer, boolean opPrecedence) {
    var combinedDeque = new long[ALLOC_SIZE];
    var sum = 0L;

    while (byteBuffer.hasRemaining()) {
      // fill output from the end, so ops can be copied with arraycopy
      // and they can share 1 array
      var outHead = combinedDeque.length;
      var opStackHead = -1;

      // converts the expression to rpn using shunting-yard
      // https://en.wikipedia.org/wiki/Shunting-yard_algorithm

      byte element;
      while ((element = byteBuffer.get()) != '\n') {
        switch (element) {
          case ' ' -> {}
          case PAREN_START -> combinedDeque[++opStackHead] = element;
          case PAREN_END -> {
            while (combinedDeque[opStackHead] != PAREN_START) {
              combinedDeque[--outHead] = combinedDeque[opStackHead--];
            }

            opStackHead--;
          }
          case ADD, MUL -> {
            while (opStackHead >= 0 &&
                (!opPrecedence || combinedDeque[opStackHead] == ADD) &&
                combinedDeque[opStackHead] != PAREN_START) {
              combinedDeque[--outHead] = combinedDeque[opStackHead--];
            }

            combinedDeque[++opStackHead] = element;
          }
          default -> combinedDeque[--outHead] = element;
        }
      }

      System.arraycopy(combinedDeque, 0, combinedDeque, outHead - (opStackHead + 1), opStackHead + 1);
      sum += evalRpn(combinedDeque, outHead - (opStackHead + 1));
    }

    return sum;
  }

  private static long evalRpn(long[] rpn, int size) {
    // uses the operator stack part of rpn to store evaluation result

    var head = -1;

    for (int rpnQueueHead = rpn.length - 1; rpnQueueHead >= size; rpnQueueHead--) {
      switch ((int) rpn[rpnQueueHead]) {
        case ADD -> rpn[--head] += rpn[head + 1];
        case MUL -> rpn[--head] *= rpn[head + 1];
        default -> rpn[++head] = rpn[rpnQueueHead] - ZERO;
      }
    }

    return rpn[0];
  }
}
