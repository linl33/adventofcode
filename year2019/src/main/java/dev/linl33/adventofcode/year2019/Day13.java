package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.point.Point3D;
import dev.linl33.adventofcode.year2019.intcodevm.IntcodeVM;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingDeque;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;

public class Day13 extends AdventSolution2019<Long, Long> {
  public static void main(String[] args) {
    new Day13().runAndPrintAll();
  }

  @Override
  public Long part1(BufferedReader reader) {
    var vm = new IntcodeVM(reader);
    vm.executeNonBlocking(IntcodeVM.ExecMode.STATEFUL);

    return countInQueue(vm.getOutput(), 2);
  }

  @Override
  public Long part2(BufferedReader reader) {
    var vm = new IntcodeVM(reader);
    vm.getMemory()[0] = 2;
    vm.executeNonBlocking(IntcodeVM.ExecMode.STATEFUL);

    while (!vm.executeNonBlocking(IntcodeVM.ExecMode.STATEFUL).hasHalted()) {
      var ball = findInQueue(vm.getOutput(), Day13::isBall, null, Point3D::x);
      var paddle = findInQueue(vm.getOutput(), Day13::isPaddle, null, Point3D::x);

      vm.getInput().add((long) Long.compare(ball, paddle));
    }

    return findInQueue(vm.getOutput(), null, Day13::isScore, Point3D::z);
  }

  private static long countInQueue(BlockingDeque<Long> queue, long valueToCount) {
    var map = new HashMap<Point2D, Long>();

    Iterator<Long> it = queue.iterator();
    while (it.hasNext()) {
      var xCoord = it.next();
      var yCoord = it.next();
      var value = it.next();

      map.put(new Point2D(xCoord.intValue(), yCoord.intValue()), value);
    }

    return map.values().stream().filter(value -> value == valueToCount).count();
  }

  private static long findInQueue(BlockingDeque<Long> queue,
                                  Predicate<Long> valPredicate,
                                  BiPredicate<Long, Long> coordPredicate,
                                  ToLongFunction<Point3D> toLongFunction) {
    if (valPredicate == null) {
      valPredicate = v -> true;
    }

    if (coordPredicate == null) {
      coordPredicate = (x, y) -> true;
    }

    var it = queue.descendingIterator();

    while (it.hasNext()) {
      var val = it.next();
      var yCoord = it.next();
      var xCoord = it.next();

      if (valPredicate.test(val) && coordPredicate.test(xCoord, yCoord)) {
        return toLongFunction.applyAsLong(new Point3D(xCoord.intValue(), yCoord.intValue(), val.intValue()));
      }
    }

    throw new NoSuchElementException();
  }

  private static boolean isBall(long value) {
    return value == 4L;
  }

  private static boolean isPaddle(long value) {
    return value == 3L;
  }

  private static boolean isScore(long x, long y) {
    return x == -1L && y == 0L;
  }
}
