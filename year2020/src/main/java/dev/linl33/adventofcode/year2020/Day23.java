package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.AdventLinkedListNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class Day23 extends AdventSolution2020<Integer, Long> {
  public static void main(String[] args) {
    new Day23().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws IOException {
    var list = solveArray(reader, 9, 100);

    var sum = 0;
    var curr = 1;

    while ((curr = list[curr]) != 1) {
      sum = sum * 10 + curr;
    }

    return sum;
  }

  public int part1ByLinkedList(BufferedReader reader) throws IOException {
    var table = simulate(reader, 9, 100);
    return Integer.parseInt(table[1].toString().replace(" ", "").substring(1));
  }

  @Override
  public Long part2(BufferedReader reader) throws IOException {
    var list = solveArray(reader, 1_000_000, 10_000_000);
    return ((long) list[1]) * ((long) list[list[1]]);
  }

  public long part2ByLinkedList(BufferedReader reader) throws IOException {
    var table = simulate(reader, 1_000_000, 10_000_000);

    var next = table[1].getNext();
    var nextNext = table[1].getNext().getNext();

    return ((long) next.getValue()) * ((long) nextNext.getValue());
  }

  private static AdventLinkedListNode<Integer>[] simulate(BufferedReader reader,
                                                          int cups,
                                                          int rounds) throws IOException {
    var seed = Arrays
        .stream(reader.readLine().split(""))
        .mapToInt(Integer::parseInt)
        .toArray();

    @SuppressWarnings("unchecked")
    var table = (AdventLinkedListNode<Integer>[]) new AdventLinkedListNode[cups + 1];
    var curr = initialize(seed, cups, table);

    for (int i = 0; i < rounds; i++) {
      var currVal = curr.getValue();

      var next = removeThree(curr);
      int nextVal = next.getValue();
      int next1Val = next.getNext().getValue();
      int next2Val = next.getNext().getNext().getValue();

      var destNode = findDestNode(table, currVal, nextVal, next1Val, next2Val);
      insertThree(destNode, next);

      curr = curr.getNext();
    }

    return table;
  }

  private static AdventLinkedListNode<Integer> initialize(int[] seed,
                                                          int cups,
                                                          AdventLinkedListNode<Integer>[] table) {
    var ll = new AdventLinkedListNode<>(seed[0]);
    table[seed[0]] = ll;
    var root = ll;

    for (int i = 1; i < seed.length; i++) {
      var nextNode = new AdventLinkedListNode<>(seed[i]);
      ll = ll.linkAfter(nextNode);

      table[seed[i]] = nextNode;
    }

    if (seed.length < cups) {
      for (int i = seed.length + 1; i <= cups; i++) {
        var nextNode = new AdventLinkedListNode<>(i);
        ll = ll.linkAfter(nextNode);

        table[i] = nextNode;
      }
    }

    return root;
  }

  private static AdventLinkedListNode<Integer> findDestNode(AdventLinkedListNode<Integer>[] table,
                                                            int currVal,
                                                            int nextVal,
                                                            int next1Val,
                                                            int next2Val) {
    var destVal = currVal - 1;

    while (destVal < 1 || (destVal == nextVal || destVal == next1Val || destVal == next2Val)) {
      if (destVal < 1) {
        destVal = table.length - 1;
      } else {
        destVal--;
      }
    }

    return table[destVal];
  }

  private static AdventLinkedListNode<Integer> removeThree(AdventLinkedListNode<Integer> curr) {
    var next = curr.getNext();
    var next2 = next.getNext().getNext();

    curr.setNext(next2.getNext());
    next2.getNext().setPrev(curr);

    return next;
  }

  private static void insertThree(AdventLinkedListNode<Integer> destNode,
                                  AdventLinkedListNode<Integer> toInsert) {
    var tmp = destNode.getNext();
    destNode.setNext(toInsert);
    toInsert.setPrev(destNode);

    var next2 = toInsert.getNext().getNext();
    tmp.setPrev(next2);
    next2.setNext(tmp);
  }

  private static int[] solveArray(BufferedReader reader, int cups, int rounds) throws IOException {
    // this implementation uses an int[] like a linked list
    // 34251 is translated to [0, 3, 5, 4, 2, 1]

    var seed = Arrays
        .stream(reader.readLine().split(""))
        .mapToInt(Integer::parseInt)
        .toArray();

    var list = new int[cups + 1];
    var curr = seed[0];
    for (int i = 1; i < seed.length; i++) {
      list[curr] = (curr = seed[i]);
    }

    for (int i = seed.length + 1; i <= cups; i++) {
      list[curr] = (curr = i);
    }

    // link the last node to the start
    list[curr] = (curr = seed[0]);

    for (int n = 0; n < rounds; n++) {
      var next = list[curr];
      var next2 = list[list[list[curr]]];

      var dest = curr - 1;
      while (dest < 1 || (dest == next || dest == next2 || dest == list[list[curr]])) {
        if (dest < 1) {
          dest = cups;
        } else {
          dest--;
        }
      }

      // unlink the 3 numbers
      // link curr.next to next2.next
      list[curr] = list[next2];

      // link the 3 numbers to dest
      // link next2.next to dest.next
      list[next2] = list[dest];
      // link dest.next to next;
      list[dest] = next;

      // advance curr
      curr = list[curr];
    }

    return list;
  }
}
