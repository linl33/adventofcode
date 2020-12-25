package dev.linl33.adventofcode.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;

public class Day25 extends AdventSolution2020<Integer, Void> {
  private static final long KF_MOD = 20201227L;
  private static final long PUB_KEY_SUB = 7L;
  private static final long START_SEARCH = 2_000_001L;

  public static void main(String[] args) {
    new Day25().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws IOException {
    var cardPub = Long.parseLong(reader.readLine());
    var doorPub = Long.parseLong(reader.readLine());

    // use modpow to kickstart the search, this significantly speeds up the search
    var val = BigInteger
        .valueOf(PUB_KEY_SUB)
        .modPow(BigInteger.valueOf(START_SEARCH), BigInteger.valueOf(KF_MOD))
        .longValue();
    var rounds = START_SEARCH;

    for (; val != cardPub && val != doorPub; rounds++) {
      val = (val * PUB_KEY_SUB) % KF_MOD;
    }

    return BigInteger
        .valueOf(val ^ cardPub ^ doorPub)
        .modPow(BigInteger.valueOf(rounds), BigInteger.valueOf(KF_MOD))
        .intValue();
  }

  @Override
  public Void part2(BufferedReader reader) throws Exception {
    throw new UnsupportedOperationException();
  }
}
