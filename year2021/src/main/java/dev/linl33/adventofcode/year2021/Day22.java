package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import dev.linl33.adventofcode.lib.util.PrintUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day22 extends AdventSolution2021<Integer, Integer> {
  public static void main(String[] args) {
//    new Day22().runAndPrintAll();
//    new Day22().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day22test1"));
    new Day22().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day22test2"));
//    new Day22().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day22test1"));
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toList();

    var dim = 101;
    var cube = new boolean[dim][dim][dim];

    for (int z = 0; z < cube.length; z++) {
      cube[z] = new boolean[dim][dim];
      var plane = cube[z];

      for (int y = 0; y < plane.length; y++) {
        plane[y] = new boolean[dim];
      }
    }

    var instructions = new ArrayList<int[]>(lines.size());
    for (String line : lines) {
      var instruction = new int[7];

      var split = line.split(" ");
      var onOff = split[0];
      var coords = split[1].split(",");
      var x = coords[0].split("=")[1].split("\\.\\.");
      var y = coords[1].split("=")[1].split("\\.\\.");
      var z = coords[2].split("=")[1].split("\\.\\.");

      instruction[0] = Integer.parseInt(x[0]);
      instruction[1] = Integer.parseInt(x[1]);
      instruction[2] = Integer.parseInt(y[0]);
      instruction[3] = Integer.parseInt(y[1]);
      instruction[4] = Integer.parseInt(z[0]);
      instruction[5] = Integer.parseInt(z[1]);
      instruction[6] = onOff.equals("on") ? 1 : 0;

      instructions.add(instruction);
    }

    System.out.println(countRegion(cube, instructions, -50, 50, -50, 50, -50, 50));

    return null;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toList();

    var all = new ArrayList<int[]>(lines.size());
    for (String line : lines) {
      var instruction = new int[7];

      var split = line.split(" ");
      var onOff = split[0];
      var coords = split[1].split(",");
      var x = coords[0].split("=")[1].split("\\.\\.");
      var y = coords[1].split("=")[1].split("\\.\\.");
      var z = coords[2].split("=")[1].split("\\.\\.");

      instruction[0] = Integer.parseInt(x[0]);
      instruction[1] = Integer.parseInt(x[1]);
      instruction[2] = Integer.parseInt(y[0]);
      instruction[3] = Integer.parseInt(y[1]);
      instruction[4] = Integer.parseInt(z[0]);
      instruction[5] = Integer.parseInt(z[1]);
      instruction[6] = onOff.equals("on") ? 1 : 0;

      all.add(instruction);
    }

    var c = 0L;

    var parts = partition(all);

    for (List<Integer> value : parts.values()) {
      System.out.println(value);
    }
    System.out.println("--------------------------------");

    for (List<Integer> part : parts.values()) {
      var vol = part.stream().map(all::get).filter(ins -> ins[6] == 1).mapToLong(ins -> (long) (ins[1] - ins[0] + 1) * (ins[3] - ins[2] + 1) * (ins[5] - ins[4] + 1)).max().orElse(-1L);
      var onOff = part.stream().map(all::get).mapToInt(ins -> ins[6]).toArray();
      System.out.println(vol);
      PrintUtil.enhancedPrint(onOff);
    }

    for (List<Integer> part : parts.values()) {
      System.out.println(part);

      if (part.size() == 1) {
        var ins = all.get(part.get(0));

        if (ins[6] == 1) {
          c += (long) (ins[1] - ins[0] + 1) * (ins[3] - ins[2] + 1) * (ins[5] - ins[4] + 1);
        }
        continue;
      }

      var instructions = new ArrayList<int[]>();

      for (Integer p : part) {
        instructions.add(all.get(p));
      }

      var xRange = new int[] { Integer.MAX_VALUE, Integer.MIN_VALUE };
      var yRange = new int[] { Integer.MAX_VALUE, Integer.MIN_VALUE };
      var zRange = new int[] { Integer.MAX_VALUE, Integer.MIN_VALUE };
      for (int[] instruction : instructions) {
        xRange[0] = Math.min(xRange[0], (instruction[0]));
        xRange[0] = Math.min(xRange[0], (instruction[1]));
        xRange[1] = Math.max(xRange[1], (instruction[0]));
        xRange[1] = Math.max(xRange[1], (instruction[1]));

        yRange[0] = Math.min(yRange[0], (instruction[2]));
        yRange[0] = Math.min(yRange[0], (instruction[3]));
        yRange[1] = Math.max(yRange[1], (instruction[2]));
        yRange[1] = Math.max(yRange[1], (instruction[3]));

        zRange[0] = Math.min(zRange[0], (instruction[4]));
        zRange[0] = Math.min(zRange[0], (instruction[5]));
        zRange[1] = Math.max(zRange[1], (instruction[4]));
        zRange[1] = Math.max(zRange[1], (instruction[5]));
      }

      PrintUtil.enhancedPrint(xRange);
      PrintUtil.enhancedPrint(yRange);
      PrintUtil.enhancedPrint(zRange);

      var dim = 101;

      var zPoints = IntStream.iterate(zRange[0], z -> z <= zRange[1], z -> z + dim).toArray();
      var yPoints = IntStream.iterate(yRange[0], y -> y <= yRange[1], y -> y + dim).toArray();
      var xPoints = IntStream.iterate(xRange[0], x -> x <= xRange[1], x -> x + dim).toArray();

      var totalCubes = (long) (zRange[1] - zRange[0] + 1) * (yRange[1] - yRange[0] + 1) * (xRange[1] - xRange[0] + 1) / Math.pow(dim, 3);
      System.out.println(totalCubes);

      var cube = new boolean[dim][dim][dim];

      for (int i = 0; i < cube.length; i++) {
        cube[i] = new boolean[dim][dim];
        var plane = cube[i];

        for (int j = 0; j < plane.length; j++) {
          plane[j] = new boolean[dim];
        }
      }

      var c2 = 0L;
      var newRegions = 0L;
      for (int z : zPoints) {
        for (int y : yPoints) {
          for (int x : xPoints) {

            if (newRegions > 0) {
              for (int i = 0; i < cube.length; i++) {
                var plane = cube[i];
                for (int j = 0; j < plane.length; j++) {
                  Arrays.fill(plane[j], false);
                }
              }
            }

            var inc = dim - 1;
            newRegions = countRegion(cube, instructions, z, z + inc, y, y + inc, x, x + inc);
            c += newRegions;

            c2++;
            if (c2 % 1_000 == 0) {
              System.out.println(c2 + " " + c);
            }
          }
        }
      }
    }

    System.out.println(c);

    return null;
  }

  private static long countRegion(boolean[][][] cube, List<int[]> instructions, int zLo, int zHi, int yLo, int yHi, int xLo, int xHi) {
    for (int[] instruction : instructions) {
      for (int iZ = Math.max(zLo, instruction[4]); iZ <= Math.min(zHi, instruction[5]); iZ++) {
        for (int iY = Math.max(yLo, instruction[2]); iY <= Math.min(yHi, instruction[3]); iY++) {
          for (int iX = Math.max(xLo, instruction[0]); iX <= Math.min(xHi, instruction[1]); iX++) {
            cube[iZ + -zLo][iY + -yLo][iX + -xLo] = instruction[6] == 1;
          }
        }
      }
    }

    var counter = 0L;
    for (int z = 0; z < cube.length; z++) {
      var plane = cube[z];

      for (int y = 0; y < plane.length; y++) {
        var row = plane[y];

        for (int x = 0; x < row.length; x++) {
          if (row[x]) {
            counter++;
          }
        }
      }
    }

    return counter;
  }

  private static Map<Integer, List<Integer>> partition(List<int[]> instructions) {
    var remaining = new ArrayDeque<Integer>();
    for (int i = 0; i < instructions.size(); i++) {
      remaining.add(i);
    }

    var groups = new HashMap<Integer, Integer>();
    var groupId = 0;

    while (!remaining.isEmpty()) {
      var next = remaining.pop();

      if (groups.containsKey(next)) {
        continue;
      }

      groupId++;
      groups.put(next, groupId);
      for (int i = 0; i < instructions.size(); i++) {
        if (i == next) {
          continue;
        }

        if (intersect(instructions.get(next), instructions.get(i))) {
          remaining.remove(i);
          groups.put(i, groupId);
        }
      }
    }

//    System.out.println(groups);
    return groups
        .entrySet()
        .stream()
        .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
  }

  private static boolean intersect(int[] a, int[] b) {
    return intersect(a[0], a[1], b[0], b[1]) && intersect(a[2], a[3], b[2], b[3]) && intersect(a[4], a[5], b[4], b[5]);
  }

  private static boolean intersect(int leftMin, int leftMax, int rightMin, int rightMax) {
    var setA = new HashSet<Integer>();
//    var setB = new HashSet<Integer>();

    for (int i = leftMin; i <= leftMax; i++) {
      setA.add(i);
    }

    for (int i = rightMin; i <= rightMax; i++) {
      if (setA.contains(i)) {
        return true;
      }
    }

    return false;
  }
}
