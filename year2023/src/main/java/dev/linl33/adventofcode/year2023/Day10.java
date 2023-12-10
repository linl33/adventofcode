package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.graph.GraphPath;
import dev.linl33.adventofcode.lib.graph.GraphUtil;
import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;

public class Day10 extends AdventSolution2023<Integer, Integer> {
  public static void main(String[] args) {
    new Day10().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);

    var map = new HashMap<Point2D, Integer>();
    Point2D start = null;

    for (int y = 0; y < lines.length; y++) {
      var line = lines[y];

      for (int x = 0; x < line.length(); x++) {
        map.put(new Point2D(x, y), line.codePointAt(x));

        if (line.codePointAt(x) == 'S') {
          start = new Point2D(x, y);
        }
      }
    }

    var max = -1;
    for (var entry : map.entrySet()) {
      var pos = entry.getKey();
      var shape = entry.getValue();

      if (shape == 'S' || shape == '.') {
        continue;
      }

      var path = GraphUtil.aStar(
        pos,
        start,
        pt -> getNeighbors(map, pt)
      ).map(GraphPath::length).orElse(-1);

      max = Math.max(max, path);
    }

    System.out.println(max);

    return max;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);

    var map = new HashMap<Point2D, Integer>();
    Point2D start = null;

    for (int y = 0; y < lines.length; y++) {
      var line = lines[y];

      for (int x = 0; x < line.length(); x++) {
        map.put(new Point2D(x, y), line.codePointAt(x));

        if (line.codePointAt(x) == 'S') {
          start = new Point2D(x, y);
        }
      }
    }

    System.out.println(STR."pre \{map.values().stream().filter(i -> i != 'S' && i != '.').count()}");
    var cleanMap = new HashMap<>(map);

    for (var entry : map.entrySet()) {
      var pos = entry.getKey();
      var shape = entry.getValue();

      if (shape == 'S' || shape == '.') {
        continue;
      }

      var path = GraphUtil.aStar(
        pos,
        start,
        pt -> getNeighbors(map, pt)
      ).map(GraphPath::length).orElse(-1);

      if (path == -1) {
        System.out.println(STR."removed \{pos}");
        cleanMap.put(entry.getKey(), (int) '.');
      }
    }

    System.out.println(STR."post \{cleanMap.values().stream().filter(i -> i != 'S' && i != '.').count()}");

    var translation = new HashMap<Integer, int[][]>();
    translation.put((int) '.', new int[][] {
      new int[] { '.', '.', '.' },
      new int[] { '.', '.', '.' },
      new int[] { '.', '.', '.' },
    });

    translation.put((int) '-', new int[][] {
      new int[] { '.', '.', '.' },
      new int[] { '0', '0', '0' },
      new int[] { '.', '.', '.' },
    });

    translation.put((int) '|', new int[][] {
      new int[] { '.', '0', '.' },
      new int[] { '.', '0', '.' },
      new int[] { '.', '0', '.' },
    });

    translation.put((int) 'L', new int[][] {
      new int[] { '.', '0', '.' },
      new int[] { '.', '0', '0' },
      new int[] { '.', '.', '.' },
    });

    translation.put((int) 'J', new int[][] {
      new int[] { '.', '0', '.' },
      new int[] { '0', '0', '.' },
      new int[] { '.', '.', '.' },
    });

    translation.put((int) '7', new int[][] {
      new int[] { '.', '.', '.' },
      new int[] { '0', '0', '.' },
      new int[] { '.', '0', '.' },
    });

    translation.put((int) 'F', new int[][] {
      new int[] { '.', '.', '.' },
      new int[] { '.', '0', '0' },
      new int[] { '.', '0', '.' },
    });

    translation.put((int) 'S', new int[][] {
      new int[] { '0', '0', '0' },
      new int[] { '0', '0', '0' },
      new int[] { '0', '0', '0' },
    });

    var expandedMap = new int[(lines.length + 2) * 3][(lines[0].length() + 2) * 3];
    for (int[] row : expandedMap) {
      Arrays.fill(row, '.');
    }

    for (int y = 3; y < expandedMap.length - 3; y += 3) {
      for (int x = 3; x < expandedMap[0].length - 3; x += 3) {
        var point = new Point2D(x / 3 - 1, y / 3 - 1);
        var val = cleanMap.get(point);
        if (val == null) {
          System.out.println(point);
          throw new IllegalStateException();
        }

        var remapped = translation.get(val);
        if (remapped == null) {
          throw new IllegalStateException();
        }

        for (int subY = 0; subY < 3; subY++) {
          System.arraycopy(remapped[subY], 0, expandedMap[y + subY], x, 3);
        }
      }
    }

    for (int[] ints : expandedMap) {
      System.out.println(Arrays.stream(ints).mapToObj(i -> i == '.' ? " " : "0").collect(Collectors.joining()));
    }

    var outsideSet = new HashSet<Point2D>();
    floodFill(expandedMap, outsideSet, new Point2D(0, 0));

    var insideSet = new HashSet<Point2D>();
    for (int y = 0; y < expandedMap.length; y++) {
      for (int x = 0; x < expandedMap[y].length; x++) {
        var val = expandedMap[y][x];
        if (val == '0') {
          continue;
        }

        if (val != '.') {
          throw new IllegalStateException();
        }

        if (!outsideSet.contains(new Point2D(x, y))) {
          insideSet.add(new Point2D(x, y));
        }
      }
    }

    System.out.println(STR."total \{expandedMap.length * expandedMap[0].length} outside \{outsideSet.size()} inside \{insideSet.size()}");

    var insideSetValues = insideSet.stream().map(pt -> new Point2D(pt.x() / 3 - 1, pt.y() / 3 - 1)).distinct().toList();
    var count = 0;
    for (Point2D pt : insideSetValues) {
      var val = cleanMap.get(pt);
      if (val == null) {
        continue;
      }

      if (val == '.') {
        count++;
      }
    }

    System.out.println(count);

    return count;
  }

  private static void floodFill(int[][] map, Set<Point2D> filledSet, Point2D start) {
//    var added = filledSet.add(start);
//    if (!added) {
//      return;
//    }

    var stack = new ArrayDeque<Point2D>();
    stack.push(start);

    var height = map.length;
    var width = map[0].length;

    while (!stack.isEmpty()) {
      var next = stack.pop();
      var added = filledSet.add(next);
      if (!added) {
        continue;
      }

      for (int deltaY = -1; deltaY <= 1; deltaY++) {
        for (int deltaX = -1; deltaX <= 1; deltaX++) {
          if (deltaX == deltaY) {
            continue;
          }

          var x = next.x() + deltaX;
          var y = next.y() + deltaY;

          if (x < 0 || x >= width) {
            continue;
          }

          if (y < 0 || y >= height) {
            continue;
          }

          var neighborValue = map[y][x];
          if (neighborValue == '.') {
            stack.push(new Point2D(x, y));
          }
        }
      }
    }
  }
  
  private static List<Point2D> getNeighbors(Map<Point2D, Integer> map, Point2D pt) {
    var ptShape = map.get(pt);

    if (ptShape == null || ptShape == 'S' || ptShape == '.') {
      return List.of();
    }

    var north = pt.translate(0, -1);
    var south = pt.translate(0, 1);
    var east = pt.translate(1, 0);
    var west = pt.translate(-1, 0);

    var list = new ArrayList<Point2D>(2);

    if (ptShape == '|') {
      list.add(north);
      list.add(south);
    } else if (ptShape == '-') {
      list.add(east);
      list.add(west);
    } else if (ptShape == 'L') {
      // north, east
      list.add(north);
      list.add(east);
    } else if (ptShape == 'J') {
      // north, west
      list.add(north);
      list.add(west);
    } else if (ptShape == '7') {
      // south, west
      list.add(south);
      list.add(west);
    } else if (ptShape == 'F') {
      // south, east
      list.add(south);
      list.add(east);
    } else {
      throw new IllegalStateException();
    }

    if (list.contains(north)) {
      var val = map.get(north);
      if (val == null || (val != '|' && val != '7' && val != 'F' && val != 'S')) {
        list.remove(north);
      }
    }

    if (list.contains(south)) {
      var val = map.get(south);
      if (val == null || (val != '|' && val != 'L' && val != 'J' && val != 'S')) {
        list.remove(south);
      }
    }

    if (list.contains(east)) {
      var val = map.get(east);
      if (val == null || (val != '-' && val != 'J' && val != '7' && val != 'S')) {
        list.remove(east);
      }
    }

    if (list.contains(west)) {
      var val = map.get(west);
      if (val == null || (val != '-' && val != 'L' && val != 'F' && val != 'S')) {
        list.remove(west);
      }
    }

    return list;
  }
}
