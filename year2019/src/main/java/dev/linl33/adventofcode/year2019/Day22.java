package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.util.PrintUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class Day22 extends AdventSolution2019<Integer, Long> {
  public static void main(String[] args) {
    new Day22().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    var deckSize = 10007L;
    var card = 2019L;

    var shuffle = reader
        .lines()
        .map(line -> line.transform(ShuffleTechnique::parse))
        .collect(Collectors.<ShuffleTechnique<?>>toUnmodifiableList());

    for (var s : shuffle) {
      card = s.performOnCard(card, deckSize);
    }

    return (int) card;
  }

  public int[] shuffleWholeDeck(BufferedReader reader, int deckSize) {
    var shuffle = reader
        .lines()
        .map(line -> line.transform(ShuffleTechnique::parse))
        .collect(Collectors.<ShuffleTechnique<?>>toUnmodifiableList());

    return shuffleDeck(deckSize, shuffle);
  }

  @Override
  public Long part2(BufferedReader reader) throws Exception {
    var deckSize = 119315717514047L;
    var card = 2020L;

    var shuffle = reader
        .lines()
        .map(line -> line.transform(ShuffleTechnique::parse))
        .collect(Collectors.<ShuffleTechnique<?>>toUnmodifiableList());

    var shuffleReversed = new ArrayList<ShuffleTechnique<?>>();
    for (int i = shuffle.size() - 1; i >= 0; i--) {
      shuffleReversed.add(shuffle.get(i).invert(deckSize));
    }

    var y = card;
    for (var s : shuffleReversed) {
      y = s.performOnCard(y, deckSize);
    }

    var z = y;
    for (var s : shuffleReversed) {
      z = s.performOnCard(z, deckSize);
    }

    var x = BigInteger.valueOf(2020);
    var yBigInt = BigInteger.valueOf(y);
    var zBigInt = BigInteger.valueOf(z);
    var deckSizeBigInt = BigInteger.valueOf(deckSize);
    var repeat = BigInteger.valueOf(101741582076661L);

    var a = yBigInt
        .subtract(zBigInt)
        .multiply(x
            .subtract(yBigInt)
            .modInverse(deckSizeBigInt)
        )
        .mod(deckSizeBigInt);

    var b = BigInteger
        .valueOf(y)
        .subtract(a.multiply(x))
        .mod(deckSizeBigInt);

    return a
        .modPow(repeat, deckSizeBigInt)
        .subtract(BigInteger.ONE)
        .multiply(a
            .subtract(BigInteger.ONE)
            .modInverse(deckSizeBigInt)
            .multiply(b)
        )
        .add(a
            .modPow(repeat, deckSizeBigInt)
            .multiply(x)
        )
        .mod(deckSizeBigInt)
        .longValue();
  }

  private static int[] shuffleDeck(int deckSize, Collection<ShuffleTechnique<?>> shuffle) {
    var deck = new int[deckSize];

    for (int i = 0; i < deck.length; i++) {
      var card = i;

      for (var s : shuffle) {
        card = (int) s.performOnCard(card, deckSize);
      }

      deck[card] = i;

      PrintUtil.enhancedPrint(deck);
    }

    return deck;
  }

  private sealed interface ShuffleTechnique<T extends ShuffleTechnique<T>> {
    static ShuffleTechnique<?> parse(@NotNull String input) {
      if (input.equals(NewStack.INPUT_STR)) {
        return NewStack.NEW_STACK;
      }

      if (input.startsWith(Increment.INPUT_STR)) {
        return new Increment(Integer.parseInt(input, 20, input.length(), 10));
      }

      if (input.startsWith(Cut.INPUT_STR)) {
        return new Cut(Integer.parseInt(input, 4, input.length(), 10));
      }

      throw new IllegalArgumentException();
    }

    long performOnCard(long cardPos, long stackSize);

    T invert(long stackSize);

    final class NewStack implements ShuffleTechnique<NewStack> {
      private static final String INPUT_STR = "deal into new stack";

      public static NewStack NEW_STACK = new NewStack();

      private NewStack() {}
      @Override
      public long performOnCard(long cardPos, long stackSize) {
        return stackSize - 1 - cardPos;
      }

      @Override
      public NewStack invert(long stackSize) {
        return this;
      }
    }

    sealed interface IIncrement extends ShuffleTechnique<IIncrement> {}

    final record Increment(long increment) implements IIncrement {
      private static final String INPUT_STR = "deal";

      @Override
      public long performOnCard(long cardPos, long stackSize) {
        return (cardPos * increment) % stackSize;
      }

      @Override
      public IIncrement invert(long stackSize) {
        return new IncrementInv(increment, stackSize);
      }
    }

    final record IncrementInv(BigInteger modInv, BigInteger stackSize) implements IIncrement {
      private IncrementInv(long increment, long stackSize) {
        this(BigInteger.valueOf(increment).modInverse(BigInteger.valueOf(stackSize)), BigInteger.valueOf(stackSize));
      }

      @Override
      public long performOnCard(long cardPos, long stackSize) {
        return modInv.multiply(BigInteger.valueOf(cardPos)).mod(this.stackSize).longValue();
      }

      @Override
      public IIncrement invert(long stackSize) {
        throw new UnsupportedOperationException();
      }
    }

    final record Cut(long position) implements ShuffleTechnique<Cut> {
      private static final String INPUT_STR = "cut";

      @Override
      public long performOnCard(long cardPos, long stackSize) {
        return Math.floorMod(cardPos - position, stackSize);
      }

      @Override
      public Cut invert(long stackSize) {
        return new Cut(-position);
      }
    }
  }
}
