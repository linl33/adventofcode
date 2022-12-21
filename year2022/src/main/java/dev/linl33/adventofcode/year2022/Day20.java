package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.AdventLinkedListNode;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day20 extends AdventSolution2022<Long, Long> {
  public static void main(String[] args) {
    new Day20().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    return decrypt(reader, 1, 1);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    return decrypt(reader, 811589153, 10);
  }

  private static long decrypt(@NotNull BufferedReader reader, int decryptionKey, int rounds) {
    var lines = reader.lines().toArray(String[]::new);
    var numberCount = lines.length;

    @SuppressWarnings("unchecked")
    var numbers = (AdventLinkedListNode<Long>[]) new AdventLinkedListNode<?>[numberCount];
    AdventLinkedListNode<Long> node = null;
    AdventLinkedListNode<Long> zero = null;

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      var num = Math.multiplyFull(Integer.parseInt(line), decryptionKey);

      var newNode = new AdventLinkedListNode<>(num);
      if (node != null) {
        node.linkAfter(newNode);
      }
      node = newNode;
      numbers[i] = node;

      if (num == 0) {
        zero = node;
      }
    }

    node.setNext(numbers[0]);
    numbers[0].setPrev(node);

    var countMinusOne = numberCount - 1;
    for (int round = 0; round < rounds; round++) {
      moveNodes(numbers, countMinusOne);
    }

    var sum = 0L;
    var curr = zero;
    for (int i = 0; i < 3; i++) {
      curr = curr.nextN(1000);
      sum += curr.getValue();
    }
    return sum;
  }

  private static void moveNodes(AdventLinkedListNode<Long>[] numbers, int countMinusOne) {
    for (var number : numbers) {
      long val = number.getValue();
      if (val == 0) {
        continue;
      }

      var prev = number.getPrev();
      var next = number.getNext();

      prev.setNext(next);
      next.setPrev(prev);

      var steps = Math.floorMod(val - 1, countMinusOne);
      var moveTo = next.nextN(steps);
      moveTo.linkAfter(number);
    }
  }
}
