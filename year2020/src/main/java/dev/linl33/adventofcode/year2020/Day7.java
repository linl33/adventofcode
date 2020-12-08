package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.graph.*;
import dev.linl33.adventofcode.lib.graph.intgraph.DataIntGraphNode;
import dev.linl33.adventofcode.lib.graph.intgraph.IdLayoutBuilder;
import dev.linl33.adventofcode.lib.graph.intgraph.IntGraphBuilder;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Day7 extends AdventSolution2020<Integer, Integer> {
  private static final String SHINY_GOLD_COLOR = "shiny gold";
  private static final Luggage SHINY_GOLD = new Luggage(SHINY_GOLD_COLOR, 0);

  public static void main(String[] args) {
    new Day7().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return solve(reader, Day7::buildIntGraph, SHINY_GOLD, Day7::countContainers);
  }

  public int part1ByMutableGraph(BufferedReader reader) {
    return solve(reader, Day7::buildMutableGraph, SHINY_GOLD_COLOR, Day7::countContainers);
  }

  @Override
  public Integer part2(BufferedReader reader) {
    return solve(reader, Day7::buildIntGraph, SHINY_GOLD, Day7::countContainedBags);
  }

  public int part2ByMutableGraph(BufferedReader reader) {
    return solve(reader, Day7::buildMutableGraph, SHINY_GOLD_COLOR, Day7::countContainedBags);
  }

  private static <TData, TNode extends GraphNode<TNode>> int solve(
      BufferedReader reader,
      Function<BufferedReader, Graph<TData, TNode>> graphFactory,
      TData shinyGoldNode,
      BiFunction<TNode, Graph<TData, TNode>, Integer> solver) {

    return graphFactory
        .andThen(graph -> solver.apply(graph.getNode(shinyGoldNode).orElseThrow(), graph))
        .apply(reader);
  }

  private static Graph<Luggage, DataIntGraphNode<Luggage>> buildIntGraph(BufferedReader reader) {
    var idLayoutBuilder = new IdLayoutBuilder<Luggage>().addField(Luggage::color);
    var builder = new IntGraphBuilder<Luggage>().withIdLayoutBuilder(idLayoutBuilder);

    return buildGraph(reader, builder);
  }

  private static Graph<String, MutableGraphNode> buildMutableGraph(BufferedReader reader) {
    var builder = new DelegatingGraphBuilder<>(new MutableGraphBuilder(), Luggage::color);
    return buildGraph(reader, builder);
  }

  private static <G> G buildGraph(BufferedReader reader,
                                  GraphBuilder<G, Luggage, ?> builder) {
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

  private static <TNode extends GraphNode<TNode>> int countContainedBags(TNode bag,
                                                                         Graph<?, TNode> graph) {
    int result = 0;

    for (var outNode : bag.outNodes()) {
      result += graph.getCost(bag, outNode) * (countContainedBags(outNode, graph) + 1);
    }

    return result;
  }

  private static <TNode extends GraphNode<TNode>> int countContainers(TNode bag, Graph<?, TNode> graph) {
    return (int) Arrays
        .stream(graph.getNodes())
        .filter(node -> node != bag)
        .filter(node -> graph.findPath(node, bag).isPresent())
        .count();
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
