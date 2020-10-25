package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.point.Point;
import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.util.*;
import dev.linl33.adventofcode.year2019.intcodevm.IntcodeVM;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;

public class Day15 extends AdventSolution2019<Integer, Integer> {
  private static final long BOT_RETURN_WALL = 0L;
  private static final long BOT_RETURN_EMPTY = 1L;
  private static final long BOT_RETURN_O2 = 2L;

  public static void main(String[] args) {
    var day15 = new Day15();

    day15.runAndPrintAll();
    day15.print(Day15::part2ByPathFinding, Day15::part2PrintMapping, day15.getPart2Resource());
  }

  @Override
  public Integer part1(BufferedReader reader) {
    var world = buildMap(reader, false);

    var start = new Point2D(0, 0);
    var end = findInMap(world, BOT_RETURN_O2);

    return GraphUtil
        .aStar(start, end, pt -> findNeighbors(pt, world), GraphUtil.manhattanDistHeuristic(end))
        .size();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    var world = buildMap(reader, true);

    var counter = 100;

    var frontier = new HashSet<Point2D>();
    frontier.add(findInMap(world, 2L));

    while (!frontier.isEmpty()) {
      var newFrontier = new HashSet<Point2D>(frontier.size() * 2);

      for (var point : frontier) {
        paintAdjacent(world, point, counter, newFrontier);
      }

      frontier = newFrontier;
      counter++;
    }

    return counter - 1 - 100;
  }

  public Integer part2ByPathFinding(BufferedReader reader) {
    var world = buildMap(reader, true);
    var end = findInMap(world, BOT_RETURN_O2);

    var fixedEndAStar = GraphUtil.adaptAStar(
        end,
        pt -> findNeighbors(pt, world),
        GraphUtil.manhattanDistHeuristic(end)
    );

    var toExplore = world
        .entrySet()
        .stream()
        .filter(entry -> entry.getValue() != null)
        .filter(entry -> !entry.getValue().equals(BOT_RETURN_WALL))
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());

    var max = 0;
    while (!toExplore.isEmpty()) {
      var next = toExplore.iterator().next();
      var nextPath = fixedEndAStar.apply(next);

      toExplore.removeAll(nextPath.keySet());
      toExplore.remove(next);

      var distance = nextPath.size();
      if (distance > max) {
        max = distance;
      }
    }

    return max;
  }

  private static void paintAdjacent(Map<Point2D, Long> world,
                                    Point2D point,
                                    long valueToPaint,
                                    Collection<Point2D> frontier) {
    for (var heading : GridUtil.HEADINGS) {
      var moved = GridUtil.move(point, heading);
      var currMapVal = world.get(moved);
      if (currMapVal != null && currMapVal > 0 && currMapVal < 100) {
        world.put(moved, valueToPaint);
        frontier.add(moved);
      }
    }
  }

  private static Map<Point2D, Long> buildMap(BufferedReader reader, boolean fullMap) {
    var vm = new IntcodeVM(reader);
    var world = new HashMap<Point2D, Long>();
    var checkpoints = new LinkedList<Checkpoint>();
    var checkpointPos = new HashMap<Point2D, Checkpoint>();
    var curr = Point.ORIGIN_2D;

    world.put(curr, 10L);

    while (true) {
      var reachableAdjacents = findAdjacentPoints(curr, world.keySet());

      if (reachableAdjacents.isEmpty()) {
        if (checkpoints.isEmpty()) {
          break;
        }

        var checkpoint = checkpoints.removeLast();
        curr = checkpoint.position();
        checkpointPos.remove(curr);

        vm = checkpoint.vmSnapshot();
      } else {
        if (reachableAdjacents.size() == 1) {
          checkpointPos.remove(curr);
        } else {
          if (!checkpointPos.containsKey(curr)) {
            var checkpoint = new Checkpoint(curr, vm.fork());
            checkpoints.addLast(checkpoint);
            checkpointPos.put(curr, checkpoint);
          }
        }

        var toExplore = reachableAdjacents.get(0);
        vm.getInput().add(toExplore.direction());
        vm.executeNonBlocking(IntcodeVM.ExecMode.STATEFUL);

        long returnVal = vm.getOutput().remove();
        world.put(toExplore.position(), returnVal);
        if (returnVal == BOT_RETURN_EMPTY) {
          curr = toExplore.position();
        } else if (returnVal == BOT_RETURN_O2) {
          if (!fullMap) {
            break;
          }
        }
      }
    }

    // TODO: implement debug logging
    print(world);

    return world;
  }

  private static ArrayList<Command> findAdjacentPoints(Point2D start, Set<Point2D> knownLocations) {
    var list = new ArrayList<Command>(4);

    var north = GridUtil.move(start, GridUtil.HEADING_NORTH);
    var south = GridUtil.move(start, GridUtil.HEADING_SOUTH);
    var west = GridUtil.move(start, GridUtil.HEADING_WEST);
    var east = GridUtil.move(start, GridUtil.HEADING_EAST);

    if (!knownLocations.contains(north)) {
      list.add(new Command(1L, north));
    }

    if (!knownLocations.contains(south)) {
      list.add(new Command(2L, south));
    }

    if (!knownLocations.contains(west)) {
      list.add(new Command(3L, west));
    }

    if (!knownLocations.contains(east)) {
      list.add(new Command(4L, east));
    }

    return list;
  }

  private static ArrayList<Point2D> findNeighbors(Point2D start, Map<Point2D, Long> world) {
    var list = new ArrayList<Point2D>(4);

    for (var heading : GridUtil.HEADINGS) {
      var possiblePos = GridUtil.move(start, heading);
      var mapVal = world.get(possiblePos);

      if (mapVal != null && (mapVal == BOT_RETURN_EMPTY || mapVal == BOT_RETURN_O2)) {
        list.add(possiblePos);
      }
    }

    return list;
  }

  private static Point2D findInMap(Map<Point2D, Long> world, Long toFind) {
    return world
        .entrySet()
        .stream()
        .filter(entry -> entry.getValue().equals(toFind))
        .findAny()
        .map(Map.Entry::getKey)
        .orElseThrow();
  }

  private static void print(Map<Point2D, Long> world) {
    PrintUtil.enhancedPrint(GeomUtil.mappingPointsToGrid(
        world,
        Day15::convertGrid,
        '\0',
        Character.class,
        true,
        true
    ));
  }

  private static char convertGrid(Long longRepresentation) {
    char toDraw;

    if (longRepresentation == null) {
      toDraw = ' ';
    } else if (longRepresentation == BOT_RETURN_WALL) {
      toDraw = PrintUtil.FULL_BLOCK;
    } else if (longRepresentation == BOT_RETURN_EMPTY) {
      toDraw = PrintUtil.LIGHT_SHADE;
    } else if (longRepresentation == BOT_RETURN_O2) {
      toDraw = 'X';
    } else if (longRepresentation == 10) {
      toDraw = PrintUtil.BLACK_CIRCLE;
    } else {
      toDraw = 'O';
    }

    return toDraw;
  }

  private static record Command(long direction, Point2D position) {}

  private static record Checkpoint(Point2D position,
                                   IntcodeVM vmSnapshot) {}
}
