package dev.linl33.adventofcode.lib;

import dev.linl33.adventofcode.lib.point.Point2D;

import java.util.Comparator;

public interface GridEntity extends Comparable<GridEntity> {
  Comparator<GridEntity> COMPARATOR = Comparator
      .<GridEntity>comparingInt(c -> c.getPosition().y())
      .thenComparingInt(c -> c.getPosition().x())
      .thenComparing(GridEntity::getId);

  String getId();
  Point2D getPosition();

  @Override
  default int compareTo(GridEntity o) {
    return COMPARATOR.compare(this, o);
  }
}
