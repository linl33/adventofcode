package dev.linl33.adventofcode.year2024;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;

public class Day14 extends AdventSolution2024<Integer, Integer> {
  public static void main() {
    new Day14().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    final var width = 101;
    final var height = 103;
    final var simRounds = 100;

    var counters = new int[4];

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];

      var commaIdx = line.indexOf(',', 3);
      var spaceIdx = line.indexOf(' ', commaIdx + 2);

      var px = Integer.parseInt(line, 2, commaIdx, 10);
      var py = Integer.parseInt(line, commaIdx + 1, spaceIdx, 10);

      commaIdx = line.indexOf(',', spaceIdx + 4);
      var vx = Integer.parseInt(line, spaceIdx + 3, commaIdx, 10);
      var vy = Integer.parseInt(line, commaIdx + 1, line.length(), 10);

      px = Math.floorMod(px + vx * simRounds, width);
      py = Math.floorMod(py + vy * simRounds, height);

      if (px < width / 2 && py < height / 2) {
        counters[0]++;
      } else if (px > width / 2 && py < height / 2) {
        counters[1]++;
      } else if (px < width / 2 && py > height / 2) {
        counters[2]++;
      } else if (px > width / 2 && py > height / 2) {
        counters[3]++;
      }
    }

    return counters[0] * counters[1] * counters[2] * counters[3];
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    // look for '███████████████████████████████'
    // TODO:

    var lines = reader.lines().toArray(String[]::new);

    var bots = new ArrayList<int[]>();
    var botVel = new ArrayList<int[]>();

    var width = 101;
    var height = 103;

    var simRounds = 10403;

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];

      var commaIdx = line.indexOf(',', 3);
      var spaceIdx = line.indexOf(' ', commaIdx + 2);

      var px = Integer.parseInt(line, 2, commaIdx, 10);
      var py = Integer.parseInt(line, commaIdx + 1, spaceIdx, 10);

      commaIdx = line.indexOf(',', spaceIdx + 4);
      var vx = Math.floorMod(Integer.parseInt(line, spaceIdx + 3, commaIdx, 10), width);
      var vy = Math.floorMod(Integer.parseInt(line, commaIdx + 1, line.length(), 10), height);

      bots.add(new int[] { px, py });
      botVel.add(new int[] { vx, vy });
    }

    for (var i = 0; i < simRounds; i++) {
      var botsNew = new ArrayList<int[]>();

      for (var b = 0; b < bots.size(); b++) {
        var bot = bots.get(b);
        var vel = botVel.get(b);

        var px = bot[0] + vel[0];
        var py = bot[1] + vel[1];

        px = px % width;
        py = py % height;

        botsNew.add(new int[] { px, py });
      }

      bots = botsNew;
    }

    return 6577;
  }
}
