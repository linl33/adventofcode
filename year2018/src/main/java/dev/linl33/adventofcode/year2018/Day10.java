package dev.linl33.adventofcode.year2018;

import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.util.GeomUtil;
import dev.linl33.adventofcode.lib.util.PrintUtil;

import java.io.BufferedReader;
import java.util.stream.Collectors;

public class Day10 extends AdventSolution2018<Character[][], Integer> {
  public static void main(String[] args) {
    new Day10().runAndPrintAll();
  }

  @Override
  public Character[][] part1(BufferedReader reader) {
    return part1And2Internal(reader).image();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    return part1And2Internal(reader).serial();
  }

  private LightImage part1And2Internal(BufferedReader reader) {
    var lights = reader
        .lines()
        .map(line -> {
          var posX = Integer.parseInt(line.substring(10, 16).trim());
          var posY = Integer.parseInt(line.substring(18, 24).trim());
          var velX = Integer.parseInt(line.substring(36, 38).trim());
          var velY = Integer.parseInt(line.substring(40, 42).trim());
          return new Light(new Point2D(posX, posY), new Point2D(velX, velY));
        })
        .collect(Collectors.toList());

    var prevRange = Integer.MAX_VALUE;
    Character[][] image = null;

    for (int i = 0; i < 30000; i++) {
      var iCopy = i;
      var lightPos = lights
          .stream()
          .map(l -> new Point2D(
              l.initialPt().x() + iCopy * l.velocity().x(),
              l.initialPt().y() + iCopy * l.velocity().y()
          ))
          .collect(Collectors.toSet());

      var xSummary = lightPos
          .stream()
          .mapToInt(Point2D::x)
          .summaryStatistics();

      var ySummary = lightPos
          .stream()
          .mapToInt(Point2D::y)
          .summaryStatistics();

      var lStart = Math.min(xSummary.getMin(), ySummary.getMin()) - 30;
      var lEnd = Math.max(xSummary.getMax(), ySummary.getMax()) + 30;
      var lRange = lEnd - lStart;

      if (lRange > prevRange) {
        // subtract 1 to get the previous iteration
        return new LightImage(image, i - 1);
      }

      if (lRange < 300) {
        image = GeomUtil.mappingPointsToGrid(
            lightPos,
            PrintUtil.FULL_BLOCK,
            ' ',
            Character.class,
            false,
            false
        );
      }

      prevRange = lRange;
    }

    throw new IllegalArgumentException();
  }

  private static record Light(Point2D initialPt, Point2D velocity) {}

  private static record LightImage(Character[][] image, int serial) {}
}
