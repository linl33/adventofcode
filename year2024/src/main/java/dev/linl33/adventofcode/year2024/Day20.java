package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.stream.Stream;

public class Day20 extends AdventSolution2024<Integer, Integer> {
  public static void main() {
    new Day20().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    return countCheats(reader, 2, 100);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    return countCheats(reader, 20, 100);
  }

  private static int countCheats(BufferedReader reader, int cheatDurationMax, int advantageTarget) {
    var lines = reader.lines().toArray(String[]::new);
    var dim = lines.length;

    var start = Point2D.ORIGIN_2D;
    var end = Point2D.ORIGIN_2D;

    for (int y = 0; y < dim; y++) {
      for (int x = 0; x < dim; x++) {
        if (lines[y].charAt(x) == 'S') {
          start = new Point2D(x, y);
        } else if (lines[y].charAt(x) == 'E') {
          end = new Point2D(x, y);
        }
      }
    }

    var distance = new Point2D[(dim - 2) * (dim - 2) - (dim - 2) / 2 * (dim - 2)];
    var curr = start;
    var totalDistance = 0;
    var heading = Stream.of(new Point2D(0, -1), new Point2D(0, 1), new Point2D(-1, 0), new Point2D(1, 0))
      .map(start::translate)
      .filter(p -> lines[p.y()].charAt(p.x()) != '#')
      .findAny()
      .map((start.rotateAboutOrigin(Point2D.Rotation.R_180))::translate)
      .orElseThrow();

    do {
      distance[totalDistance++] = curr;

      var next = curr.translate(heading);
      if (lines[next.y()].charAt(next.x()) == '#') {
        var right = heading.rotateAboutOrigin(Point2D.Rotation.CW_90);
        next = curr.translate(right);

        if (lines[next.y()].charAt(next.x()) == '#') {
          var left = heading.rotateAboutOrigin(Point2D.Rotation.CCW_90);
          next = curr.translate(left);
          heading = left;
        } else {
          heading = right;
        }
      }

      curr = next;
    } while (!curr.equals(end));
    distance[totalDistance] = end;

    var count = 0;
    for (var i = 0; i < totalDistance - advantageTarget; i++) {
      var cheatStart = distance[i];

      for (int j = i + 1 + advantageTarget; j <= totalDistance; j++) {
        var cheatEnd = distance[j];

        var cheatDuration = cheatStart.manhattanDistance(cheatEnd);
        if (cheatDuration > cheatDurationMax) {
          continue;
        }

        var advantage = j - i - cheatDuration;
        if (advantage >= advantageTarget) {
          count++;
        }
      }
    }

    return count;
  }
}
