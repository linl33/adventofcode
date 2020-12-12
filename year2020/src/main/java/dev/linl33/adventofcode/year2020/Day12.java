package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.point.Point;
import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.util.GridUtil;

import java.io.BufferedReader;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 extends AdventSolution2020<Integer, Integer> {
  public static void main(String[] args) {
    new Day12().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    return solve(
        reader,
        stream -> {
          var input = stream.collect(Collectors.toList());

          var heading = GridUtil.HEADING_EAST;
          var ferry = new Point2D(0, 0);

          for (var instr : input) {
            switch (instr.action) {
              case LEFT, RIGHT -> heading = switch (instr.value) {
                case 90 -> GridUtil.turn(
                    instr.action == NavAction.LEFT ?
                        GridUtil.TurningDirection.LEFT_90 :
                        GridUtil.TurningDirection.RIGHT_90,
                    heading
                );
                case 270 -> GridUtil.turn(
                    instr.action == NavAction.LEFT ?
                        GridUtil.TurningDirection.LEFT_270 :
                        GridUtil.TurningDirection.RIGHT_270,
                    heading
                );
                case 180 -> GridUtil.turn(GridUtil.TurningDirection.INVERT, heading);
                default -> throw new IllegalArgumentException();
              };

              case NORTH, SOUTH, EAST, WEST, FORWARD -> ferry = GridUtil.move(
                  ferry,
                  switch (instr.action) {
                    case NORTH -> GridUtil.HEADING_NORTH;
                    case SOUTH -> GridUtil.HEADING_SOUTH;
                    case EAST -> GridUtil.HEADING_EAST;
                    case WEST -> GridUtil.HEADING_WEST;
                    case FORWARD -> heading;
                    default -> throw new IllegalStateException();
                  },
                  instr.value,
                  false,
                  false
              );
            }
          }

          return ferry;
        }
    );
  }

  @Override
  public Integer part2(BufferedReader reader) throws Exception {
    return solve(
        reader,
        stream -> {
          var input = stream.collect(Collectors.toList());

          var ferry = new Point2D(0, 0);
          var wp = new Point2D(10, 1);

          for (var instr : input) {
            switch (instr.action) {
              case LEFT, RIGHT -> wp = wp.rotateAboutOrigin(switch (instr.value) {
                case 90 -> instr.action == NavAction.LEFT ? Point2D.Rotation.CCW_90 : Point2D.Rotation.CW_90;
                case 270 -> instr.action == NavAction.LEFT ? Point2D.Rotation.CCW_270 : Point2D.Rotation.CW_270;
                case 180 -> Point2D.Rotation.R_180;
                default -> throw new IllegalArgumentException();
              });

              case NORTH, SOUTH, EAST, WEST -> wp = GridUtil.move(
                  wp,
                  switch (instr.action) {
                    case NORTH -> GridUtil.HEADING_NORTH;
                    case SOUTH -> GridUtil.HEADING_SOUTH;
                    case EAST -> GridUtil.HEADING_EAST;
                    case WEST -> GridUtil.HEADING_WEST;
                    default -> throw new IllegalArgumentException();
                  },
                  instr.value,
                  false,
                  false
              );

              case FORWARD -> ferry = ferry.translate(instr.value * wp.x(), instr.value * wp.y());
            }
          }

          return ferry;
        }
    );
  }

  private static int solve(BufferedReader reader,
                           Function<Stream<NavInstr>, Point2D> applyNavInstr) {
    var input = reader
        .lines()
        .map(NavInstr::new);

    return applyNavInstr.apply(input).manhattanDistance(Point.ORIGIN_2D);
  }

  private static record NavInstr(NavAction action, int value) {
    public NavInstr(String str) {
      this(NavAction.parse(str.charAt(0)), Integer.parseInt(str, 1, str.length(), 10));
    }
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
}
