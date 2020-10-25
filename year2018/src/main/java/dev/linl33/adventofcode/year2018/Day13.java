package dev.linl33.adventofcode.year2018;

import dev.linl33.adventofcode.lib.GridEntity;
import dev.linl33.adventofcode.lib.HasHeading;
import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.util.GridUtil;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day13 extends AdventSolution2018<Point2D, Point2D> {
  public static void main(String[] args) {
    new Day13().runAndPrintAll();
  }

  @Override
  public Point2D part1(BufferedReader reader) {
    var lines = reader.lines().collect(Collectors.toList());
    var height = lines.size();
    var width = lines.stream().mapToInt(String::length).max().orElseThrow();
    var grid = new char[height][width];
    var emptyMap = new char[height][width];

    var cartsSet = parseInput(lines, grid, emptyMap);

    Point2D crashPos = null;
    while (crashPos == null) {
      var newCarts = new TreeSet<Cart>();

      for (var cart : cartsSet) {
        var nextPos = GridUtil.move(cart, false, true);
        var nextPosChar = readGrid(nextPos, grid);

        if (isCart(nextPosChar)) {
          crashPos = nextPos;
          writeGrid(nextPos, 'X', grid);
        } else {
          var newCart = simulateCartNoCrash(cart, nextPos, nextPosChar);
          newCarts.add(newCart);
          writeGrid(nextPos, newCart.getHeadingAsChar(), grid);
        }

        copyGridToGrid(cart.getPosition(), emptyMap, grid);
      }

      cartsSet = newCarts;
    }

    return crashPos;
  }

  @Override
  public Point2D part2(BufferedReader reader) {
    var lines = reader.lines().collect(Collectors.toList());
    var height = lines.size();
    var width = lines.stream().mapToInt(String::length).max().orElseThrow();
    var grid = new char[height][width];
    var emptyMap = new char[height][width];

    var cartsSet = parseInput(lines, grid, emptyMap);

    var skipSet = new HashSet<Cart>();
    while (cartsSet.size() > 1) {
      var newCarts = new TreeSet<Cart>();
      skipSet.clear();

      for (var cart : cartsSet) {
        if (skipSet.contains(cart)) {
          continue;
        }

        var nextPos = GridUtil.move(cart, false, true);
        var nextPosChar = readGrid(nextPos, grid);

        if (isCart(nextPosChar)) {
          newCarts.removeIf(c -> c.getPosition().equals(nextPos));
          cartsSet.stream().filter(c -> c.getPosition().equals(nextPos)).forEach(skipSet::add);

          copyGridToGrid(nextPos, emptyMap, grid);
        } else {
          var newCart = simulateCartNoCrash(cart, nextPos, nextPosChar);
          newCarts.add(newCart);

          writeGrid(nextPos, newCart.getHeadingAsChar(), grid);
        }

        copyGridToGrid(cart.getPosition(), emptyMap, grid);
      }

      cartsSet = newCarts;
    }

    return cartsSet.first().getPosition();
  }

  @Override
  public Object part1PrintMapping(Point2D part1Result) {
    return resultPrintMapping(part1Result);
  }

  @Override
  public Object part2PrintMapping(Point2D part2Result) {
    return resultPrintMapping(part2Result);
  }

  private static String resultPrintMapping(Point2D result) {
    return result.x() + "," + result.y();
  }

  private static Cart simulateCartNoCrash(Cart cart, Point2D nextPos, char nextPosChar) {
    return switch (nextPosChar) {
      case '+' -> cart.passIntersection();

      case '/', '\\' -> {
        GridUtil.TurningDirection turningDirection;
        if (nextPosChar == '/') {
          if (cart.getHeading() == 1 || cart.getHeading() == 3) {
            turningDirection = GridUtil.TurningDirection.LEFT_90;
          } else {
            turningDirection = GridUtil.TurningDirection.RIGHT_90;
          }
        } else {
          if (cart.getHeading() == 1 || cart.getHeading() == 3) {
            turningDirection = GridUtil.TurningDirection.RIGHT_90;
          } else {
            turningDirection = GridUtil.TurningDirection.LEFT_90;
          }
        }

        yield new Cart(cart, nextPos, GridUtil.turn(turningDirection, cart.getHeading()));
      }

      case '-', '|' -> new Cart(cart, nextPos, cart.getHeading());

      default -> throw new IllegalArgumentException();
    };
  }

  private static TreeSet<Cart> parseInput(List<String> lines, char[][] grid, char[][] emptyMap) {
    var cartsSet = new TreeSet<Cart>();
    for (var y = 0; y < lines.size(); y++) {
      for (var x = 0; x < lines.get(y).length(); x++) {
        var curr = lines.get(y).charAt(x);
        grid[y][x] = curr;
        emptyMap[y][x] = curr;

        if (isCart(curr)) {
          var cartPos = new Point2D(x, y);
          cartsSet.add(new Cart(cartPos, GridUtil.parseHeading(curr), 0));

          if (curr == '>' || curr == '<') {
            emptyMap[y][x] = '-';
          } else {
            emptyMap[y][x] = '|';
          }
        }
      }
    }

    return cartsSet;
  }

  private static boolean isCart(char c) {
    return c == '>' || c == '<' || c =='^' || c == 'v';
  }

  private static char readGrid(Point2D pos, char[][] grid) {
    return grid[pos.y()][pos.x()];
  }

  private static void writeGrid(Point2D pos, char newVal, char[][] grid) {
    grid[pos.y()][pos.x()] = newVal;
  }

  private static void copyGridToGrid(Point2D pos, char[][] src, char[][] dest) {
    writeGrid(pos, readGrid(pos, src), dest);
  }

  private static record Cart(
      String id,
      Point2D position,
      int heading,
      int intersectionState
  ) implements GridEntity, HasHeading {
    @Override
    public String getId() {
      return id();
    }

    @Override
    public Point2D getPosition() {
      return position();
    }

    @Override
    public int getHeading() {
      return heading();
    }

    public Cart(Point2D position, int heading, int intersectionState) {
      this(position.toString(), position, heading, intersectionState);
    }

    public Cart(Cart other, Point2D position, int heading) {
      this(other.getId(), position, heading, other.intersectionState());
    }

    public Cart passIntersection() {
      GridUtil.TurningDirection turningDirection;
      if (intersectionState == 0) {
        turningDirection = GridUtil.TurningDirection.LEFT_90;
      } else if (intersectionState == 1) {
        turningDirection = GridUtil.TurningDirection.NULL;
      } else {
        turningDirection = GridUtil.TurningDirection.RIGHT_90;
      }

      return new Cart(
          getId(),
          GridUtil.move(getPosition(), getHeading(), false, true),
          GridUtil.turn(turningDirection, getHeading()),
          (intersectionState() + 1) % 3
      );
    }
  }
}
