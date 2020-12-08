package dev.linl33.adventofcode.year2018;

import dev.linl33.adventofcode.lib.graph.MutableGraph;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Stream;

public class Day7 extends AdventSolution2018<String, Integer> {
  public static void main(String[] args) {
    new Day7().runAndPrintAll();
  }

  @Override
  public String part1(BufferedReader reader) {
    var graph = buildGraph(reader.lines());

    var sequence = new StringBuilder();
    while (!graph.isEmpty()) {
      nextNode(graph, Set.of()).ifPresent(node -> {
        sequence.append(node);
        graph.removeNode(node);
      });
    }

    return sequence.toString();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    return part2Internal(reader, false);
  }

  public Integer part2Internal(BufferedReader reader, boolean simple) {
    var graph = buildGraph(reader.lines());

    var t = -1;
    var workers = new ArrayList<Worker>();

    if (simple) {
      workers.add(new Worker());
      workers.add(new Worker());
    } else {
      workers.add(new Worker());
      workers.add(new Worker());
      workers.add(new Worker());
      workers.add(new Worker());
      workers.add(new Worker());
    }

    var queued = new HashSet<String>();
    while (!graph.isEmpty()) {
      for (var worker : workers) {
        var wTime = worker.getTime();

        if (wTime != null) {
          worker.setTime(wTime - 1);

          if (wTime == 1) {
            worker.getTask().run();
            worker.setTask(null);
            worker.setTime(null);
          }
        }

        if (worker.getTask() == null) {
          nextNode(graph, queued).ifPresent(node -> {
            queued.add(node);
            worker.setTask(() -> graph.removeNode(node));

            var time = node.charAt(0) - 64;
            if (!simple) {
              time += 60;
            }
            worker.setTime(time);
          });
        }
      }

      t++;
    }

    return t;
  }

  private static MutableGraph buildGraph(Stream<String> instructions) {
    var graph = new MutableGraph();

    instructions
        .forEach(s -> {
          var nodeA = s.substring(5, 6);
          var nodeB = s.substring(36, 37);

          graph
              .addNode(nodeA)
              .addNode(nodeB)
              .addEdge(nodeA, nodeB);
        });

    return graph;
  }

  private static Optional<String> nextNode(MutableGraph graph, Set<String> excluded) {
    for (var node : graph.getNodes()) {
      if (excluded.contains(node.getLabel())) {
        continue;
      }

      if (node.inNodes().size() == 0) {
        return Optional.ofNullable(node.getLabel());
      }
    }

    return Optional.empty();
  }

  private static class Worker {
    private Integer time;
    private Runnable task;

    public Integer getTime() {
      return time;
    }

    public void setTime(Integer time) {
      this.time = time;
    }

    public Runnable getTask() {
      return task;
    }

    public void setTask(Runnable task) {
      this.task = task;
    }

    public Worker(Integer time, Runnable task) {
      this.time = time;
      this.task = task;
    }

    public Worker() {
      this(null, null);
    }
  }
}
