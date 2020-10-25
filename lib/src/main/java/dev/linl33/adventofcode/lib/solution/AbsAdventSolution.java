package dev.linl33.adventofcode.lib.solution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbsAdventSolution<T1, T2> implements AdventSolution<T1, T2> {
  private final Logger logger;

  @Override
  public Logger getLogger() {
    return logger;
  }

  protected AbsAdventSolution() {
    logger = LogManager.getLogger(getClass());
  }

  @Override
  public int getDay() {
    return Integer.parseInt(getClass().getSimpleName().substring(3));
  }
}
