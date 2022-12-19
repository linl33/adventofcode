package dev.linl33.adventofcode.year2022;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class Day19 extends AdventSolution2022<Integer, Integer> {
  private static final int PART_1_TIME = 24;
  private static final int PART_2_TIME = 32;

  public static void main(String[] args) {
    new Day19().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var blueprints = parseBlueprints(reader);

    var sum = 0;
    for (int i = 0; i < blueprints.length; i++) {
      sum += (i + 1) * collectGeodes(blueprints[i], PART_1_TIME);
    }

    return sum;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var blueprints = parseBlueprints(reader);

    var product = 1;
    for (int i = 0; i < Math.min(3, blueprints.length); i++) {
      var v = collectGeodes(blueprints[i], PART_2_TIME);
      product *= v;
    }

    return product;
  }

  private static BotRecipe[][] parseBlueprints(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var blueprints = new BotRecipe[lines.length][4];

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      var parts = line.split(" ");

      var blueprintBots = new BotRecipe[4];

      var oreBot = new BotRecipe(Integer.parseInt(parts[6]), 0, 0);
      var clayBot = new BotRecipe(Integer.parseInt(parts[12]), 0, 0);
      var obsidianBot = new BotRecipe(Integer.parseInt(parts[18]), Integer.parseInt(parts[21]), 0);
      var geodeBot = new BotRecipe(Integer.parseInt(parts[27]), 0, Integer.parseInt(parts[30]));

      blueprintBots[0] = oreBot;
      blueprintBots[1] = clayBot;
      blueprintBots[2] = obsidianBot;
      blueprintBots[3] = geodeBot;

      blueprints[i] = blueprintBots;
    }

    return blueprints;
  }

  private static int collectGeodes(BotRecipe[] blueprint, int t) {
    var start = new int[] { 0, 0, 0, 0, 1, 0, 0, 0 };
    var end = new int[0];

    var queue = new ArrayDeque<int[]>();
    queue.add(start);

    var nodes = new LinkedHashSet<int[]>();
    nodes.add(start);

    var time = new ArrayDeque<Integer>();
    time.add(1);

    var edges = new HashMap<int[], Map<int[], Integer>>();

    var resourceMax = new int[] {
        Math.max(blueprint[1].costOre, Math.max(blueprint[2].costOre, blueprint[3].costOre)) + 2,
        blueprint[2].costClay + 2,
        blueprint[3].costObsidian + 2,
        Integer.MAX_VALUE,
    };
    var botMax = new int[] {
        Math.max(blueprint[1].costOre, Math.max(blueprint[2].costOre, blueprint[3].costOre)),
        blueprint[2].costClay,
        blueprint[3].costObsidian,
        Integer.MAX_VALUE,
    };

    while (!queue.isEmpty()) {
      var curr = queue.remove();
      int currTime = time.remove();

      edges.putIfAbsent(curr, new HashMap<>(4));
      var outEdges = edges.get(curr);

      var added = false;
      if (currTime < t) {
        for (int i = 0; i < blueprint.length; i++) {
          if (curr[i + 4] >= botMax[i]) {
            continue;
          }
          if (curr[i] >= resourceMax[i]) {
            continue;
          }

          var botRecipe = blueprint[i];
          if (!botRecipe.isPossible(curr)) {
            continue;
          }

          var buildTime = botRecipe.buildTime(curr);
          var timeNext = currTime + buildTime;

          if (timeNext > t) {
            continue;
          }

          var next = new int[curr.length];
          System.arraycopy(curr, 0, next, 0, curr.length);

          for (int j = 0; j < 3; j++) {
            next[j] += next[j + 4] * buildTime;
          }

          botRecipe.build(next);
          if (i != 3) {
            next[i + 4]++;
          }

          if (!nodes.contains(next)) {
            nodes.add(next);
            queue.add(next);
            time.add(timeNext);
          } else {
            nodes.remove(next);
            nodes.add(next);
          }

          var expectedGeodes = i != 3 ? 0 : (t - timeNext + 1);
          outEdges.put(next, expectedGeodes);

          added = true;
        }
      }

      if (!added) {
        outEdges.put(end, 0);
      }
    }

    var total = new HashMap<int[], Integer>();
    total.put(start, 0);

    for (var node : nodes) {
      int nodeTotal = total.get(node);
      var outEdges = edges.get(node);

      for (var edge : outEdges.entrySet()) {
        int weight = edge.getValue();
        var newTotal = nodeTotal + weight;
        total.compute(edge.getKey(), (__, v) -> v == null ? newTotal : Math.max(v, newTotal));
      }
    }

    return total.get(end);
  }

  private record BotRecipe(int costOre, int costClay, int costObsidian) {
    private void build(int[] resources) {
      resources[0] -= costOre;
      resources[1] -= costClay;
      resources[2] -= costObsidian;
    }

    private boolean isPossible(int[] inventory) {
      return (costOre == 0 || inventory[4] > 0) && (costClay == 0 || inventory[5] > 0) && (costObsidian == 0 || inventory[6] > 0);
    }

    private int buildTime(int[] resources) {
      var missingOre = costOre - resources[0];
      var missingClay = costClay - resources[1];
      var missingObsidian = costObsidian - resources[2];

      var timeOre = missingOre > 0 ? Math.ceilDiv(missingOre, resources[4]) + 1 : 1;
      var timeClay = missingClay > 0 ? Math.ceilDiv(missingClay, resources[5]) + 1 : 1;
      var timeObsidian = missingObsidian > 0 ? Math.ceilDiv(missingObsidian, resources[6]) + 1 : 1;

      return Math.max(timeOre, Math.max(timeClay, timeObsidian));
    }
  }
}
