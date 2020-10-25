package dev.linl33.adventofcode.lib.point;

public record Point3D(int x, int y, int z) implements Point<Point3D> {
  public Point3D(String x, String y, String z) {
    this(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z));
  }

  public Point3D translate(int deltaX, int deltaY, int deltaZ) {
    return new Point3D(x() + deltaX, y() + deltaY, z() + deltaZ);
  }

  @Override
  public Point3D translate(Point3D delta) {
    return translate(delta.x(), delta.y(), delta.z());
  }

  @Override
  public int manhattanDistance(Point3D anotherPoint) {
    return Math.abs(x() - anotherPoint.x()) +
        Math.abs(y() - anotherPoint.y()) +
        Math.abs(z() - anotherPoint.z());
  }
}
