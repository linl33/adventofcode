package dev.linl33.adventofcode.year2020;

import java.io.BufferedReader;

public class Day18 extends AdventSolution2020<Long, Long> {
  private static final char ADD = '+';
  private static final char MUL = '*';
  private static final char PAREN_START = '(';
  private static final char PAREN_END = ')';
  private static final int ALLOC_SIZE = 1 << 7;
  private static final long ZERO = '0';

  public static void main(String[] args) {
    new Day18().runAndPrintAll();
  }

  @Override
  public Long part1(BufferedReader reader) {
    var combinedDeque = new long[ALLOC_SIZE];

    return reader
        .lines()
        .mapToLong(exp -> eval(exp, false, combinedDeque))
        .sum();
  }

  @Override
  public Long part2(BufferedReader reader) {
    var combinedDeque = new long[ALLOC_SIZE];

    return reader
        .lines()
        .mapToLong(exp -> eval(exp, true, combinedDeque))
        .sum();
  }

  private static long eval(String exp, boolean opPrecedence, long[] combinedDeque) {
    // fill output from the end, so ops can be copied with arraycopy
    // and they can share 1 array
    var outHead = combinedDeque.length;
    var opStackHead = -1;

    // converts the expression to rpn using shunting-yard
    // https://en.wikipedia.org/wiki/Shunting-yard_algorithm

    for (char element : exp.toCharArray()) {
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

    return evalRpn(combinedDeque, outHead - (opStackHead + 1));
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
