package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.graph.intgraph.DataIntGraphNode;
import dev.linl33.adventofcode.lib.graph.intgraph.IdLayoutBuilder;
import dev.linl33.adventofcode.lib.graph.intgraph.IntGraph;
import dev.linl33.adventofcode.lib.graph.intgraph.IntGraphBuilder;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class Day7 extends AdventSolution2020<Integer, Integer> {
  private static final Luggage SHINY_GOLD = new Luggage("shiny gold", 0);

  public static void main(String[] args) {
    new Day7().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    var graph = buildGraph(reader);
    var shinyGold = graph.getNode(SHINY_GOLD).orElseThrow();

    // TODO: implement graph visitor

    var count = 0;
    for (DataIntGraphNode<Luggage> node : graph.getNodes()) {
      if (node == shinyGold) {
        continue;
      }

      if (graph.findPath(node, shinyGold).isPresent()) {
        count++;
      }
    }

    return count;
  }

  @Override
  public Integer part2(BufferedReader reader) {
    var graph = buildGraph(reader);
    var shinyGold = graph.getNode(SHINY_GOLD).orElseThrow();

    return countContainedBags(shinyGold, graph);
  }

  private static IntGraph<Luggage, DataIntGraphNode<Luggage>> buildGraph(BufferedReader reader) {
    var idLayoutBuilder = new IdLayoutBuilder<Luggage>().addField(Luggage::color);
    var builder = new IntGraphBuilder<Luggage>().withIdLayoutBuilder(idLayoutBuilder);

    reader
        .lines()
        .map(LuggageRule::parse)
        .forEach(rule -> {
          builder.addNode(rule.container);
          rule.content.forEach(luggage -> builder
              .addNode(luggage)
              .addEdge(rule.container, luggage, luggage.count)
          );
        });

    return builder.build();
  }

  private static int countContainedBags(DataIntGraphNode<Luggage> node,
                                        IntGraph<Luggage, DataIntGraphNode<Luggage>> graph) {
    int result = 0;

    for (DataIntGraphNode<Luggage> outNode : node.outNodes()) {
      result += graph.getCost(node, outNode) * (countContainedBags(outNode, graph) + 1);
    }

    return result;
  }

  private static record Luggage(String color, int count) {
    public static Luggage NO_OTHER_BAG = new Luggage("no other", 0);

    public static Luggage parse(String str) {
      if (str.startsWith("no")) {
        return NO_OTHER_BAG;
      }

      var parts = str.split(" ");

      if (parts.length == 4) {
        return new Luggage(parts[1] + " " + parts[2], Integer.parseInt(parts[0]));
      } else {
        return new Luggage(parts[0] + " " + parts[1], 0);
      }
    }
  }

  private static record LuggageRule(Luggage container, List<Luggage> content) {
    public static LuggageRule parse(String str) {
      var parts = str.split(" contain ");

      var container = Luggage.parse(parts[0]);

      var contentArr = parts[1].split(", ");
      var content = new ArrayList<Luggage>(contentArr.length);
      for (String innerLuggage : contentArr) {
        var parsed = Luggage.parse(innerLuggage);
        if (parsed != Luggage.NO_OTHER_BAG) {
          content.add(parsed);
        }
      }

      return new LuggageRule(container, content);
    }
  }
}
