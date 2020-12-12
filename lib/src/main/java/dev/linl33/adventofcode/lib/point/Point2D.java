package dev.linl33.adventofcode.lib.point;

public record Point2D(int x, int y) implements Point<Point2D> {
  public enum Rotation {
    CW_90, CCW_90, CW_270, CCW_270, R_180
  }

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

  @Override
  public int squaredEuclideanDistance(Point2D anotherPoint) {
    return (int) Math.pow(x() - anotherPoint.x(), 2) + (int) Math.pow(y() - anotherPoint.y(), 2);
  }

  public Point2D rotateAboutOrigin(Rotation rotation) {
    return switch (rotation) {
      case CW_90, CCW_270 -> new Point2D(y, -x);
      case CCW_90, CW_270 -> new Point2D(-y, x);
      case R_180 -> new Point2D(-x, -y);
    };
  }
}
