package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.point.Point2D;

import java.io.BufferedReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day3 extends AdventSolution2020<Integer, Long> {
  private static final char TREE_CHAR = '#';

  public static void main(String[] args) {
    new Day3().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return reader
        .lines()
        .collect(Collectors.collectingAndThen(
            Collectors.toUnmodifiableList(),
            trees -> countTrees(trees, 3, 1)
        ));
  }

  @Override
  public Long part2(BufferedReader reader) {
    return reader
        .lines()
        .collect(Collectors.collectingAndThen(
            Collectors.toUnmodifiableList(),
            trees -> Stream
                .of(
                    new Point2D(1, 1),
                    new Point2D(3, 1),
                    new Point2D(5, 1),
                    new Point2D(7, 1),
                    new Point2D(1, 2)
                )
                .mapToLong(slope -> countTrees(trees, slope.x(), slope.y()))
                .reduce(1, (a, b) -> a * b)
        ));
  }

  private static int countTrees(List<String> trees, int slopeX, int slopeY) {
    var treeCount = 0;
    var lineWidth = trees.get(0).length();

    var posX = 0;
    for (int posY = 0; posY < trees.size(); posY += slopeY) {
      if (trees.get(posY).charAt(posX) == TREE_CHAR) {
        treeCount++;
      }

      posX = (posX + slopeX) % lineWidth;
    }

    return treeCount;
  }
}
