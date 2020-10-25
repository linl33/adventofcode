package dev.linl33.adventofcode.lib.util;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class LoggingUtil {
  public static final Marker SOLUTION_MARKER = MarkerManager.getMarker("SOLUTION");
  public static final Marker SOLUTION_RESULT_MARKER = MarkerManager.getMarker("SOLUTION_RESULT");
}
