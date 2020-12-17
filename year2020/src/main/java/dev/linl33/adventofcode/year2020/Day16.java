package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.util.AdventUtil;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

public class Day16 extends AdventSolution2020<Integer, Long> {
  private static final String DEPARTURE_PREFIX = "departure ";
  private static final int DEPARTURE_FIELD_COUNT = 6;

  public static void main(String[] args) {
    new Day16().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    var notes = TrainNotes.parse(reader, false);

    return Arrays
        .stream(notes.nearbyTickets)
        .filter(notes.anyMatch.negate())
        .sum();
  }

  @Override
  public Long part2(BufferedReader reader) throws Exception {
    var notes = TrainNotes.parse(reader, true);

    // store the possible fields for each field
    // if the ith bit is unset then the ith field is possible
    var possibilities = new int[notes.ticketSize];

    // go through each field in each ticket and find rules that apply
    for (int i = 0; i < notes.nearbyTickets.length; i++) {
      var searchSpace = ~possibilities[i % notes.ticketSize];

      // skip if this field has only 1 possibility
      if (Integer.bitCount(searchSpace) == Integer.SIZE - notes.ticketSize + 1) {
        continue;
      }

      for (int ruleIdx = 0; ruleIdx < notes.ticketSize; ruleIdx++) {
        if (((searchSpace >> ruleIdx) & 1) == 0) {
          continue;
        }

        if (!notes.rules[ruleIdx].test(notes.nearbyTickets[i])) {
          possibilities[i % notes.ticketSize] |= (1 << ruleIdx);
        }
      }
    }

    var depFieldsFound = 0;
    var fieldProduct = 1L;

    while (depFieldsFound < DEPARTURE_FIELD_COUNT) {
      for (int i = 0; i < possibilities.length; i++) {
        var possible = ~possibilities[i];

        if (Integer.bitCount(possible) != Integer.SIZE - notes.ticketSize + 1) {
          continue;
        }

        var ruleIdx = Integer.numberOfTrailingZeros(possible);
        if (notes.isDepartureField[ruleIdx] == 1 && depFieldsFound++ < DEPARTURE_FIELD_COUNT) {
          fieldProduct *= notes.ticket[i];
        }

        for (int j = 0; j < possibilities.length; j++) {
          possibilities[j] |= (1 << ruleIdx);
        }
      }
    }

    return fieldProduct;
  }

  private static record TrainNotes(IntPredicate[] rules,
                                   int[] ticket,
                                   int[] nearbyTickets,
                                   IntPredicate anyMatch,
                                   int[] isDepartureField,
                                   int ticketSize) {
    public static TrainNotes parse(BufferedReader reader, boolean filterInvalid) {
      var input = AdventUtil
          .readInputGrouped(reader)
          .iterator();

      var fields = input.next()
          .collect(Collectors.toMap(
              rule -> rule.split(":")[0],
              TrainNotes::parsePair,
              (left, __) -> left,
              LinkedHashMap::new
          ));

      var anyMatch = fields
          .values()
          .stream()
          .reduce(IntPredicate::or)
          .orElseThrow();

      var myTicket = input.next()
          .skip(1)
          .map(TrainNotes::ticketToIntArr)
          .findAny()
          .orElseThrow();

      var nearbyTickets = input.next()
          .skip(1)
          .map(t -> ticketToIntArr(t, myTicket.length))
          .filter(t -> !filterInvalid || Arrays.stream(t).allMatch(anyMatch))
          .flatMapToInt(Arrays::stream)
          .toArray();

      return new TrainNotes(
          fields.values().toArray(new IntPredicate[0]),
          myTicket,
          nearbyTickets,
          anyMatch,
          fields.keySet().stream().mapToInt(f -> f.startsWith(DEPARTURE_PREFIX) ? 1 : 0).toArray(),
          myTicket.length
      );
    }

    private static IntPredicate parsePair(String pair) {
      var parts = pair.split(" ");
      var range1 = rangeToIntArr(parts[parts.length - 1]);
      var range2 = rangeToIntArr(parts[parts.length - 3]);

      return pairedClosedRangePredicate(range1[0], range1[1], range2[0], range2[1]);
    }

    private static IntPredicate pairedClosedRangePredicate(int from, int to, int from1, int to1) {
      return i -> (i >= from && i <= to) || (i >= from1 && i <= to1);
    }

    private static int[] rangeToIntArr(String range) {
      var parts = range.split("-");
      return new int[] {
          Integer.parseInt(parts[0]),
          Integer.parseInt(parts[1])
      };
    }

    private static int[] ticketToIntArr(String ticket) {
      return Arrays
          .stream(ticket.split(","))
          .mapToInt(Integer::parseInt)
          .toArray();
    }

    private static int[] ticketToIntArr(String ticket, int ticketSize) {
      // split the ticket into an int array manually
      // String.split is slow

      var result = new int[ticketSize];
      var fieldVal = 0;
      var valMultiplier = 1;
      var counter = ticketSize - 1;

      for (int i = ticket.length() - 1; i >= 0; i--) {
        var c = ticket.charAt(i);
        if (c == ',') {
          result[counter--] = fieldVal;
          fieldVal = 0;
          valMultiplier = 1;
        } else {
          fieldVal += valMultiplier * (c - '0');
          valMultiplier *= 10;
        }
      }
      result[counter] = fieldVal;

      return result;
    }
  }
}
