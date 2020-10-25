package dev.linl33.adventofcode.year2018;

import dev.linl33.adventofcode.lib.AdventLinkedListNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class Day14 extends AdventSolution2018<String, Integer> {
  public static void main(String[] args) {
    new Day14().runAndPrintAll();
  }

  @Override
  public String part1(BufferedReader reader) throws IOException {
    var recipes = Integer.parseInt(reader.readLine());

    var elf1 = new AdventLinkedListNode<>(3);
    var elf2 = new AdventLinkedListNode<>(7);
    var first = elf1;
    var last = elf2;
    var sum = elf1.getValue() + elf2.getValue();
    var count = 2;

    elf1.linkAfter(elf2);
    elf1.linkBefore(elf2);
    elf2.linkAfter(elf1);
    elf2.linkBefore(elf1);

    while (count <= recipes + 10) {
      for (var digit : getDigits(sum)) {
        var node = new AdventLinkedListNode<>(digit);

        last.linkAfter(node);
        last = node;

        count++;
      }

      elf1 = elf1.nextN(1 + elf1.getValue());
      elf2 = elf2.nextN(1 + elf2.getValue());
      sum = elf1.getValue() + elf2.getValue();
    }

    return get10Score(first.toString(), recipes);
  }

  @Override
  public Integer part2(BufferedReader reader) throws IOException {
    var recipe = reader.readLine();
    var recipeArr = Arrays
        .stream(recipe.split(""))
        .mapToInt(Integer::parseInt)
        .toArray();

    var elf1 = new AdventLinkedListNode<>(3);
    var elf2 = new AdventLinkedListNode<>(7);
    var first = elf1;
    var last = elf2;
    var sum = elf1.getValue() + elf2.getValue();
    var count = 2;

    elf1.linkAfter(elf2);
    elf1.linkBefore(elf2);
    elf2.linkAfter(elf1);
    elf2.linkBefore(elf1);

    while (true) {
      var addedNodes = 0;

      for (var digit : getDigits(sum)) {
        var node = new AdventLinkedListNode<>(digit);

        last.linkAfter(node);
        last = node;

        count++;
        addedNodes++;
      }

      elf1 = elf1.nextN(1 + elf1.getValue());
      elf2 = elf2.nextN(1 + elf2.getValue());
      sum = elf1.getValue() + elf2.getValue();

      if (count >= recipeArr.length) {
        if (linkedListContains(first, recipeArr, last.nextN(-recipeArr.length - addedNodes))) {
          if (last.getValue() != recipeArr[recipeArr.length - 1]) {
            count--;
          }

          break;
        }
      }
    }

    return count - recipeArr.length;
  }

  private static int[] getDigits(int sum) {
    if (sum < 10) {
      return new int[] {sum};
    } else {
      return new int[] {sum / 10, sum % 10};
    }
  }

  private static String get10Score(String scoreboard, int start) {
    var s = scoreboard.split(" ");

    var sb = new StringBuilder();
    for (var i = start; i < start + 10; i++) {
      sb.append(s[i]);
    }

    return sb.toString();
  }

  private static boolean linkedListContains(AdventLinkedListNode<Integer> list, int[] target, AdventLinkedListNode<Integer> start) {
    var cursor = start;
    var arrCursor = 0;
    var found = false;

    do {
      // the ints are all single digits
      if (cursor.getValue() == target[arrCursor]) {
        arrCursor++;
        found = true;
      } else {
        arrCursor = 0;
        found = false;
      }

      cursor = cursor.getNext();
    } while (arrCursor < target.length && cursor != list);

    return found && arrCursor == target.length;
  }
}
