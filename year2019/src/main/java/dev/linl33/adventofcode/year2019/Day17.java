package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.util.GeomUtil;
import dev.linl33.adventofcode.lib.util.GridUtil;
import dev.linl33.adventofcode.lib.util.PrintUtil;
import dev.linl33.adventofcode.year2019.intcodevm.IntcodeUtil;
import dev.linl33.adventofcode.year2019.intcodevm.IntcodeVM;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day17 extends AdventSolution2019<Integer, Long> {
  public static void main(String[] args) {
    new Day17().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    var scaffolding = buildScaffolding(reader);

    var alignmentParam = 0;
    for (Point2D point : scaffolding.keySet()) {
      if (isIntersection(point, scaffolding.keySet())) {
        alignmentParam += point.x() * point.y();
      }
    }

    return alignmentParam;
  }

  @Override
  public Long part2(BufferedReader reader) throws Exception {
    var asciiProgram = IntcodeUtil.buildMemory(reader);
    var asciiVacuumProgram = Arrays.copyOf(asciiProgram, asciiProgram.length);
    asciiVacuumProgram[0] = 2;

    var scaffolding = buildScaffolding(asciiProgram);

    PrintUtil.enhancedPrint(GeomUtil.mappingPointsToGrid(
        scaffolding,
        Function.identity(),
        '.',
        Character.class,
        false,
        false
    ));

    var robot = scaffolding
        .entrySet()
        .stream()
        .filter(entry -> entry.getValue() != '#')
        .findAny()
        .orElseThrow(IllegalArgumentException::new);

    @GridUtil.GridHeading
    var heading = GridUtil.parseHeading(robot.getValue());
    var position = robot.getKey();
    var path = new StringJoiner(",");

    var toVisit = new HashSet<>(scaffolding.keySet());
    toVisit.remove(robot.getKey());
    var straightPathCounter = 0;
    while (!toVisit.isEmpty()) {
      var newPosition = GridUtil.move(position, heading, false, true);

      if (scaffolding.containsKey(newPosition)) {
        straightPathCounter++;
        position = newPosition;
        toVisit.remove(newPosition);
      } else {
        var turned = false;

        for (var d : new GridUtil.TurningDirection[] {
            GridUtil.TurningDirection.LEFT_90,
            GridUtil.TurningDirection.RIGHT_90
        }) {
          var afterTurnHeading = GridUtil.turn(d, heading);
          var afterTurn = GridUtil.move(position, afterTurnHeading, false, true);

          if (scaffolding.containsKey(afterTurn)) {
            if (straightPathCounter > 0) {
              path.add(Integer.toString(straightPathCounter + 1));
              straightPathCounter = 0;
            }

            path.add(directionToString(d));
            heading = afterTurnHeading;
            position = afterTurn;
            turned = true;

            toVisit.remove(afterTurn);
            break;
          }
        }

        if (!turned) {
          throw new IllegalStateException();
        }
      }
    }

    path.add(Integer.toString(straightPathCounter + 1));

    var vacuumVm = new IntcodeVM(asciiVacuumProgram);
    var inputQueue = vacuumVm.getInput();

    var movementLogic = findMovementLogic(path.toString());
    feedInput(movementLogic.routine(), inputQueue);
    feedInput(movementLogic.funcA(), inputQueue);
    feedInput(movementLogic.funcB(), inputQueue);
    feedInput(movementLogic.funcC(), inputQueue);
    feedInput("n", inputQueue);

    return vacuumVm
        .executeNonBlocking(IntcodeVM.ExecMode.STATEFUL)
        .getOutput()
        .getLast();
  }

  private static Map<Point2D, Character> buildScaffolding(long[] program) {
    var vm = new IntcodeVM(program);
    vm.executeNonBlocking(IntcodeVM.ExecMode.STATELESS);

    var x = 0;
    var y = 0;
    var scaffolding = new HashMap<Point2D, Character>();
    for (var output : vm.getOutput()) {
      if (output == 10L) {
        x = 0;
        y++;
        continue;
      }

      if (output != 46L) {
        scaffolding.put(new Point2D(x, y), (char) output.intValue());
      }
      x += 1;
    }

    return scaffolding;
  }

  private static Map<Point2D, Character> buildScaffolding(BufferedReader reader) {
    return buildScaffolding(IntcodeUtil.buildMemory(reader));
  }

  private static boolean isIntersection(Point2D point, Set<Point2D> scaffolding) {
    if (!scaffolding.contains(point)) {
      return false;
    }

    if (!scaffolding.contains(point.translate(1, 0))) {
      return false;
    }

    if (!scaffolding.contains(point.translate(-1, 0))) {
      return false;
    }

    if (!scaffolding.contains(point.translate(0, 1))) {
      return false;
    }

    return scaffolding.contains(point.translate(0, -1));
  }

  private static String directionToString(GridUtil.TurningDirection turningDirection) {
    return switch (turningDirection) {
      case LEFT_90 -> "L";
      case RIGHT_90 -> "R";
      default -> throw new IllegalArgumentException();
    };
  }

  private static String partialPath(String[] path, int from, int length) {
    return Arrays
        .stream(path)
        .skip(from * 2L)
        .limit(length * 2L)
        .collect(Collectors.joining(","));
  }

  private static MovementLogic findMovementLogic(String pathStr) {
    var pathArr = pathStr.split(",");

    for (int aLength = 1; aLength <= 5; aLength++) {
      for (int bLength = 1; bLength <= 5; bLength++) {
        for (int cLength = 1; cLength <= 5; cLength++) {
          var funcA = partialPath(pathArr, 0, aLength);
          if (funcA.length() > 20) {
            continue;
          }

          var pathWithoutA = pathStr.replaceAll(funcA + "[,]?", "");
          var funcB = partialPath(pathWithoutA.split(","), 0, bLength);
          if (funcB.length() > 20) {
            continue;
          }

          var pathWithoutAB = pathWithoutA.replaceAll(funcB + "[,]?", "");
          var funcC = partialPath(pathWithoutAB.split(","), 0, cLength);
          if (funcC.length() > 20) {
            continue;
          }

          if (pathWithoutAB.replaceAll(funcC + "[,]?", "").isEmpty()) {
            var routine = pathStr
                .replace(funcA, "A")
                .replace(funcB, "B")
                .replace(funcC, "C");

            return new MovementLogic(routine, funcA, funcB, funcC);
          }
        }
      }
    }

    throw new IllegalArgumentException();
  }

  private static void feedInput(String strInput, Queue<Long> inputQueue) {
    for (char c : strInput.toCharArray()) {
      inputQueue.add((long) c);
    }

    inputQueue.add((long) '\n');
  }

  private static record MovementLogic(String routine, String funcA, String funcB, String funcC) {}
}
