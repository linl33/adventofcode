package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.jmh.JmhBenchmarkOption;
import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.IntStream;

public class Day15 extends AdventSolution2022<Long, Long> {
  private static final int PART_2_UPPER_BOUND = 4_000_000;

  public static void main(String[] args) {
//    new Day15().runAndPrintAll();

//    new Day15().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day15"));
//    new Day15().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day15test1"));
//    new Day15().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day15"));
//    new Day15().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day15test1"));

    new Day15().benchmark(JmhBenchmarkOption.PART_2, JmhBenchmarkOption.PERF_PROFILE);
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    return part1Row(reader, 2_000_000);
  }

  public long part1Row(@NotNull BufferedReader reader, int targetRow) {
    var lines = reader.lines().toList();
    var sensorCount = lines.size();

    var sensors = new ArrayList<Point2D>(sensorCount);
    var beacons = new ArrayList<Point2D>(sensorCount);
    parseInput(lines, sensors, beacons);

    var ranges = new ArrayList<Range>(sensorCount);
    var sum = 0L;

    for (int i = 0; i < sensorCount; i++) {
      var sensor = sensors.get(i);
      var beacon = beacons.get(i);

      var distance = sensor.manhattanDistance(beacon);

      var intersects = Math.abs(sensor.y() - targetRow) <= distance;
      if (!intersects) {
        continue;
      }

      var extent = distance - Math.abs(sensor.y() - targetRow);
      var extentLeft = sensor.x() - extent;
      var extentRight = sensor.x() + extent;

      new Range(extentLeft, extentRight).addToList(ranges);
    }

    var beaconSet = new HashSet<>(sensorCount);
    for (int i = 0; i < sensorCount; i++) {
      var beacon = beacons.get(i);
      if (beacon.y() == targetRow && beaconSet.add(beacon)) {
        sum--;
      }
    }

    for (var range : ranges) {
      sum += range.end() - range.start() + 1;
    }

    return sum;
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toList();
    var sensorCount = lines.size();

    var sensors = new ArrayList<Point2D>(sensorCount);
    var beacons = new ArrayList<Point2D>(sensorCount);
    parseInput(lines, sensors, beacons);

    for (int i = 0; i < sensors.size(); i++) {
      var s1 = sensors.get(i);
      var b1 = beacons.get(i);
      var d1 = s1.manhattanDistance(b1);

      for (int j = i + 1; j < sensors.size(); j++) {
        var s2 = sensors.get(j);
        var b2 = beacons.get(j);
        var d2 = s2.manhattanDistance(b2);

//        System.out.println(s1.manhattanDistance(s2) - (d1 + d2));

        if (s1.manhattanDistance(s2) == (2 + d1 + d2)) {
          System.out.println(s1 + ", " + s2);
          System.out.println("rise " + (s2.y() - s1.y()));
          System.out.println("run " + (s2.x() - s1.x()));
          System.out.println("d2 " + d2);
        }
      }
    }

    var rList = new ArrayList<Range>(sensorCount);
    var fullRow = new Range(0, PART_2_UPPER_BOUND);
    for (int y = 0; y <= PART_2_UPPER_BOUND; y++) {
      rList.clear();

      for (int s = 0; s < sensorCount; s++) {
        var sensor = sensors.get(s);
        var beacon = beacons.get(s);

        var distance = sensor.manhattanDistance(beacon);

        var intersects = Math.abs(sensor.y() - y) <= distance;
        if (!intersects) {
          continue;
        }

        var extent = distance - Math.abs(sensor.y() - y);
        var extentLeft = Math.max(0, sensor.x() - extent);
        var extentRight = Math.min(PART_2_UPPER_BOUND, sensor.x() + extent);

        new Range(extentLeft, extentRight).addToList(rList);

        if (rList.size() == 1 && rList.get(0).equals(fullRow)) {
//          System.out.println("Early exit, y = " + y + ", s = " + s);
          break;
        }
      }

      if (rList.size() > 1) {
//        System.out.println("y = " + y);
//        System.out.println(rList);

        var x = Math.max(rList.get(0).start, rList.get(1).start) - 1;

//        var pt = new Point2D(x, y);
//        for (int i = 0; i < sensors.size(); i++) {
//          Point2D sensor = sensors.get(i);
//          var b = beacons.get(i);
////          System.out.println(sensor + " " + (sensor.manhattanDistance(pt) - sensor.manhattanDistance(b)));
//        }

        System.out.println("x = " + x);
        System.out.println("y = " + y);
        return Math.multiplyFull(x, 4_000_000) + y;
      }
    }

    throw new IllegalStateException("Unreachable");
  }

  private static void parseInput(List<String> lines, List<Point2D> sensors, List<Point2D> beacons) {
    for (var line : lines) {
      var parts = line.split(" ");

      var sensorX = Integer.parseInt(parts[2], 2, parts[2].length() - 1, 10);
      var sensorY = Integer.parseInt(parts[3], 2, parts[3].length() - 1, 10);

      var beaconX = Integer.parseInt(parts[8], 2, parts[8].length() - 1, 10);
      var beaconY = Integer.parseInt(parts[9], 2, parts[9].length(), 10);

      var sensor = new Point2D(sensorX, sensorY);
      var beacon = new Point2D(beaconX, beaconY);

      sensors.add(sensor);
      beacons.add(beacon);
    }
  }

  private record Range(int start, int end) {
    private Range merge(Range other) {
      var min = Math.min(start, other.start);
      var max = Math.max(end, other.end);
      return new Range(min, max);
    }

    private boolean overlap(Range r2) {
      return Math.max(start, r2.start) <= Math.min(end, r2.end);
    }

    private void addToList(List<Range> list) {
      if (list.isEmpty()) {
        list.add(this);
        return;
      }

      var range = this;
      var it = list.listIterator();
      while (it.hasNext()) {
        var rangeNext = it.next();
        if (range.overlap(rangeNext)) {
          it.remove();
          range = range.merge(rangeNext);
        }
      }

      list.add(range);
    }
  }
}
