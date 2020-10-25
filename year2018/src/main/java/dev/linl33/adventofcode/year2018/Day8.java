package dev.linl33.adventofcode.year2018;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Day8 extends AdventSolution2018<Integer, Integer> {
  public static void main(String[] args) {
    new Day8().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    List<Integer> serializedTree = readTree(reader);

    var sum = 0;
    while (serializedTree.size() > 0) {
      sum += collapseTree(serializedTree);
    }

    return sum;
  }

  @Override
  public Integer part2(BufferedReader reader) {
    List<Integer> serializedTree = readTree(reader);

    while (serializedTree.size() > 3) {
      collapseTreePart2(serializedTree);
    }

    return serializedTree.get(2);
  }

  private static List<Integer> readTree(BufferedReader reader) {
    return reader
        .lines()
        .limit(1)
        .flatMap(line -> Arrays.stream(line.split(" ")))
        .map(Integer::parseInt)
        .collect(Collectors.toList());
  }

  private static Integer collapseTree(List<Integer> serializedTree) {
    Integer sum = null;

    for (int i = 0; i < serializedTree.size(); i += 2) {
      var childCount = serializedTree.get(i);
      var metadataCount = serializedTree.get(i + 1);

      if (childCount == 0) {
        sum = 0;

        for (int j = 0; j < metadataCount; j++) {
          sum += serializedTree.remove(i + 2);
        }

        if (i - 2 > -1) {
          serializedTree.set(i - 2, serializedTree.get(i - 2) - 1);
        }

        serializedTree.remove(i);
        serializedTree.remove(i);
        return sum;
      }
    }

    return sum;
  }

  private static void collapseTreePart2(List<Integer> serializedTree) {
    for (int i = 0; i < serializedTree.size(); i += 2) {
      var childCount = serializedTree.get(i);
      var metadataCount = serializedTree.get(i + 1);

      if (childCount == 0) {
        if (metadataCount == 1 || metadataCount == 0) {
          i += metadataCount;
          continue;
        }

        var sum = 0;

        for (int j = 0; j < metadataCount; j++) {
          sum += serializedTree.remove(i + 2);
        }

        serializedTree.set(i + 1, 1);
        serializedTree.add(i + 2, sum);

        return;
      } else {
        boolean ready = true;
        var childValueMap = new HashMap<Integer, Integer>();
        var childCursor = -1;
        for (int j = 0; j < childCount; j++) {
          if (childCursor == -1) {
            childCursor = i + 2;
          } else {
            childCursor += serializedTree.get(childCursor + 1) + 2;
          }

          var childJChildCount = serializedTree.get(childCursor);
          var childJMetadataCount = serializedTree.get(childCursor + 1);
          if (childJChildCount != 0 || childJMetadataCount > 1) {
            ready = false;
            break;
          }

          childValueMap.put(j + 1, childJMetadataCount == 0 ? 0 : serializedTree.get(childCursor + 2));
        }

        if (ready) {
          var sum = 0;

          childCursor += serializedTree.get(childCursor + 1) + 2;
          for (int j = 0; j < metadataCount; j++) {
            var metadataJ = serializedTree.get(childCursor + j);
            if (metadataJ > childCount || metadataJ < 1) {
              continue;
            }

            sum += childValueMap.get(metadataJ);
          }

          for (int j = 0; j < childCursor - i - 2 + metadataCount; j++) {
            serializedTree.remove(i + 2);
          }

          serializedTree.set(i, 0);
          serializedTree.set(i + 1, 1);
          serializedTree.add(i + 2, sum);

          return;
        }
      }
    }
  }
}
