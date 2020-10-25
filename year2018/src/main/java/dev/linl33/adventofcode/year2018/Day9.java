package dev.linl33.adventofcode.year2018;

import dev.linl33.adventofcode.lib.AdventLinkedListNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class Day9 extends AdventSolution2018<Long, Long> {
  public static void main(String[] args) {
    new Day9().runAndPrintAll();
  }

  @Override
  public Long part1(BufferedReader reader) throws IOException {
    String[] segments = reader.readLine().split(" ");

    var players = Integer.parseInt(segments[0]);
    var lastMarble = Integer.parseInt(segments[6]);

    return scoreGame(players, lastMarble);
  }

  @Override
  public Long part2(BufferedReader reader) throws IOException {
    String[] segments = reader.readLine().split(" ");

    var players = Integer.parseInt(segments[0]);
    var lastMarble = Integer.parseInt(segments[6]) * 100;

    return scoreGame(players, lastMarble);
  }

  public static long scoreGame(int players, int lastMarble) {
    var currMarble = new AdventLinkedListNode<>(0);
    var currPlayer = -1;
    var score = new long[players];

    for (int i = 1; i <= lastMarble; i++) {
      currPlayer = (currPlayer + 1) % players;

      if (i % 23 == 0) {
        var toRemove = currMarble.nextN(-7);
        currMarble = toRemove.getNext();
        toRemove.unlink();

        score[currPlayer] = score[currPlayer] + i + toRemove.getValue();
      } else {
        currMarble = currMarble.getNext().linkAfter(new AdventLinkedListNode<>(i));
      }
    }

    return Arrays.stream(score).max().orElseThrow();
  }
}
