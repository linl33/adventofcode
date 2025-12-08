package dev.linl33.adventofcode.year2025;

import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.point.Point3D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day8 extends AdventSolution2025<Long, Long> {
  static void main() {
    new Day8().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var boxes = new ArrayList<Point3D>();
    var circuits = new HashMap<Point3D, Integer>();

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];
      var sep = line.indexOf(',');
      var sep2 = line.indexOf(',', sep + 1);

      var x = Integer.parseInt(line, 0, sep, 10);
      var y = Integer.parseInt(line, sep + 1, sep2, 10);
      var z = Integer.parseInt(line, sep2 + 1, line.length(), 10);

      boxes.add(new Point3D(x, y, z));
    }

    for (var i = 0; i < boxes.size(); i++) {
      circuits.put(boxes.get(i), i);
    }

    var topK = new PriorityQueue<Point2D>(Comparator.comparingLong(pt -> {
      var box1 = boxes.get(pt.x());
      var box2 = boxes.get(pt.y());

      return Math.powExact((long) box1.x() - box2.x(), 2) + Math.powExact((long) box1.y() - box2.y(), 2) + Math.powExact((long) box1.z() - box2.z(), 2);
    }));

    for (var i = 0; i < boxes.size(); i++) {
      for (var y = i + 1; y < boxes.size(); y++) {
        topK.add(new Point2D(i, y));
      }
    }

    // TODO: test input needs 10
    // for (int i = 0; i < 10; i++) {
    for (int i = 0; i < 1000; i++) {
      var next = topK.poll();

      var left = boxes.get(next.x());
      var right = boxes.get(next.y());

      int leftCircuit = circuits.get(left);
      int rightCircuit = circuits.get(right);

      if (leftCircuit != rightCircuit) {
        var minCircuit = Math.min(leftCircuit, rightCircuit);
        var maxCircuit = minCircuit ^ leftCircuit ^ rightCircuit;
        circuits.replaceAll((k, v) -> v == maxCircuit ? minCircuit : v);
      }
    }

    return circuits
      .values()
      .stream()
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
      .values()
      .stream()
      .sorted(Comparator.reverseOrder())
      .limit(3)
      .mapToLong(i -> i)
      .reduce((a, b) -> a * b)
      .orElseThrow();
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var boxes = new ArrayList<Point3D>();
    var circuits = new HashMap<Point3D, Integer>();

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];
      var sep = line.indexOf(',');
      var sep2 = line.indexOf(',', sep + 1);

      var x = Integer.parseInt(line, 0, sep, 10);
      var y = Integer.parseInt(line, sep + 1, sep2, 10);
      var z = Integer.parseInt(line, sep2 + 1, line.length(), 10);

      boxes.add(new Point3D(x, y, z));
    }

    for (var i = 0; i < boxes.size(); i++) {
      circuits.put(boxes.get(i), i);
    }
    var totalCircuits = circuits.size();

    var topK = new PriorityQueue<Point2D>(Comparator.comparingLong(pt -> {
      var box1 = boxes.get(pt.x());
      var box2 = boxes.get(pt.y());

      return Math.powExact((long) box1.x() - box2.x(), 2) + Math.powExact((long) box1.y() - box2.y(), 2) + Math.powExact((long) box1.z() - box2.z(), 2);
    }));

    for (var i = 0; i < boxes.size(); i++) {
      for (var y = i + 1; y < boxes.size(); y++) {
        topK.add(new Point2D(i, y));
      }
    }

    while (true) {
      var next = topK.poll();

      var left = boxes.get(next.x());
      var right = boxes.get(next.y());

      int leftCircuit = circuits.get(left);
      int rightCircuit = circuits.get(right);

      if (leftCircuit != rightCircuit) {
        var minCircuit = Math.min(leftCircuit, rightCircuit);
        var maxCircuit = minCircuit ^ leftCircuit ^ rightCircuit;
        circuits.replaceAll((k, v) -> v == maxCircuit ? minCircuit : v);
        totalCircuits--;

        if (totalCircuits == 1) {
          return (long) left.x() * (long) right.x();
        }
      }
    }
  }
}
