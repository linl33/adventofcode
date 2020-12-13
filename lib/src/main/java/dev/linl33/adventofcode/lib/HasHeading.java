package dev.linl33.adventofcode.lib;

import dev.linl33.adventofcode.lib.util.GridUtil;

@FunctionalInterface
public interface HasHeading {
  @GridUtil.GridHeading
  int getHeading();

  default char getHeadingAsChar() {
    return GridUtil.headingToChar(getHeading());
  }
}
