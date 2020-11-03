package dev.linl33.adventofcode.lib.grid;

public interface GridConfiguration {
  GridConfiguration DEFAULT = new DefaultGridConfiguration();

  boolean isBlocked(int value);

  boolean isEmptySpace(int value);

  int wall();

  int emptySpace();

  class DefaultGridConfiguration implements GridConfiguration {
    private DefaultGridConfiguration() {}

    @Override
    public boolean isBlocked(int value) {
      return value == '#' || value == ' ';
    }

    @Override
    public boolean isEmptySpace(int value) {
      return value == '.';
    }

    @Override
    public int wall() {
      return '#';
    }

    @Override
    public int emptySpace() {
      return ' ';
    }
  }
}
