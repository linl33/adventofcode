package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.jmh.JmhBenchmarkOption;
import dev.linl33.adventofcode.lib.graph.GraphPath;
import dev.linl33.adventofcode.lib.graph.GraphUtil;
import dev.linl33.adventofcode.lib.point.Point3D;
import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day16 extends AdventSolution2022<Integer, Integer> {
  private static final int PART_1_TOTAL_TIME = 30;
  private static final int PART_2_TOTAL_TIME = PART_1_TOTAL_TIME - 4;

  public static void main(String[] args) {
//    new Day16().runAndPrintAll();

//    new Day16().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day16"));
//    new Day16().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day16test1"));
//    new Day16().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day16"));
//    new Day16().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day16test1"));

    new Day16().benchmark(JmhBenchmarkOption.PART_1, JmhBenchmarkOption.PROFILE);
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toList();

    var flowRates = new HashMap<String, Integer>();
    var tunnels = new HashMap<String, List<String>>();

    for (var line : lines) {
      var valveTunnels = line.split(";");
      var valveParts = valveTunnels[0].split(" ");
      var valve = valveParts[1];
      var valveRate = Integer.parseInt(valveParts[valveParts.length - 1], 5, valveParts[valveParts.length - 1].length(), 10);
      flowRates.put(valve, valveRate);

      var outEdges = Arrays.stream(valveTunnels[1].substring(23).split(",")).map(String::trim).toList();
      tunnels.put(valve, outEdges);
    }

    var functioningValves = flowRates.entrySet().stream().filter(entry -> entry.getValue() > 0).map(Map.Entry::getKey).toArray(String[]::new);
    var pathLengths = new HashMap<Integer, int[]>();
    var aaToAll = new int[functioningValves.length];
    pathLengths.put(functioningValves.length, aaToAll);
    for (int i = 0; i < functioningValves.length; i++) {
      int path = GraphUtil.aStar("AA", functioningValves[i], tunnels::get).map(GraphPath::length).orElseThrow();
      aaToAll[i] = path + 1;
    }
    for (int i = 0; i < functioningValves.length; i++) {
      var map = new int[functioningValves.length];
      pathLengths.put(i, map);

      for (int j = 0; j <= i; j++) {
        map[j] = pathLengths.get(j)[i];
      }

      for (int j = i + 1; j < functioningValves.length; j++) {
        int path = GraphUtil.aStar(functioningValves[i], functioningValves[j], tunnels::get).map(GraphPath::length).orElseThrow();
        map[j] = path + 1;
      }
    }

    var intFlowRates = new int[functioningValves.length];
    for (int i = 0; i < functioningValves.length; i++) {
      intFlowRates[i] = flowRates.get(functioningValves[i]);
    }

    var max = 0;

    var maxStackDepth = functioningValves.length * functioningValves.length;

    var pos = new int[maxStackDepth];
    pos[0] = functioningValves.length;

    var paths = new int[maxStackDepth];
    paths[0] = 1 << functioningValves.length;

    var totals = new int[maxStackDepth];
    totals[0] = 0;

    var remaining = new int[maxStackDepth];
    remaining[0] = PART_1_TOTAL_TIME;

    var stackPointer = 0;

    while (stackPointer >= 0) {
      var curr = pos[stackPointer];
      int path = paths[stackPointer];
      int currTotal = totals[stackPointer];
      int currRemaining = remaining[stackPointer];
      stackPointer--;

      var neighborCosts = pathLengths.get(curr);

      var hasNext = false;
      for (int i = 0, mask = 1; i < functioningValves.length; i++, mask <<= 1) {
        if ((path & mask) != 0) {
          continue;
        }

        int nextRemaining = currRemaining - neighborCosts[i];
        if (nextRemaining > 0) {
          hasNext = true;
          stackPointer++;

          pos[stackPointer] = i;
          paths[stackPointer] = (path | mask);

          var nextTotal = currTotal + intFlowRates[i] * nextRemaining;
          totals[stackPointer] = nextTotal;

          remaining[stackPointer] = nextRemaining;
        }
      }

      if (!hasNext) {
        max = Math.max(max, currTotal);
      }
    }

    var start = new Point3D(functioningValves.length, 0, 1 << functioningValves.length);
    var end = new Point3D(-1, PART_1_TOTAL_TIME + 1, -1);

    var q = new ArrayDeque<Point3D>();
    q.add(start);

    var nodes = new LinkedHashSet<Point3D>();
    nodes.add(start);

    var edges = new LinkedHashMap<Point3D, Map<Point3D, Integer>>();

    while (!q.isEmpty()) {
      var next = q.remove();
      edges.putIfAbsent(next, new LinkedHashMap<>());
//      System.out.println(next);

      var position = next.x();
      var time = next.y();
      var history = next.z();

      var added = false;
      for (int i = 0, mask = 1; i < pathLengths.get(position).length; i++, mask <<= 1) {
        if (i == position) {
          continue;
        }

        if ((history & mask) != 0) {
          continue;
        }

        int timeNext = pathLengths.get(position)[i] + time;
        if (timeNext >= PART_1_TOTAL_TIME) {
          continue;
        }

        added = true;
        var pt = new Point3D(i, timeNext, history | mask);
        if (!nodes.contains(pt)) {
          nodes.add(pt);
          q.add(pt);
        } else {
          nodes.remove(pt);
          nodes.add(pt);
        }

        var c = edges.get(next).put(pt, -(PART_1_TOTAL_TIME - timeNext) * intFlowRates[i]);
        assert c == null;
      }

      // enqueue end state
      if (!added) {
        edges.get(next).put(end, 0);
      }
    }

    nodes.add(end);
    edges.put(end, Map.of());
//    System.out.println(nodes.size());
//    System.out.println(edges.size());
//    System.out.println(v);

//    var tally = new HashMap<Point2D, Integer>();
//    for (var node : nodes) {
//      tally.compute(new Point2D(node.x(), node.y()), (__, v) -> v == null ? 1 : v + 1);
//    }
//    System.out.println(tally);
//
//    for (var entry : tally.entrySet()) {
////      if (entry.getValue() > 10 && entry.getValue() < 20) {
//      if (entry.getValue() > 500 && entry.getValue() < 600) {
//        System.out.println(entry.getKey());
//
//        for (Point3D node : nodes) {
//          if (node.x() == entry.getKey().x() && node.y() == entry.getKey().y()) {
//            System.out.println("x = " + node.x() + ", y = " + node.y() + ", z = " + Integer.toBinaryString(node.z()));
//          }
//        }
//        System.out.println();
//      }
//    }

    var distance = new HashMap<Point3D, Integer>();
    distance.put(start, 0);

    for (var node : nodes) {
      int nodeDistance = distance.get(node);
      var nodeEdges = edges.get(node);

      for (var edge : nodeEdges.entrySet()) {
        int weight = edge.getValue();
        var newDistance = nodeDistance + weight;
        distance.compute(edge.getKey(), (__, v) -> v == null ? newDistance : Math.min(v, newDistance));
      }
    }

//    System.out.println(distance);
//    System.out.println(distance.get(end));

    return -distance.get(end);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toList();

    var flowRates = new HashMap<String, Integer>();
    var tunnels = new HashMap<String, List<String>>();

    for (var line : lines) {
      var valveTunnels = line.split(";");
      var valveParts = valveTunnels[0].split(" ");
      var valve = valveParts[1];
      var valveRate = Integer.parseInt(valveParts[valveParts.length - 1], 5, valveParts[valveParts.length - 1].length(), 10);
      flowRates.put(valve, valveRate);

      var outEdges = Arrays.stream(valveTunnels[1].substring(23).split(",")).map(String::trim).toList();
      tunnels.put(valve, outEdges);
    }

    var workingValves = flowRates.entrySet().stream().filter(entry -> entry.getValue() > 0).map(Map.Entry::getKey).toArray(String[]::new);
    var pathLengths = new HashMap<String, Map<String, Integer>>();
    var aaToAll = new HashMap<String, Integer>();
    pathLengths.put("AA", aaToAll);
    for (var valve : workingValves) {
      GraphUtil.aStar("AA", valve, tunnels::get).ifPresent(p -> aaToAll.put(valve, p.length() + 1));
    }
    for (var valve : workingValves) {
      var map = new HashMap<String, Integer>();
      pathLengths.put(valve, map);

      for (var valve2 : workingValves) {
        if (valve.equals(valve2)) {
          continue;
        }

        GraphUtil.aStar(valve, valve2, tunnels::get).ifPresent(p -> map.put(valve2, p.length() + 1));
      }
    }
    System.out.println(Arrays.toString(workingValves));
    System.out.println(pathLengths);
    System.out.println(ZonedDateTime.now());

    var curr = "AA";
    var curr2 = "AA";
    var max = 0;

    Deque<String> stack = new ArrayDeque<>();
    stack.push(curr);

    Deque<List<String>> paths = new ArrayDeque<>();
    paths.push(new ArrayList<>(List.of("AA")));

    Deque<String> stack2 = new ArrayDeque<>();
//    queue2.add(curr);
    stack2.push(curr);

    Deque<List<String>> paths2 = new ArrayDeque<>();
//    paths2.add(new ArrayList<>(List.of("AA")));
    paths2.push(new ArrayList<>(List.of("AA")));

    while (!stack.isEmpty()) {
      curr = stack.pop();
      var path = paths.pop();

      curr2 = stack2.pop();
      var path2 = paths2.pop();

      var time = remainingTime(path, pathLengths, PART_2_TOTAL_TIME);
      var time2 = remainingTime(path2, pathLengths, PART_2_TOTAL_TIME);

      if (time >= 0 && time2 >= 0) {
        var total = calcTotal(path, pathLengths, flowRates, PART_2_TOTAL_TIME) + calcTotal(path2, pathLengths, flowRates, PART_2_TOTAL_TIME);
        max = Math.max(max, total);
//        System.out.println("t = " + time + " max = " + max + ": " + path + ", " + path2 + " queue: " + queue.size());
      } else {
        continue;
      }

      // all open
      if (path.size() - 1 + path2.size() - 1 == workingValves.length) {
        continue;
      }

      List<String> neighbors = new ArrayList<>(pathLengths.get(curr).keySet());
      String finalCurr = curr;
      var expected = neighbors.stream().collect(Collectors.toMap(Function.identity(), n -> (time - pathLengths.get(finalCurr).get(n)) * flowRates.get(n)));
      neighbors.sort(Comparator.<String, Integer>comparing(expected::get));
//      neighbors.sort(Comparator.<String, Integer>comparing(expected::get).reversed());
//      System.out.println(neighbors);

//      System.out.println(curr + ": " + neighbors);
      for (int n1 = 0; n1 < neighbors.size(); n1++) {
        String neighbor = neighbors.get(n1);
        if (path.contains(neighbor) || path2.contains(neighbor)) {
          continue;
        }

        var nextPath = new ArrayList<>(path);
        nextPath.add(neighbor);

        var neighbors2 = new ArrayList<>(pathLengths.get(curr2).keySet());
        String finalCurr2 = curr2;
        var expected2 = neighbors2.stream().collect(Collectors.toMap(Function.identity(), n -> (time2 - pathLengths.get(finalCurr2).get(n)) * flowRates.get(n)));
        neighbors2.sort(Comparator.comparing(expected2::get));
//        neighbors2.sort(Comparator.<String, Integer>comparing(expected2::get).reversed());

        var start = 0;
        if (curr.equals(curr2)) {
          start = n1 + 1;
        }

        for (int n2 = start; n2 < neighbors2.size(); n2++) {
          var neighbor2 = neighbors2.get(n2);
          if (neighbor.equals(neighbor2) || path.contains(neighbor2) || path2.contains(neighbor2)) {
            continue;
          }

          var nextPath2 = new ArrayList<>(path2);
          nextPath2.add(neighbor2);

          if (remainingTime(nextPath, pathLengths, PART_2_TOTAL_TIME) > 0
              && remainingTime(nextPath2, pathLengths, PART_2_TOTAL_TIME) > 0) {
            stack.push(neighbor);
            stack2.push(neighbor2);
            paths.push(nextPath);
            paths2.push(nextPath2);
          }
        }
      }
    }

    System.out.println(ZonedDateTime.now());
    return max;
  }

  private static int remainingTime(List<String> path, Map<String, Map<String, Integer>> weights, int totalTime) {
    var time = totalTime;
    var curr = path.get(0);
    for (int i = 1; i < path.size(); i++) {
      time -= weights.get(curr).get(path.get(i));
      curr = path.get(i);
    }

    return time;
  }

  private static int calcTotal(List<String> path, Map<String, Map<String, Integer>> weights, Map<String, Integer> flowRates, int totalTime) {
    var time = totalTime;
    var total = 0;
    var curr = path.get(0);
    for (int i = 1; i < path.size(); i++) {
      time -= weights.get(curr).get(path.get(i));
      curr = path.get(i);

      total += time * flowRates.get(curr);
    }
    return total;
  }
}
