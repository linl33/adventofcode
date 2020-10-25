package dev.linl33.adventofcode.lib;

import dev.linl33.adventofcode.lib.util.GridUtil;

public interface HasHeading {
  int getHeading();

  default char getHeadingAsChar() {
    return GridUtil.headingToChar(getHeading());
  }
}
