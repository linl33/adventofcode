package dev.linl33.adventofcode.lib.grid;

import java.util.Arrays;

public interface ArrayGrid extends Grid {
  int[] array();

  default void invertX() {
    for (int y = 0; y < height(); y++) {
      for (int x = 0; x < (width()) / 2; x++) {
        var tmp = get(x, y);
        set(x, y, get((width() - 1) - x, y));
        set((width() - 1) - x, y, tmp);
      }
    }
  }

  default void invertY() {
    for (int y = 0; y < (height()) / 2; y++) {
      for (int x = 0; x < width(); x++) {
        var tmp = get(x, y);
        set(x, y, get(x, (height() - 1) - y));
        set(x, (height() - 1) - y, tmp);
      }
    }
  }

  default void rotateClockwise() {
    var backup = Arrays.copyOf(array(), array().length);

    for (int y = 0; y < height(); y++) {
      for (int x = 0; x < width(); x++) {
        set(y, x, backup[y * height() + x]);
      }
    }

    invertX();
  }

  default void rotateCounterClockwise() {
    var backup = Arrays.copyOf(array(), array().length);

    for (int y = 0; y < height(); y++) {
      for (int x = 0; x < width(); x++) {
        set(y, x, backup[y * height() + x]);
      }
    }

    invertY();
  }
}
