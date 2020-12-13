package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.point.Point;
import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.util.GridUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day12 extends AdventSolution2020<Integer, Integer> {
  public static void main(String[] args) {
    new Day12().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    return solve(reader, new WaypointNavInstrVisitor(new Point2D(1, 0)) {
      @Override
      public @NotNull Point2D visit(@NotNull Point2D ferry, @NotNull DirNavInstr instr) {
        return GridUtil.move(ferry, instr.heading(), instr.value, false, false);
      }
    });
  }

  @Override
  public Integer part2(BufferedReader reader) throws Exception {
    return solve(reader, new WaypointNavInstrVisitor(new Point2D(10, 1)) {
      @Override
      public @NotNull Point2D visit(@NotNull Point2D ferry, @NotNull DirNavInstr instr) {
        setWaypoint(GridUtil.move(getWaypoint(), instr.heading(), instr.value, false, false));

        return ferry;
      }
    });
  }

  private static int solve(BufferedReader reader, NavInstrVisitor visitor) {
    return reader
        .lines()
        .map(NavInstr::parse)
        .reduce(
            new Point2D(0, 0),
            visitor::visit,
            (first, second) -> first
        )
        .manhattanDistance(Point.ORIGIN_2D);
  }

  private enum NavAction {
    NORTH, SOUTH, EAST, WEST, LEFT, RIGHT, FORWARD;

    public static NavAction parse(char navActionChar) {
      return switch (navActionChar) {
        case 'N' -> NORTH;
        case 'S' -> SOUTH;
        case 'E' -> EAST;
        case 'W' -> WEST;
        case 'L' -> LEFT;
        case 'R' -> RIGHT;
        case 'F' -> FORWARD;
        default -> throw new IllegalArgumentException();
      };
    }
  }

  private interface NavInstr {
    int value();

    @NotNull
    Point2D accept(@NotNull Point2D ferry, @NotNull NavInstrVisitor visitor);

    @NotNull
    static NavInstr parse(@NotNull String str) {
      var action = NavAction.parse(str.charAt(0));
      var value = Integer.parseInt(str, 1, str.length(), 10);

      return switch (action) {
        case NORTH, EAST, SOUTH, WEST -> new DirNavInstr(action, value);
        case LEFT, RIGHT -> new RotNavInstr(action, value);
        case FORWARD -> new ForwardNavInstr(value);
      };
    }
  }

  private static record RotNavInstr(NavAction action, int value) implements NavInstr {
    @Override
    public @NotNull Point2D accept(@NotNull Point2D ferry, @NotNull NavInstrVisitor visitor) {
      return visitor.visit(ferry, this);
    }
  }

  private static record DirNavInstr(NavAction action, int value) implements NavInstr {
    @Override
    public @NotNull Point2D accept(@NotNull Point2D ferry, @NotNull NavInstrVisitor visitor) {
      return visitor.visit(ferry, this);
    }

    @GridUtil.GridHeading
    public int heading() {
      return switch (action) {
        case NORTH -> GridUtil.HEADING_NORTH;
        case SOUTH -> GridUtil.HEADING_SOUTH;
        case EAST -> GridUtil.HEADING_EAST;
        case WEST -> GridUtil.HEADING_WEST;
        default -> throw new IllegalArgumentException();
      };
    }
  }

  private static record ForwardNavInstr(int value) implements NavInstr {
    @Override
    public @NotNull Point2D accept(@NotNull Point2D ferry, @NotNull NavInstrVisitor visitor) {
      return visitor.visit(ferry, this);
    }
  }

  private interface NavInstrVisitor {
    @NotNull
    Point2D visit(@NotNull Point2D ferry, @NotNull RotNavInstr instr);
    @NotNull
    Point2D visit(@NotNull Point2D ferry, @NotNull DirNavInstr instr);
    @NotNull
    Point2D visit(@NotNull Point2D ferry, @NotNull ForwardNavInstr instr);

    default Point2D visit(@NotNull Point2D ferry, @NotNull NavInstr instr) {
      return instr.accept(ferry, this);
    }
  }

  private static abstract class WaypointNavInstrVisitor implements NavInstrVisitor {
    private Point2D waypoint;

    public Point2D getWaypoint() {
      return waypoint;
    }

    public void setWaypoint(Point2D waypoint) {
      this.waypoint = waypoint;
    }

    public WaypointNavInstrVisitor(Point2D waypoint) {
      this.waypoint = waypoint;
    }

    @Override
    public @NotNull Point2D visit(@NotNull Point2D ferry, @NotNull RotNavInstr instr) {
      waypoint = waypoint.rotateAboutOrigin(switch (instr.value) {
        case 90 -> instr.action == NavAction.LEFT ? Point2D.Rotation.CCW_90 : Point2D.Rotation.CW_90;
        case 270 -> instr.action == NavAction.LEFT ? Point2D.Rotation.CCW_270 : Point2D.Rotation.CW_270;
        case 180 -> Point2D.Rotation.R_180;
        default -> throw new IllegalArgumentException();
      });

      return ferry;
    }

    @Override
    public @NotNull Point2D visit(@NotNull Point2D ferry, @NotNull ForwardNavInstr instr) {
      return ferry.translate(instr.value * waypoint.x(), instr.value * waypoint.y());
    }
  }
}
