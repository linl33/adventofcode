package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.util.AdventUtil;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Set;

public class Day22 extends AdventSolution2020<Integer, Integer> {
  private  static final char CARD_OFFSET = 'A';

  public static void main(String[] args) {
    new Day22().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return solve(reader, false);
  }

  @Override
  public Integer part2(BufferedReader reader) {
    return solve(reader, true);
  }

  private static int solve(BufferedReader reader, boolean recursive) {
    var decks = AdventUtil.readInputGrouped(reader).iterator();

    var p1 = decks.next().skip(1).mapToInt(Integer::parseInt).toArray();
    var p2 = decks.next().skip(1).mapToInt(Integer::parseInt).toArray();

    var deck = new char[p1.length * 2];
    for (int i = 0; i < p1.length; i++) {
      deck[i] = (char) (CARD_OFFSET + p1[i]);
    }

    var deckPos = p1.length;
    for (int i = p2.length - 1; i >= 0; i--) {
      deck[deckPos++] = (char) (CARD_OFFSET + p2[i]);
    }

    var game = new DoubleDeck(deck);
    playCombat(game, recursive, new HashSet<>(1000), new HashSet<>(1000));

    return game.tallyScore();
  }

  private static Player playCombat(DoubleDeck game,
                                   boolean recursive,
                                   Set<String> hist1,
                                   Set<String> hist2) {
    HashSet<String> hist1Rec = null;
    HashSet<String> hist2Rec = null;
    char[] gameArrRec = null;

    while (!game.ended()) {
      if (recursive && (!hist1.add(game.serialize(Player.P1)) || !hist2.add(game.serialize(Player.P2)))) {
        return Player.P1;
      }

      var c1 = game.drawCard(Player.P1);
      var c2 = game.drawCard(Player.P2);

      var roundWinner = c1 > c2 ? Player.P1 : Player.P2;
      if (recursive && game.canRecurse()) {
        // reuse the history set for independent recursive games
        // also reuse the backing array for DoubleDeck
        if (hist1Rec == null) {
          hist1Rec = new HashSet<>(1000);
        } else {
          hist1Rec.clear();
        }
        if (hist2Rec == null) {
          hist2Rec = new HashSet<>(1000);
        } else {
          hist2Rec.clear();
        }

        if (gameArrRec == null) {
          gameArrRec = new char[game.cards.length];
        }

        roundWinner = playCombat(game.copy(gameArrRec), true, hist1Rec, hist2Rec);
      }

      game.moveCards(roundWinner);
    }

    return game.winner();
  }

  /**
   * Store the 2 decks in 1 array
   *
   * P1's deck is stored on the left, going right
   * P2's deck is store on the right, going left
   */
  private static class DoubleDeck {
    private final char[] cards;
    private int p1TopIdx;
    private int p1Length;
    private int p2TopIdx;
    private int p2Length;

    public DoubleDeck(char[] cards, int p1TopIdx, int p1Length, int p2TopIdx, int p2Length) {
      this.cards = cards;
      this.p1TopIdx = p1TopIdx;
      this.p1Length = p1Length;
      this.p2TopIdx = p2TopIdx;
      this.p2Length = p2Length;
    }

    public DoubleDeck(char[] cards) {
      this(cards, 0, cards.length / 2, cards.length - 1, cards.length / 2);
    }

    public int drawCard(Player player) {
      return switch (player) {
        case P1 -> {
          p1Length--;
          yield cards[p1TopIdx++];
        }
        case P2 -> {
          p2Length--;
          yield cards[p2TopIdx--];
        }
      };
    }

    public void moveCards(Player winner) {
      // remember the 2 cards to be moved
      var p1Card = cards[p1TopIdx - 1];
      var p2Card = cards[p2TopIdx + 1];

      // move the p1 part 1 slot to the left
      System.arraycopy(
          cards,
          p1TopIdx,
          cards,
          --p1TopIdx,
          p1Length
      );
      // move the p2 part 1 slot to the right
      System.arraycopy(
          cards,
          p2TopIdx - p2Length + 1,
          cards,
          ++p2TopIdx - p2Length + 1,
          p2Length
      );

      // put the new cards back
      cards[p1TopIdx + p1Length] = p1Card;
      cards[p1TopIdx + p1Length + 1] = p2Card;

      // from the winner's perspective the 2 cards added to the bottom
      // are always in the correct ordering

      switch (winner) {
        case P1 -> p1Length += 2;
        case P2 -> p2Length += 2;
      }
    }

    public boolean ended() {
      return p1Length == 0 || p2Length == 0;
    }

    public Player winner() {
      return p1Length != 0 ? Player.P1 : Player.P2;
    }

    public boolean canRecurse() {
      return p1Length >= (cards[p1TopIdx - 1] - CARD_OFFSET) && p2Length >= (cards[p2TopIdx + 1] - CARD_OFFSET);
    }

    public String serialize(Player player) {
      var start = switch (player) {
        case P1 -> p1TopIdx;
        case P2 -> p2TopIdx - p2Length + 1;
      };

      var end = switch (player) {
        case P1 -> p1TopIdx + p1Length;
        case P2 -> p2TopIdx + 1;
      };

      return new String(cards, start, end - start);
    }

    public DoubleDeck copy(char[] newCards) {
      var p1CopyLength = (cards[p1TopIdx - 1] - CARD_OFFSET);
      var p1New = p1TopIdx + (p1Length - p1CopyLength);

      var p2CopyLength = (cards[p2TopIdx + 1] - CARD_OFFSET);

      // copy the existing cards into a new array such that
      // all the empty spaces are only gathered at the left-most and right-most
      // ends of the array

      System.arraycopy(
          cards,
          p1TopIdx,
          newCards,
          p1New,
          p1CopyLength
      );
      System.arraycopy(
          cards,
          p2TopIdx - p2CopyLength + 1,
          newCards,
          p2TopIdx - p2Length + 1,
          p2CopyLength
      );

      return new DoubleDeck(
          newCards,
          p1New,
          p1CopyLength,
          p2TopIdx - (p2Length - p2CopyLength),
          p2CopyLength
      );
    }

    public int tallyScore() {
      var player = winner();

      var mul = switch (player) {
        case P1 -> cards.length;
        case P2 -> 1;
      };

      var mulDelta = switch (player) {
        case P1 -> -1;
        case P2 -> 1;
      };

      var sum = 0;
      for (int start = p1TopIdx; start < p2TopIdx + 1; start++) {
        sum += (cards[start] - CARD_OFFSET) * mul;
        mul += mulDelta;
      }

      return sum;
    }
  }

  private enum Player {
    P1, P2
  }
}
