package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.util.GeomUtil;
import dev.linl33.adventofcode.lib.util.GridUtil;
import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.util.PrintUtil;
import dev.linl33.adventofcode.year2019.intcodevm.IntcodeVM;

import java.io.BufferedReader;
import java.util.HashMap;

public class Day11 extends AdventSolution2019<Integer, Long[][]> {
  public static void main(String[] args) {
    var day11 = new Day11();

    day11.runAndPrintAll();
    day11.print(Day11::part1a, Day11::part2PrintMapping, day11.getPart1Resource());
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return paintHull(reader, 0L).size();
  }

  public Long[][] part1a(BufferedReader reader) {
    return GeomUtil.pointsToGrid(paintHull(reader, 0L), Long.class, false, true);
  }

  @Override
  public Long[][] part2(BufferedReader reader) {
    return GeomUtil.pointsToGrid(paintHull(reader, 1L), Long.class, false, true);
  }

  private static HashMap<Point2D, Long> paintHull(BufferedReader reader, long bgColor) {
    var vm = new IntcodeVM(reader);
    var vmInput = vm.getInput();
    var vmOutput = vm.getOutput();

    var color = new HashMap<Point2D, Long>();
    var heading = 0;
    var pos = new Point2D(0, 0);

    while (!vm.hasHalted()) {
      vmInput.addLast(color.getOrDefault(pos, bgColor));
      vm.executeNonBlocking(IntcodeVM.ExecMode.STATEFUL);

      while (vmOutput.size() > 0) {
        var turningDirection = vmOutput.removeLast() == 0 ?
            GridUtil.TurningDirection.LEFT_90 : GridUtil.TurningDirection.RIGHT_90;
        color.put(pos, vmOutput.removeLast());

        heading = GridUtil.turn(turningDirection, heading);
        pos = GridUtil.move(pos, heading);
      }
    }
    return color;
  }

  @Override
  public char[][] part2PrintMapping(Long[][] arr) {
    var draw = new char[arr.length][arr[0].length];

    for (var i = 0; i < arr.length; i++) {
      for (var j = 0; j < arr[i].length; j++) {
        if (arr[i][j] != null) {
          draw[i][j] = arr[i][j] == 1L ? PrintUtil.FULL_BLOCK : PrintUtil.LIGHT_SHADE;
        } else {
          draw[i][j] = ' ';
        }
      }
    }

    return draw;
  }
}
