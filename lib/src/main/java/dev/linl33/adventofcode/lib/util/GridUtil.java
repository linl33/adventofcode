package dev.linl33.adventofcode.lib.util;

import dev.linl33.adventofcode.lib.GridEntity;
import dev.linl33.adventofcode.lib.HasHeading;
import dev.linl33.adventofcode.lib.grid.Grid;
import dev.linl33.adventofcode.lib.grid.GridVisitResult;
import dev.linl33.adventofcode.lib.point.Point2D;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class GridUtil {
  public enum TurningDirection {
    RIGHT_90, LEFT_90, RIGHT_270, LEFT_270, INVERT, NULL
  }

  public static final int HEADING_NORTH = 0;
  public static final int HEADING_EAST = 1;
  public static final int HEADING_SOUTH = 2;
  public static final int HEADING_WEST = 3;
  public static final int[] HEADINGS = new int[]{HEADING_NORTH, HEADING_EAST, HEADING_SOUTH, HEADING_WEST};

  public static int turn(TurningDirection direction, int heading) {
    return switch (direction) {
      case RIGHT_90, LEFT_270 -> HEADINGS[(heading + 1) % 4];
      case LEFT_90, RIGHT_270 -> HEADINGS[Math.floorMod(heading - 1, 4)];
      case INVERT -> HEADINGS[(heading + 2) % 4];
      case NULL -> heading;
    };
  }

  public static Point2D move(Point2D curr, int heading) {
    return move(curr, heading, false, false);
  }

  public static Point2D move(Point2D curr, int heading, boolean invertX, boolean invertY) {
    return move(curr, heading, 1, invertX, invertY);
  }

  public static <T extends GridEntity & HasHeading> Point2D move(T entity, boolean invertX, boolean invertY) {
    return move(entity.getPosition(), entity.getHeading(), invertX, invertY);
  }

  public static Point2D move(Point2D curr, int heading, int units, boolean invertX, boolean invertY) {
    if (invertX && (heading == 1 || heading == 3)) {
      heading = turn(TurningDirection.INVERT, heading);
    }

    if (invertY && (heading == 0 || heading == 2)) {
      heading = turn(TurningDirection.INVERT, heading);
    }

    return switch (heading) {
      case HEADING_NORTH -> curr.translate(0, units);
      case HEADING_EAST -> curr.translate(units, 0);
      case HEADING_SOUTH -> curr.translate(0, -units);
      case HEADING_WEST -> curr.translate(-units, 0);
      default -> throw new IllegalArgumentException();
    };
  }

  public static int parseHeading(char headingChar) {
    return switch (headingChar) {
      case '^' -> HEADING_NORTH;
      case '>' -> HEADING_EAST;
      case 'v' -> HEADING_SOUTH;
      case '<' -> HEADING_WEST;
      default -> throw new IllegalArgumentException();
    };
  }
  
  public static char headingToChar(int heading) {
    return switch (heading) {
      case HEADING_NORTH -> '^';
      case HEADING_EAST -> '>';
      case HEADING_SOUTH -> 'v';
      case HEADING_WEST -> '<';
      default -> throw new IllegalArgumentException();
    };
  }

  public static List<Point2D> orthogonalNeighbors(Grid grid, Point2D pt) {
    return Arrays
        .stream(HEADINGS)
        .mapToObj(h -> move(pt, h))
        .filter(grid::isWithinBounds)
        .filter(newPt -> !grid.configuration().isBlocked(grid.get(newPt)))
        .collect(Collectors.toUnmodifiableList());
  }

  public static List<Point2D> orthogonalNeighbors(Grid grid, int x, int y) {
    return orthogonalNeighbors(grid, new Point2D(x, y));
  }

  public static void fillEnclaves(Grid grid) {
    var changed = new AtomicBoolean(false);

    do {
      changed.setPlain(false);

      grid.visit((x, y, value) -> {
        if (!grid.configuration().isEmptySpace(value)) {
          return GridVisitResult.CONTINUE;
        }

        if (orthogonalNeighbors(grid, x, y).size() == 1) {
          changed.setPlain(true);
          return new GridVisitResult.Mutate.MutateAndContinue(grid.configuration().wall());
        } else {
          return GridVisitResult.CONTINUE;
        }
      });
    } while (changed.getPlain());
  }
}
