package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.grid.ArrayGrid;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import dev.linl33.adventofcode.lib.util.AdventUtil;
import dev.linl33.adventofcode.lib.util.PrintUtil;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Day20 extends AdventSolution2020<Long, Integer> {
  public static void main(String[] args) {
//    new Day20().runAndPrintAll();
    new Day20().print(SolutionPart.PART_2);
  }

  @Override
  public Long part1(BufferedReader reader) throws Exception {
    var edgeMap = AdventUtil
        .readInputGrouped(reader)
        .map(rTile -> {
          var tileLines = rTile.collect(Collectors.toList());
          var id = Integer.parseInt(tileLines.get(0), 5, 9, 10);

          var tileLineReader = new BufferedReader(new StringReader(tileLines.stream().skip(1).collect(Collectors.joining("\n"))));
          return new Tile(id, new RowArrayGrid(tileLineReader));
        })
        .collect(Collectors.toMap(
            Tile::id,
            Day20::makeEdges
        ));

    return edgeMap
        .entrySet()
        .stream()
        .filter(entry -> 2 == countMatches(entry.getValue(), edgeMap, entry.getKey()))
        .mapToLong(Map.Entry::getKey)
        .reduce((left, right) -> left * right)
        .orElseThrow();
  }

  @Override
  public Integer part2(BufferedReader reader) throws Exception {
    var rawTiles = AdventUtil.readInputGrouped(reader).collect(Collectors.toList());
    var tiles = new ArrayList<Tile>();

    for (Stream<String> rTile : rawTiles) {
      var tileLines = rTile.collect(Collectors.toList());
      var id = tileLines.get(0).split(" ")[1].substring(0, 4);

      var tileLineReader = new BufferedReader(new StringReader(tileLines.stream().skip(1).collect(Collectors.joining("\n"))));
      var grid = new RowArrayGrid(tileLineReader);
      tiles.add(new Tile(Integer.parseInt(id), grid));
    }
    var edgeMap = tiles.stream().collect(Collectors.toMap(
        Tile::id,
        Day20::makeEdges
    ));

    var tileMap = tiles.stream().collect(Collectors.toMap(
        Tile::id,
        Function.identity()
    ));

    int sideLength = (int) Math.sqrt(tiles.size());

    var tilesByConnectedEdges = edgeMap
        .entrySet()
        .stream()
        .collect(Collectors.groupingBy(
            entry -> countMatches(entry.getValue(), edgeMap, entry.getKey()),
            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
        ));
    var corner = tilesByConnectedEdges.get(2);
    var side = tilesByConnectedEdges.get(3);

    var cArr = corner.keySet().toArray(Integer[]::new);

    var outerEdge = traceOuterEdgeClockwise(sideLength);

    var newImage = new int[tiles.size()];

    var exclusion = new HashSet<Integer>();
    var count = 0;

    var cornerSides = new HashMap<>(side);
    cornerSides.putAll(corner);

    var start = cArr[0];
    var edge = edgeMap.get(start)[0];
    exclusion.add(cArr[0]);
    newImage[outerEdge[count++]] = start;
    while (exclusion.size() < cornerSides.size()) {
      var next = findFirstMatch(edge, cornerSides, start, exclusion);
      for (int i = 0; i < 4; i++) {
        var a = findAllMatches(edgeMap.get(next)[i], cornerSides, next, exclusion, new ArrayList<>());
        if (a > 0) {
          edge = edgeMap.get(next)[i];
          break;
        }
      }
      exclusion.add(next);
      newImage[outerEdge[count++]] = next;
      start = next;
    }

    PrintUtil.enhancedPrint(newImage);

    var remaining = new HashMap<>(edgeMap);
    remaining.keySet().removeAll(exclusion);

    for (int y = 1; y < sideLength - 1; y++) {
      for (int x = 1; x < sideLength - 1; x++) {
        if (newImage[y * sideLength + x] > 0) {
          continue;
        }

        var up = newImage[(y - 1) * sideLength + x];

        var output = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
          var a = findAllMatches(edgeMap.get(up)[i], remaining, up, exclusion, output);
        }

        if (output.size() != 1) {
          throw new IllegalStateException();
        }

        newImage[y * sideLength + x] = output.get(0);
        exclusion.add(output.get(0));
      }
    }

    PrintUtil.enhancedPrint(newImage);

    var transformation = new int[][]{
        {3, 2},
        {1},
        {1},
        {4},
        {3, 1},
        {4},
        {4},
        {2},
        {},
        {3, 2},
        {2},
        {4, 2},

        {4},
        {4},
        {3},
        {4},
        {2},
        {3, 2},
        {4},
        {},
        {1},
        {1},
        {4, 2},
        {4, 2},

        {4},
        {},
        {4, 2},
        {},
        {1},
        {4},
        {3},
        {2},
        {3, 2},
        {4},
        {1},
        {2},

        {1},
        {1},
        {4},
        {3},
        {2},
        {4},
        {4, 2},
        {},
        {},
        {},
        {},
        {4, 2},

        {4},
        {3, 2},
        {4},
        {4},
        {3, 2},
        {3, 2},
        {2},
        {4, 2},
        {4, 2},
        {3, 2},
        {1},
        {3},

        {3, 1},
        {3, 1},
        {1},
        {2},
        {1},
        {1},
        {},
        {4, 2},
        {4, 2},
        {4},
        {3},
        {2},

        {2},
        {4},
        {4},
        {3, 1},
        {3},
        {3, 1},
        {1},
        {},
        {4},
        {2},
        {2},
        {2},

        {2},
        {1},
        {3, 1},
        {4, 1},
        {},
        {3},
        {4},
        {2},
        {1},
        {},
        {},
        {},

        {},
        {4},
        {1},
        {3},
        {2},
        {4},
        {},
        {4},
        {4, 1},
        {2},
        {4},
        {3, 1},

        {2},
        {},
        {4, 1},
        {3, 1},
        {4},
        {2},
        {},
        {},
        {4, 1},
        {},
        {1},
        {4},

        {3, 1},
        {3},
        {3, 1},
        {4},
        {1},
        {3},
        {3},
        {},
        {3},
        {2},
        {1},
        {2},

        {2},
        {1},
        {4, 1},
        {2},
        {1},
        {},
        {3},
        {3},
        {4, 1},
        {4, 1},
        {3, 1},
        {4},
    };

    for (int i = 0; i < transformation.length; i++) {
      for (int t : transformation[i]) {
        switch (t) {
          case 1 -> tileMap.get(newImage[i]).grid.invertX();
          case 2 -> tileMap.get(newImage[i]).grid.invertY();
          case 3 -> tileMap.get(newImage[i]).grid.rotateClockwise();
          case 4 -> tileMap.get(newImage[i]).grid.rotateCounterClockwise();
          default -> throw new IllegalArgumentException();
        }
      }
    }

    var whole = new String[96];
    var counter = 0;

    var ylimit = 12;
    for (int y = 0; y < ylimit; y++) {
      for (int row = 0; row < 10; row++) {
        var sb = new StringBuilder();
        var sb2 = new StringBuilder();

        for (int col = 0; col < 12; col++) {
//          System.out.println(row);
          sb.append(new String(print(tileMap.get(newImage[y * 12 + col]).grid.row(row))));

          var otherStr = new String(print(tileMap.get(newImage[y * 12 + col]).grid.row(row)));
          otherStr = otherStr.substring(1, otherStr.length() - 1);
          sb2.append(otherStr);
          sb.append("|");
        }

        var out = sb.toString();
        System.out.println(out);

        for (Integer i : List.of(9, 20, 31, 42, 53, 64, 75, 86, 97, 108, 119)) {
          if (out.charAt(i) != out.charAt(i + 2)) {
            throw new IllegalStateException();
          }
        }

        if (row != 0 && row != 9) {
          whole[counter++] = sb2.toString();
        }
      }

      System.out.println("-".repeat(120 + 12));
    }

    var ops = (Consumer<ArrayGrid>[][]) new Consumer[][]{
//        {},
//        {(Consumer<ArrayGrid>) ArrayGrid::invertX},
//        {(Consumer<ArrayGrid>) ArrayGrid::invertY},
//        {(Consumer<ArrayGrid>) ArrayGrid::rotateClockwise},
//        {(Consumer<ArrayGrid>) ArrayGrid::rotateClockwise, (Consumer<ArrayGrid>) ArrayGrid::rotateClockwise}, // GOOD
//        {(Consumer<ArrayGrid>) ArrayGrid::rotateCounterClockwise},
        {(Consumer<ArrayGrid>) ArrayGrid::invertX, (Consumer<ArrayGrid>) ArrayGrid::invertY}, // GOOD
//        {(Consumer<ArrayGrid>) ArrayGrid::invertX, (Consumer<ArrayGrid>) ArrayGrid::rotateClockwise},
//        {(Consumer<ArrayGrid>) ArrayGrid::invertX, (Consumer<ArrayGrid>) ArrayGrid::rotateClockwise, (Consumer<ArrayGrid>) ArrayGrid::rotateClockwise},
//        {(Consumer<ArrayGrid>) ArrayGrid::invertX, (Consumer<ArrayGrid>) ArrayGrid::rotateCounterClockwise},
//        {(Consumer<ArrayGrid>) ArrayGrid::invertY, (Consumer<ArrayGrid>) ArrayGrid::rotateClockwise},
//        {(Consumer<ArrayGrid>) ArrayGrid::invertY, (Consumer<ArrayGrid>) ArrayGrid::rotateClockwise, (Consumer<ArrayGrid>) ArrayGrid::rotateClockwise},
//        {(Consumer<ArrayGrid>) ArrayGrid::invertY, (Consumer<ArrayGrid>) ArrayGrid::rotateCounterClockwise},
//        {(Consumer<ArrayGrid>) ArrayGrid::invertX, (Consumer<ArrayGrid>) ArrayGrid::invertY, (Consumer<ArrayGrid>) ArrayGrid::rotateClockwise},
//        {(Consumer<ArrayGrid>) ArrayGrid::invertX, (Consumer<ArrayGrid>) ArrayGrid::invertY, (Consumer<ArrayGrid>) ArrayGrid::rotateClockwise, (Consumer<ArrayGrid>) ArrayGrid::rotateClockwise},
//        {(Consumer<ArrayGrid>) ArrayGrid::invertX, (Consumer<ArrayGrid>) ArrayGrid::invertY, (Consumer<ArrayGrid>) ArrayGrid::rotateCounterClockwise},
    };

    var seaMonster = new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', ' '},
        {'#', ' ', ' ', ' ', ' ', '#', '#', ' ', ' ', ' ', ' ', '#', '#', ' ', ' ', ' ', ' ', '#', '#', '#'},
        {' ', '#', ' ', ' ', '#', ' ', ' ', '#', ' ', ' ', '#', ' ', ' ', '#', ' ', ' ', '#', ' ', ' ', ' '}
    };

    var seaMonsterHeight = seaMonster.length;
    var seaMonsterWidth = seaMonster[0].length;

    for (int i = 0; i < ops.length; i++) {
      System.out.println("---");
      var s2 = transform(whole, ops[i]);

      var seaMonsterCount = 0;

      for (int y = 0; y < s2.length - seaMonsterHeight; y++) {
        for (int x = 0; x < s2.length - seaMonsterWidth; x++) {
          if (hasSeaMonster(s2, x, y)) {
            seaMonsterCount++;
            x += (seaMonsterWidth - 1);
          }
        }
      }

      return 2444 - (seaMonsterCount * 15);
    }

    return -1;
  }

  private static record Tile(int id, ArrayGrid grid) {
  }

  private static int[] makeEdges(Tile t) {
    var top = t.grid.row(0);
    var bottom = t.grid.row(t.grid.height() - 1);

    var c = ((RowArrayGrid) t.grid).asColumnArrayGrid();
    var left = c.column(0);
    var right = c.column(c.width() - 1);

    if (top.length != right.length) {
      throw new IllegalStateException();
    }

    return new int[]{
        convertTo(top),
        convertTo(bottom),
        convertTo(left),
        convertTo(right),

        convertTo(flip(top)),
        convertTo(flip(bottom)),
        convertTo(flip(left)),
        convertTo(flip(right))
    };
  }

  private static int[] flip(int[] in) {
    var output = new int[in.length];

    for (int i = 0; i < in.length; i++) {
      output[in.length - i - 1] = in[i];
    }

    return output;
  }

  private static int countMatches(int[] edges, Map<Integer, int[]> tiles, int id) {
    var count = 0;

    for (var entry : tiles.entrySet()) {
      if (entry.getKey().equals(id)) {
        continue;
      }

      var m = Arrays.stream(edges).anyMatch(e -> arrCont(entry.getValue(), e));
      if (m) {
        count++;
      }
    }

    return count;
  }

  private static int findFirstMatch(int edges, Map<Integer, int[]> tiles, int id, Set<Integer> exclude) {
    for (var entry : tiles.entrySet()) {
      if (entry.getKey().equals(id)) {
        continue;
      }

      if (exclude.contains(entry.getKey())) {
        continue;
      }

      var m = Arrays.stream(entry.getValue()).anyMatch(i -> i == edges);

//      var m = Arrays.stream(edges).anyMatch(e -> arrCont(entry.getValue(), e));
      if (m) {
        return entry.getKey();
      }
    }

    throw new NoSuchElementException();
  }

  private static int findAllMatches(int edges, Map<Integer, int[]> tiles, int id, Set<Integer> exclude, ArrayList<Integer> output) {
    var count = 0;
//    var outut = new ArrayList<Integer>();

    for (var entry : tiles.entrySet()) {
      if (entry.getKey().equals(id)) {
        continue;
      }

      if (exclude.contains(entry.getKey())) {
        continue;
      }

      var m = Arrays.stream(entry.getValue()).anyMatch(i -> i == edges);
      if (m) {
        count = entry.getKey();
        output.add(entry.getKey());
      }
    }

    return count;
  }

  private static int convertTo(int[] edge) {
    var output = 0;
    for (int i = 0; i < edge.length; i++) {
      if (edge[i] == '#') {
        output <<= 1;
        output |= 1;
      } else {
        output <<= 1;
      }
    }

    return output;
  }

  private static boolean arrCont(int[] arr, int target) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == target) {
        return true;
      }
    }

    return false;
  }

  private static char[] print(int[] a) {
    var c = new char[a.length];
    for (int i = 0; i < a.length; i++) {
      if (a[i] == '#') {
        c[i] = '#';
      } else {
        c[i] = '.';
      }
    }

    return c;
  }

  private static String[] transform(String[] arr, Consumer<ArrayGrid>... transformation) {
    var chars = Arrays.stream(arr).flatMapToInt(String::chars).toArray();
    var g = new RowArrayGrid(chars, arr.length, arr.length);

    for (Consumer<ArrayGrid> t : transformation) {
      t.accept(g);
    }

    var output = new String[arr.length];
    for (int i = 0; i < arr.length; i++) {
      var tmp = g.row(i);
      var cTmp = new char[arr.length];
      for (int j = 0; j < arr.length; j++) {
        cTmp[j] = (char) tmp[j];
      }

      output[i] = new String(cTmp);
    }

    return output;
  }

  private static int[] traceOuterEdgeClockwise(int sideLength) {
    var total = sideLength * sideLength;

    var result = new int[sideLength + (sideLength - 1) + (sideLength - 1) + (sideLength - 2)];
    var counter = 0;

    int index;
    // top edge
    for (index = 0; index < sideLength; index++) {
      result[counter++] = index;
    }

    // right edge
    for (index = 2 * sideLength - 1; index < total - 1; index += sideLength) {
      result[counter++] = index;
    }

    // bottom edge
    for (; index >= total - sideLength; index--) {
      result[counter++] = index;
    }

    // left edge
    for (index = total - sideLength - sideLength; index >= sideLength; index -= sideLength) {
      result[counter++] = index;
    }

    return result;
  }

  private static boolean hasSeaMonster(String[] image, int x, int y) {
    var seaMonster = new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', ' '},
        {'#', ' ', ' ', ' ', ' ', '#', '#', ' ', ' ', ' ', ' ', '#', '#', ' ', ' ', ' ', ' ', '#', '#', '#'},
        {' ', '#', ' ', ' ', '#', ' ', ' ', '#', ' ', ' ', '#', ' ', ' ', '#', ' ', ' ', '#', ' ', ' ', ' '}
    };

    for (int seaMonsterY = 0; seaMonsterY < seaMonster.length; seaMonsterY++) {
      for (int seaMonsterX = 0; seaMonsterX < seaMonster[seaMonsterY].length; seaMonsterX++) {
        var d = seaMonster[seaMonsterY][seaMonsterX];
        if (d == ' ') {
          continue;
        }

        if (d != image[y + seaMonsterY].charAt(x + seaMonsterX)) {
          return false;
        }
      }
    }

    return true;
  }
}
