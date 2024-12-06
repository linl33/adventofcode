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
    System.out.println(edges.size());

    var st = lines[0].substring(0, 3);
    var end = "";

    var deque = new ArrayDeque<String>();

    for (int n = 0; n < 1; n++) {
      System.out.println(n);
      var visited = new HashSet<String>();

      deque.push(st);
      while (!deque.isEmpty()) {
        var curr = deque.remove();
        var added = visited.add(curr);
        if (!added) {
          continue;
        }

        var neighbors = edges.get(curr);
        var unvisitedNeighbors = new HashSet<>(neighbors);
        unvisitedNeighbors.removeAll(visited);

        if (unvisitedNeighbors.isEmpty()) {
          end = curr;
//          System.out.println(curr);
//          System.out.println(visited.size());
          continue;
//          deque.clear();
//          break;
        }

        deque.addAll(unvisitedNeighbors);
//        var next = unvisitedNeighbors.iterator().next();
//        edges.get(curr).remove(next);
//        edges.get(next).remove(curr);
//        deque.push(next);
      }
    }

    var dedupedEdges = new ArrayList<String[]>();

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
//    var e = dedupedEdges.stream().map(arr -> STR."\{arr[0]} -- \{arr[1]}").distinct().sorted().toList();
    var e = dedupedEdges.stream().map(arr -> arr[0] + " -- " + arr[1]).distinct().sorted().toList();

    System.out.println("graph my_graph {");
    e.forEach(s -> System.out.println(s));
    System.out.println("}");

    edges.get("pzq").remove("rrz");
    edges.get("rrz").remove("pzq");
    edges.get("jtr").remove("mtq");
    edges.get("mtq").remove("jtr");
    edges.get("ddj").remove("znv");
    edges.get("znv").remove("ddj");

//    var start = edges.keySet().iterator().next();
    var start = lines[0].substring(0, 3);
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

    System.out.println(reachable);
    System.out.println(visited.contains("prr"));
    System.out.println(visited.contains("ggk"));
    return reachable * (edges.keySet().size() - reachable);
  }

  @Override
  public Void part2(@NotNull BufferedReader reader) {
    throw new UnsupportedOperationException();
  }
}
