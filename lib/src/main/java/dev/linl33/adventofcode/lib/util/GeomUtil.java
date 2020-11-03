package dev.linl33.adventofcode.lib.util;

import dev.linl33.adventofcode.lib.point.Point2D;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class GeomUtil {

  public static <T, U> U[][] mappingPointsToGrid(Map<Point2D, T> points,
                                                 Function<T, U> mapping,
                                                 U defaultValue,
                                                 Class<U> uClass,
                                                 boolean invertX,
                                                 boolean invertY) {
    return mappingPointsToGrid(points.keySet(), mapping.compose(points::get), defaultValue, uClass, invertX, invertY);
  }

  public static <U> U[][] mappingPointsToGrid(Set<Point2D> points,
                                              U pointValue,
                                              U defaultValue,
                                              Class<U> uClass,
                                              boolean invertX,
                                              boolean invertY) {
    return GeomUtil.<U>mappingPointsToGrid(points, __ -> pointValue, defaultValue, uClass, invertX, invertY);
  }

  public static <U> U[][] mappingPointsToGrid(Set<Point2D> points,
                                              Function<Point2D, U> pointValueGetter,
                                              U defaultValue,
                                              Class<U> uClass,
                                              boolean invertX,
                                              boolean invertY) {
    var uGrid = pointsToGrid(points, pointValueGetter, uClass, invertX, invertY);

    for (int y = 0; y < uGrid.length; y++) {
      for (int x = 0; x < uGrid[y].length; x++) {
        if (uGrid[y][x] == null) {
          uGrid[y][x] = defaultValue;
        }
      }
    }

    return uGrid;
  }

  public static <T> T[][] pointsToGrid(Map<Point2D, T> points, Class<T> tClass, boolean invertX, boolean invertY) {
    return pointsToGrid(points.keySet(), points::get, tClass, invertX, invertY);
  }

  public static <T> T[][] pointsToGrid(Set<Point2D> points,
                                       Function<Point2D, T> pointValueGetter,
                                       Class<T> tClass,
                                       boolean invertX,
                                       boolean invertY) {
    var xMin = points.stream().mapToInt(Point2D::x).min().orElseThrow();
    var xMax = points.stream().mapToInt(Point2D::x).max().orElseThrow();

    var yMin = points.stream().mapToInt(Point2D::y).min().orElseThrow();
    var yMax = points.stream().mapToInt(Point2D::y).max().orElseThrow();

    @SuppressWarnings("unchecked")
    T[][] grid = (T[][]) Array.newInstance(tClass, yMax - yMin + 1, xMax - xMin + 1);
    pointsToGrid(points, pointValueGetter, xMin, yMin, invertX, invertY, grid);

    return grid;
  }

  public static <T> void pointsToGrid(Map<Point2D, T> points, int xOffset, int yOffset, boolean invertX, boolean invertY, T[][] grid) {
    pointsToGrid(points.keySet(), points::get, xOffset, yOffset, invertX, invertY, grid);
  }

  public static <T> void pointsToGrid(Set<Point2D> points,
                                      Function<Point2D, T> pointValueGetter,
                                      int xOffset,
                                      int yOffset,
                                      boolean invertX,
                                      boolean invertY,
                                      T[][] grid) {
    for (var pt : points) {
      var x = pt.x() - xOffset;
      var y = pt.y() - yOffset;

      if (invertX) {
        x = (grid[0].length - 1) - x;
      }

      if (invertY) {
        y = (grid.length - 1) - y;
      }

      grid[y][x] = pointValueGetter.apply(pt);
    }
  }
}
