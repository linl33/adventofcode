package dev.linl33.adventofcode.lib.point;

public interface Point<T extends Point<T>> {
  Point2D ORIGIN_2D = new Point2D(0, 0);
  Point3D ORIGIN_3D = new Point3D(0, 0, 0);

  T translate(T delta);

  int manhattanDistance(T anotherPoint);

  int squaredEuclideanDistance(T anotherPoint);
}
