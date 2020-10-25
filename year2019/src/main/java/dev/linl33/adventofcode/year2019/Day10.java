package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.util.AdventUtil;
import dev.linl33.adventofcode.lib.util.MathUtil;
import dev.linl33.adventofcode.lib.util.StreamUtil;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10 extends AdventSolution2019<Integer, Integer> {
  private static final char ASTEROID_CHAR = '#';
  private static final double HALF_PI = Math.PI / 2d;
  private static final double TWO_PI = 2d * Math.PI;

  public static void main(String[] args) {
    new Day10().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return Collections.max(countVisibleAsteroids(buildGrid(reader)).values());
  }

  @Override
  public Integer part2(BufferedReader reader) {
    var grid = buildGrid(reader);

    var bestAsteroid = AdventUtil.argMax(countVisibleAsteroids(grid));
    var x = bestAsteroid.x();
    var y = bestAsteroid.y();

    var pt = orderAsteroidsByDestruction(grid, x, y, false).get(199);
    return pt.x() * 100 + pt.y();
  }

  public List<Point2D> part2Detailed(BufferedReader reader) {
    var grid = buildGrid(reader);
    var bestAsteroid = AdventUtil.argMax(countVisibleAsteroids(grid));

    return orderAsteroidsByDestruction(grid, bestAsteroid.x(), bestAsteroid.y(), true);
  }

  private static ArrayList<Point2D> orderAsteroidsByDestruction(char[][] grid, int x, int y, boolean fullList) {
    var height = grid.length;
    var width = grid[0].length;

    var bestAsteroid = new Point2D(x, y);

    var asteroidSet = buildAsteroidSet(grid);
    asteroidSet.remove(bestAsteroid);

    var comparator = Comparator
        .<Point2D>comparingDouble(pt -> translateAngle(Math.atan2(y - pt.y(), pt.x() - x)))
        .thenComparing(Point2D::toString);

    var asteroidList = new ArrayList<Point2D>();
    while (asteroidSet.size() > 0) {
      var layer = new TreeSet<>(comparator);
      var blockedAsteroids = new HashSet<Point2D>();

      asteroidOrderIntStream(-y, height - y)
          .forEachOrdered(deltaY -> asteroidOrderIntStream(-x, width - x)
              .filter(deltaX -> deltaX != 0 || deltaY != 0)
              .filter(deltaX -> grid[y + deltaY][x + deltaX] == ASTEROID_CHAR)
              .filter(deltaX -> asteroidSet.remove(bestAsteroid.translate(deltaX, deltaY)))
              .forEachOrdered(deltaX -> {
                layer.add(bestAsteroid.translate(deltaX, deltaY));
                findAsteroidInLineOfSight(bestAsteroid, deltaX, deltaY, height, width).forEach(a -> {
                  if (asteroidSet.remove(a)) {
                    blockedAsteroids.add(a);
                  }
                });
              })
          );

      asteroidList.addAll(layer);
      if (!fullList && asteroidList.size() >= 200) {
        return asteroidList;
      }

      asteroidSet.addAll(blockedAsteroids);
    }

    return asteroidList;
  }

  private static HashSet<Point2D> buildAsteroidSet(char[][] grid) {
    var set = new HashSet<Point2D>();

    var height = grid.length;
    var width = grid[0].length;

    for (var i = 0; i < height; i++) {
      for (var j = 0; j < width; j++) {
        if (grid[i][j] == ASTEROID_CHAR) {
          set.add(new Point2D(j, i));
        }
      }
    }

    return set;
  }

  private static char[][] buildGrid(BufferedReader reader) {
    return reader
        .lines()
        .map(String::toCharArray)
        .toArray(char[][]::new);
  }

  private static Map<Point2D, Integer> countVisibleAsteroids(char[][] grid) {
    var height = grid.length;
    var width = grid[0].length;

    var allAsteroids = buildAsteroidSet(grid);
    return allAsteroids
        .stream()
        .map(asteroid -> {
          var x = asteroid.x();
          var y = asteroid.y();
          var asteroidSet = new HashSet<>(allAsteroids);

          var count = asteroidOrderIntStream(-y, height - y)
              .flatMap(deltaY -> asteroidOrderIntStream(-x, width - x)
                  .filter(deltaX -> deltaX != 0 || deltaY != 0)
                  .filter(deltaX -> grid[y + deltaY][x + deltaX] == ASTEROID_CHAR)
                  .filter(deltaX -> asteroidSet.remove(asteroid.translate(deltaX, deltaY)))
                  .map(deltaX -> {
                    findAsteroidInLineOfSight(asteroid, deltaX, deltaY, height, width).forEach(asteroidSet::remove);
                    return 1;
                  })
              )
              .sum();

          return new AbstractMap.SimpleImmutableEntry<>(asteroid, count);
        })
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static IntStream asteroidOrderIntStream(int startInclusive, int endExclusive) {
    return IntStream.concat(
        StreamUtil.rangeReverseClosed(-1, startInclusive),
        IntStream.range(0, endExclusive)
    );
  }

  private static List<Point2D> findAsteroidInLineOfSight(Point2D asteroid, int deltaX, int deltaY, int maxHeight, int maxWidth) {
    var asteroids = new ArrayList<Point2D>();

    var withinBounds = true;
    var gcd = MathUtil.gcd(Math.abs(deltaX), Math.abs(deltaY));
    var reducedX = deltaX / gcd;
    var reducedY = deltaY / gcd;
    var mul = 2;
    while (withinBounds) {
      var o = asteroid.translate(reducedX * mul, reducedY * mul);
      asteroids.add(o);
      mul++;
      withinBounds = o.x() >= 0 && o.x() < maxWidth && o.y() >= 0 && o.y() < maxHeight;
    }

    return asteroids;
  }

  private static double translateAngle(double angle) {
    if (angle < 0d) {
      return -angle + HALF_PI;
    }

    if (angle >= 0d && angle <= HALF_PI) {
      return HALF_PI - angle;
    }

    if (angle > HALF_PI && angle <= Math.PI) {
      return TWO_PI - angle + HALF_PI;
    }

    throw new IllegalArgumentException();
  }
}
