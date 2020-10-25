package dev.linl33.adventofcode.lib.point;

public record Point2D(int x, int y) implements Point<Point2D> {
  public Point2D(String x, String y) {
    this(Integer.parseInt(x), Integer.parseInt(y));
  }

  public Point2D translate(int deltaX, int deltaY) {
    return new Point2D(x() + deltaX, y() + deltaY);
  }

  @Override
  public Point2D translate(Point2D delta) {
    return translate(delta.x(), delta.y());
  }

  @Override
  public int manhattanDistance(Point2D anotherPoint) {
    return Math.abs(x() - anotherPoint.x()) + Math.abs(y() - anotherPoint.y());
  }
}
