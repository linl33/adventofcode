package dev.linl33.adventofcode.year2023;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;

public class Day25 extends AdventSolution2023<Integer, Void> {
  public static void main(String[] args) {
    new Day25().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var edges = new HashMap<String, Set<String>>();

    for (String line : lines) {
      var node = line.substring(0, 3);
      var right = line.substring(5).split(" ");

      for (var rightNode : right) {
        edges.putIfAbsent(node, new HashSet<>());
        edges.putIfAbsent(rightNode, new HashSet<>());

        edges.get(node).add(rightNode);
        edges.get(rightNode).add(node);
      }
    }

    var dedupedEdges = new ArrayList<String[]>();

    System.out.println(edges);

//    System.out.println("graph my_graph {");
    for (String s : edges.keySet()) {
//      System.out.println(s);
    }
    for (var kv : edges.entrySet()) {
      for (String edge : kv.getValue()) {
//        System.out.println(STR."\{kv.getKey()} -- \{edge}");
        dedupedEdges.add(new String[] { kv.getKey(), edge });
      }
    }
//    System.out.println("}");

    dedupedEdges.forEach(Arrays::sort);
    var e = dedupedEdges.stream().map(arr -> STR."\{arr[0]} -- \{arr[1]}").distinct().sorted().toList();

    System.out.println("graph my_graph {");
    e.forEach(s -> System.out.println(s));
    System.out.println("}");

    edges.get("pzq").remove("rrz");
    edges.get("rrz").remove("pzq");
    edges.get("jtr").remove("mtq");
    edges.get("mtq").remove("jtr");
    edges.get("ddj").remove("znv");
    edges.get("znv").remove("ddj");

    var start = edges.keySet().iterator().next();
    var queue = new ArrayDeque<String>();
    var visited = new HashSet<String>();

    var reachable = 0;

    queue.add(start);
    while (!queue.isEmpty()) {
      var next = queue.remove();
      var added = visited.add(next);
      if (!added) {
        continue;
      }

      reachable++;

      queue.addAll(edges.get(next));
    }

    return reachable * (edges.keySet().size() - reachable);
  }

  @Override
  public Void part2(@NotNull BufferedReader reader) {
    throw new UnsupportedOperationException();
  }
}
