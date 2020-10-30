package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.graph.GraphPath;
import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import dev.linl33.adventofcode.lib.util.AdventUtil;
import dev.linl33.adventofcode.lib.graph.GraphUtil;
import dev.linl33.adventofcode.lib.util.MathUtil;
import dev.linl33.adventofcode.lib.util.PrintUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day18 extends AdventSolution2019<Integer, Integer> {
  private static final int MAX_COUNT = 26;
  private static final char WALL_CHAR = '#';
  private static final char DOT_CHAR = '.';

  public static void main(String[] args) {
//    new Day18().runAndPrintAll();

    new Day18().print(SolutionPart.PART_2);
  }

//  @Override
//  public String getDefaultResource() {
//    return "day18test9";
//  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    var initVault = VaultState.newVault(reader, false);
    initVault.print();

    return solveVault(initVault);
  }

  @Override
  public Integer part2(BufferedReader reader) throws Exception {
    var initVault = readVault(reader);
    var splits = splitVault(initVault);

    var initVaultCopy = copyVault(initVault);

//    for (char[][] split : splits) {
//      PrintUtil.enhancedPrint(split);
//      System.out.println();
//    }

    PrintUtil.enhancedPrint(initVault);

    var keyDeque = new ArrayDeque<Integer>();
    var posDeque = new ArrayDeque<int[]>();
    var costDeque = new ArrayDeque<Integer>();

    var doorPos = new Point2D[26];
    var keyPos = new Point2D[26];
    var keyCount = 0;

    var keys = 0;
    var entranceArr = new Point2D[4];
    var entranceCounter = 0;
    for (int y = 0; y < initVault.length; y++) {
      for (int x = 0; x < initVault[0].length; x++) {
        var c = initVault[y][x];
        var pt = new Point2D(x, y);

        if (c >= 'a' && c <= 'z') {
          keys |= (1 << (c - 'a'));
          keyPos[c - 'a'] = pt;
          keyCount++;
        } else if (c >= 'A' && c <= 'Z') {
          doorPos[c - 'A'] = pt;
        } else if (c == '@') {
          entranceArr[entranceCounter++] = pt;
        }

        if (c != WALL_CHAR) {
          initVaultCopy[y][x] = '.';
        }
      }
    }

    keyPos = Arrays.copyOf(keyPos, keyCount);
    doorPos = Arrays.copyOf(doorPos, keyCount);

//    PrintUtil.enhancedPrint(initVaultCopy);

    var state = VaultState.newVault(initVault, true);

    keyDeque.push(keys);
    posDeque.push(new int[] {52, 53, 54, 55});
    costDeque.push(0);

    var min = Integer.MAX_VALUE;
    /*while (!keyDeque.isEmpty()) {
      int k = keyDeque.pop();
      var p = posDeque.pop();
      int c = costDeque.pop();

      if (k == 0) {
        if (c < min) {
          min = c;
          System.out.println("MIN: " + min);
        }

        continue;
      }

      for (int nextKey = 0; nextKey < keyPos.length; nextKey++) {
        if ((k & (1 << nextKey)) == 0) {
          continue;
        }

        var nextKeyPos = keyPos[nextKey];

        // TODO: remember which key is in which part
        for (int i = 0; i < p.length; i++) {
          Point2D quadrantPos = p[i];
          var path = GraphUtil.aStarAsGraphPath(
              quadrantPos,
              nextKeyPos,
              pt -> listNeighborsWithDoors(pt, initVault, k),
              GraphUtil.manhattanDistHeuristic(nextKeyPos),
              (__, ___) -> 1
          );

          if (path != null) {
            var newPos = Arrays.copyOf(p, p.length);
            newPos[i] = nextKeyPos;

            keyDeque.push(k & ~(1 << nextKey));
            posDeque.push(newPos);
            costDeque.push(c + path.length());
          }
        }
      }
    }*/

    return min;
  }

  private static int solveVault(VaultState init) {
    var keyCount = init.keyCount();
    var maskLength = (int) Math.pow(2, keyCount);

    var store = new int[keyCount][maskLength];

    // TODO: use int[][] instead
    @SuppressWarnings("unchecked")
    var recentEntries = (ArrayList<Integer>[]) new ArrayList[keyCount];
    for (int i = 0; i < recentEntries.length; i++) {
      recentEntries[i] = new ArrayList<>();
    }

    for (int[] arr : store) {
      Arrays.fill(arr, Integer.MAX_VALUE);
    }

    for (int key = 0; key < keyCount; key++) {
      var neighborFunc = init.neighborsWithDoorsClosed(1 << (key - 32));

      for (int k2 = 0; k2 < keyCount; k2++) {
        if (k2 == key) {
          continue;
        }

        var path = GraphUtil.aStarLengthOnly(
            k2,
            key,
            neighborFunc,
            __ -> 0,
            init::cost,
            MAX_COUNT + keyCount
        );

        if (path.isPresent()) {
          store[k2][1 << key] = path.getAsInt();
          recentEntries[k2].add(1 << key);
        }
      }
    }

//    for (int i = 0; i < store.length; i++) {
//      var curr = (char) (i + 'a');
//      var thisStore = store[i];
//
//      var masks = IntStream
//          .range(0, maskLength)
//          .filter(x -> thisStore[x] != Integer.MAX_VALUE)
//          .mapToObj(m -> decodeMask(m, keyCount))
//          .collect(Collectors.joining(", "));
//
//      System.out.println(curr + " -> " + masks);
//    }

    System.out.println();

    var depth = keyCount - 2;

    var stopMap = new HashMap<Integer, Map<Integer, Set<Integer>>>();

    Integer pruneA = 0;
    Integer pruneB = 0;
    Integer pruneC = 0;
    Integer pruneD = 0;
    Integer pruneE = 0;
    Integer pruneF = 0;

    var intClearHeuristicsFunc = init.clearHeuristics();

    while (depth-- > 0) {
      var varCount = keyCount - depth - 1;
      System.out.println("searching depth = " + depth + ", vars = " + varCount);

      for (int currPos = 0; currPos < store.length; currPos++) {
        var intHeuristic = varCount < 10 ? init.manhattanHeuristics(currPos) : intClearHeuristicsFunc.apply(currPos);

        var stopMapForEntry = stopMap.computeIfAbsent(currPos, __ -> new HashMap<>());

        var recentEntriesForCurr = recentEntries[currPos];
        var newRecentEntries = new ArrayList<Integer>(MathUtil.choose(keyCount, varCount));

        for (int keyToAdd = 0; keyToAdd < keyCount; keyToAdd++) {
          if (keyToAdd == currPos) {
            continue;
          }

          if (init.dependencies[currPos] > 0 && (init.dependencies[currPos] & (1 << keyToAdd)) > 0) {
            pruneF++;
            continue;
          }

          for (var currMask : recentEntriesForCurr) {
            var newMask = currMask | (1 << keyToAdd);

            if (newMask == currMask) {
              continue;
            }

            // TODO: this doesn't remember the no-path cases
            if (store[currPos][newMask] != Integer.MAX_VALUE) {
              pruneE++;
              continue;
            }

            var min = Integer.MAX_VALUE;

            var intNeighborFunc = init.neighborsWithDoorsClosed(newMask);

            for (int maskKey = 0; maskKey < keyCount; maskKey++) {
              if ((newMask & (1 << maskKey)) == 0) {
                continue;
              }

              var restOfPath = store[maskKey][newMask & ~(1 << maskKey)];

              if (restOfPath >= min) {
                pruneA++;
                continue;
              }

              var limit = stopMapForEntry.get(maskKey);

              if (limit != null) {
                var found = false;
                for (Integer l : limit) {
                  if ((newMask & l) == l) {
                    found = true;
                    break;
                  }
                }

                if (found) {
                  pruneB++;
                  continue;
                }
              }

              var bestCaseDistance = intHeuristic.applyAsInt(maskKey);
              if (bestCaseDistance + restOfPath > min) {
                pruneC++;
                continue;
              }

              var path = GraphUtil.aStarLengthOnly(
                  maskKey,
                  currPos,
                  intNeighborFunc,
                  intHeuristic,
                  init::cost,
                  MAX_COUNT + keyCount
              );

              if (path.isEmpty()) {
                stopMapForEntry.computeIfAbsent(maskKey, __ -> new HashSet<>()).add(newMask);
                pruneD++;

                continue;
              }

              min = Math.min(path.getAsInt() + restOfPath, min);
            }

            if (min != Integer.MAX_VALUE) {
              newRecentEntries.add(newMask);
              store[currPos][newMask] = min;
            }
          }
        }

        recentEntries[currPos] = newRecentEntries;
      }
    }

    System.out.println("FINDING MIN");
    System.out.println(String.join(", ", pruneA.toString(), pruneB.toString(), pruneC.toString(), pruneD.toString(), pruneE.toString(), pruneF.toString()));
    System.out.println(pruneA + pruneB + pruneC + pruneD + pruneE + pruneF);

//    for (int i = 0; i < store.length; i++) {
//      var curr = (char) (i + 'a');
//      var thisStore = store[i];
//
//      var masks = IntStream
//          .range(0, maskLength)
//          .filter(x -> thisStore[x] != Integer.MAX_VALUE)
//          .mapToObj(m -> decodeMask(m, keyCount))
//          .collect(Collectors.joining(", "));
//
//      System.out.println(curr + " -> " + masks);
//    }

    // TODO: do this part ahead of time and pass in
    var min = Integer.MAX_VALUE;
    for (int i = 0; i < store.length; i++) {
      var partialPathDistance = store[i][init.keys() & ~(1 << i)];

      if (partialPathDistance == Integer.MAX_VALUE) {
        continue;
      }

      var cost = init.costToEntrance(i);

      if (cost < 0) {
        continue;
      }

      min = Math.min(min, cost + partialPathDistance);
    }

    System.out.println();
    System.out.println(min);

    if (min == Integer.MAX_VALUE) {
      throw new IllegalArgumentException();
    }

    return min;
  }

  private static char[][] copyVault(char[][] vault) {
    var copy = new char[vault.length][];

    for (int i = 0; i < vault.length; i++) {
      copy[i] = Arrays.copyOf(vault[i], vault[i].length);
    }

    return copy;
  }

  private static List<Point2D> listNeighbors(Point2D position, char[][] vault) {
    var neighbors = new ArrayList<Point2D>(4);

    var posX = position.x();
    var posY = position.y();

    if (posX + 1 < vault[0].length && vault[posY][posX + 1] == DOT_CHAR) {
      neighbors.add(position.translate(1, 0));
    }

    if (posX - 1 >= 0 && vault[posY][posX - 1] == DOT_CHAR) {
      neighbors.add(position.translate(-1, 0));
    }

    if (posY + 1 < vault.length && vault[posY + 1][posX] == DOT_CHAR) {
      neighbors.add(position.translate(0, 1));
    }

    if (posY - 1 >= 0 && vault[posY - 1][posX] == DOT_CHAR) {
      neighbors.add(position.translate(0, -1));
    }

    return neighbors;
  }

  private static List<Point2D> listNeighborsWithDoors(Point2D position, char[][] vault, int doors) {
    var neighbors = new ArrayList<Point2D>(4);

    var posX = position.x();
    var posY = position.y();

    if (posX + 1 < vault[0].length && isNeighbor(vault[posY][posX + 1], doors)) {
      neighbors.add(position.translate(1, 0));
    }

    if (posX - 1 >= 0 && isNeighbor(vault[posY][posX - 1], doors)) {
      neighbors.add(position.translate(-1, 0));
    }

    if (posY + 1 < vault.length && isNeighbor(vault[posY + 1][posX], doors)) {
      neighbors.add(position.translate(0, 1));
    }

    if (posY - 1 >= 0 && isNeighbor(vault[posY - 1][posX], doors)) {
      neighbors.add(position.translate(0, -1));
    }

    return neighbors;
  }

  private static boolean isNeighbor(char c, int doors) {
    return c != WALL_CHAR && (c == DOT_CHAR || (c >= 'a' && c <= 'z') || (doors & (1 << (c - 'A' + 32))) == 0);
  }

  private static String decodeMask(int mask, int maskSize) {
    return IntStream
        .range(0, maskSize)
        .filter(i -> (mask & (1 << i)) > 0)
        .mapToObj(i -> String.valueOf((char) (i + 'a')))
        .collect(Collectors.joining(" "));
  }

  private static char[][] readVault(BufferedReader reader) {
    return reader
        .lines()
        .map(String::toCharArray)
        .toArray(char[][]::new);
  }

  private static char[][][] splitVault(char[][] vault) {
    var parts = new char[4][][];

    Point2D entrance = null;
    for (int y = 0; y < vault.length; y++) {
      for (int x = 0; x < vault[0].length; x++) {
        if (vault[y][x] == '@') {
          entrance = new Point2D(x, y);
        }
      }
    }

    if (entrance == null) {
      throw new IllegalArgumentException();
    }

    vault[entrance.y()][entrance.x()] = '#';
    vault[entrance.y() - 1][entrance.x()] = '#';
    vault[entrance.y() + 1][entrance.x()] = '#';
    vault[entrance.y()][entrance.x() - 1] = '#';
    vault[entrance.y()][entrance.x() + 1] = '#';
    vault[entrance.y() - 1][entrance.x() - 1] = '@';
    vault[entrance.y() + 1][entrance.x() + 1] = '@';
    vault[entrance.y() - 1][entrance.x() + 1] = '@';
    vault[entrance.y() + 1][entrance.x() - 1] = '@';

    parts[0] = new char[entrance.y() + 1][];
    parts[1] = new char[entrance.y() + 1][];
    parts[2] = new char[vault.length - entrance.y()][];
    parts[3] = new char[vault.length - entrance.y()][];

    for (int y = 0; y <= entrance.y(); y++) {
      parts[0][y] = Arrays.copyOfRange(vault[y], 0, entrance.x() + 1);
      parts[1][y] = Arrays.copyOfRange(vault[y], entrance.x(), vault[0].length);
    }

    for (int y = entrance.y(); y < vault.length; y++) {
      parts[2][y - entrance.y()] = Arrays.copyOfRange(vault[y], 0, entrance.x() + 1);
      parts[3][y - entrance.y()] = Arrays.copyOfRange(vault[y], entrance.x(), vault[0].length);
    }

    return parts;
  }

  private static record VaultState(char[][] vault,
                                   Point2D[] entrance,
                                   int keys,
                                   int doors,
                                   int keyCount,
                                   Point2D[] keyArr,
                                   Point2D[] doorArr,
                                   int[][] adjacencyMatrix,
                                   int[][] adjacencyIntList,
                                   int[] keyPartition,
                                   int[] dependencies) {
    private static final int[] INT_EDGE_LIST_EMPTY = new int[0];

    private static final Logger LOGGER = LogManager.getLogger(VaultState.class);

    public static VaultState newVault(BufferedReader reader, boolean includeEntrances) {
      return newVault(readVault(reader), includeEntrances);
    }

    public static VaultState newVault(char[][] vault, boolean includeEntrances) {
      var vaultCopy = copyVault(vault); // TODO: remove the copy

      var entrances = new ArrayList<Point2D>();

      var keys = 0;
      var doors = 0;

      var keyCount = 0;

      var keyArrTmp = new Point2D[MAX_COUNT];
      var doorArrTmp = new Point2D[MAX_COUNT];

      for (int y = 0; y < vault.length; y++) {
        for (int x = 0; x < vault[y].length; x++) {
          var c = vault[y][x];

          if (c == WALL_CHAR || c == DOT_CHAR) {
            continue;
          }

          vaultCopy[y][x] = DOT_CHAR;

          var pos = new Point2D(x, y);

          if (c == '@') {
            entrances.add(pos);
            vault[y][x] = DOT_CHAR;
            continue;
          }

          var isDoor = c < 'a';
          var idx = c - (isDoor ? 'A' : 'a');

          (isDoor ? doorArrTmp : keyArrTmp)[idx] = pos;

          if (isDoor) {
            doors |= 1 << idx;
          } else {
            keys |= 1 << idx;
            keyCount++;
          }
        }
      }

      if (entrances.isEmpty()) {
        throw new IllegalArgumentException();
      }

      var nodeSize = includeEntrances ? (MAX_COUNT * 2 + entrances.size()) : (MAX_COUNT + keyCount);

      var keyArr = Arrays.copyOf(keyArrTmp, keyCount);
      var doorArr = Arrays.copyOf(doorArrTmp, keyCount);

      var removedDoors = fillEnclaves(vaultCopy, entrances, keyArr, doorArr);

      for (var d : removedDoors) {
        doors &= ~(1 << d);
      }

      vault = vaultCopy;

      var nodes = new HashMap<Integer, Point2D>();

      IntStream.range(0, keyCount).forEach(i -> nodes.put(i, keyArr[i]));
      IntStream.range(0, keyCount).forEach(i -> nodes.put(i + MAX_COUNT, doorArr[i]));
      if (includeEntrances) {
        IntStream.range(0, entrances.size()).forEach(i -> nodes.put(i + MAX_COUNT * 2, entrances.get(i)));
      }
      nodes.values().removeIf(Objects::isNull);

      var adjacencyMatrix = new int[nodeSize][nodeSize];
      var adjacencyList = new int[nodeSize][];

      for (var node : nodes.entrySet()) {
        var currPt = node.getValue();
        var currInt = node.getKey();

        adjacencyList[currInt] = doWithVault(
            vault,
            Map.of(currPt, DOT_CHAR),
            v -> {
              var aStarForKey = GraphUtil.adaptAStar(
                  currPt,
                  pt -> listNeighbors(pt, v),
                  GraphUtil.manhattanDistHeuristic(currPt)
              );

              var edges = new int[nodes.size() - 1];
              var edgeCount = 0;

              for (var n2 : nodes.entrySet()) {
                if (node == n2) {
                  continue;
                }

                var n2Pt = n2.getValue();
                var n2Int = n2.getKey();

                var path = doWithVault(
                    v,
                    Map.of(n2Pt, DOT_CHAR),
                    __ -> aStarForKey.apply(n2Pt)
                );

                if (path.isPresent()) {
                  var pathLength = path.get().length();

                  adjacencyMatrix[currInt][n2Int] = pathLength;
                  edges[edgeCount++] = n2Int;

                  var currStr = currInt > 51 ? ("@" + (currInt - 51)) : String.valueOf(AdventUtil.intToAlphabet(currInt));
                  var n2Str = n2Int > 51 ? ("@" + (n2Int - 51)) : String.valueOf(AdventUtil.intToAlphabet(n2Int));

                  LOGGER.debug("{} -> {} {}", currStr, n2Str, pathLength);
                } else {
                  adjacencyMatrix[currInt][n2Int] = Integer.MIN_VALUE;
                }
              }

              return Arrays.copyOf(edges, edgeCount);
            }
        );
      }

      int[] keyPartition;

      if (entrances.size() == 1) {
        keyPartition = new int[keyCount];
      } else {
        if (includeEntrances) {
          // TODO: this is wrong

          keyPartition = new int[keyCount];

          if (entrances.size() > 1) {
            for (int i = 0; i < keyCount; i++) {
              for (int e = 0; e < entrances.size(); e++) {
                if (adjacencyMatrix[i][e] > 0) {
                  keyPartition[i] = e;
                  break;
                }
              }
            }
          }
        } else {
          keyPartition = null;
        }
      }

      PrintUtil.enhancedPrint(keyPartition);

      var entranceArr = entrances.toArray(new Point2D[0]);

      return new VaultState(
          vault,
          entranceArr,
          keys,
          doors,
          keyCount,
          keyArr,
          doorArr,
          adjacencyMatrix,
          adjacencyList,
          keyPartition,
          buildDependencies(vault, entranceArr, keyPartition, keyCount, keyArr, adjacencyList)
      );
    }

    public IntFunction<int[]> neighborsWithDoorsClosed(int doors) {
      return i -> {
        if (i > 25 && (doors & (1 << (i % 26))) > 0) {
          return INT_EDGE_LIST_EMPTY;
        }

        return adjacencyIntList[i];
      };
    }

    private int[] neighbors(int i) {
      return adjacencyIntList[i];
    }

    public IntUnaryOperator manhattanHeuristics(int target) {
      var targetPt = (target > 25 ? doorArr : keyArr)[target % 26];

      return start -> targetPt.manhattanDistance((start > 25 ? doorArr : keyArr)[start % 26]);
    }

    public IntFunction<IntUnaryOperator> clearHeuristics() {
      var clearHeuristicMap = new HashMap<Long, Integer>();

      return cEnd -> {
        var heuristic = manhattanHeuristics(cEnd);

        return (IntUnaryOperator) cStart -> {
          if (cEnd == cStart) {
            return 0;
          }

          return clearHeuristicMap
              .computeIfAbsent(
                  (1L << cEnd) | (1L << cStart),
                  __ -> GraphUtil
                      .aStarLengthOnly(cEnd, cStart, this::neighbors, heuristic, this::cost, MAX_COUNT + keyCount).getAsInt()
              );
        };
      };
    }

    public int cost(int start, int end) {
      return adjacencyMatrix[start][end];
    }

    public int costToEntrance(int key) {
      var path = GraphUtil.aStar(
          keyArr[key],
          entrance[keyPartition[key]],
          pt -> listNeighbors(pt, vault)
      );

      return path.map(GraphPath::length).orElse(Integer.MIN_VALUE);
    }

    public void doWithVault(Map<Point2D, Character> changes, Consumer<VaultState> action) {
      doWithVault(changes, v -> {
        action.accept(v);
        return null;
      });
    }

    public <T> T doWithVault(Map<Point2D, Character> changes, Function<VaultState, T> action) {
      return doWithVault(vault(), changes, v -> action.apply(this));
    }

    public void print() {
      var changes = Arrays
          .stream(entrance())
          .collect(Collectors.toUnmodifiableMap(
              Function.identity(),
              __ -> '@')
          );

      doWithVault(changes, (Consumer<VaultState>) v -> PrintUtil.enhancedPrint(v.vault()));
    }

    public static <T> T doWithVault(char[][] vault, Map<Point2D, Character> changes, Function<char[][], T> action) {
      var backup = new HashMap<Point2D, Character>(changes.size());

      for (var entry : changes.entrySet()) {
        backup.put(entry.getKey(), vault[entry.getKey().y()][entry.getKey().x()]);
        vault[entry.getKey().y()][entry.getKey().x()] = entry.getValue();
      }

      var result = action.apply(vault);

      for (var entry : backup.entrySet()) {
        vault[entry.getKey().y()][entry.getKey().x()] = entry.getValue();
      }

      return result;
    }

    private static Set<Integer> fillEnclaves(char[][] vault, Collection<Point2D> originSet, Point2D[] keyPosArr, Point2D[] doorPosArr) {
      var keyPosSet = Set.of(keyPosArr);

      boolean changed;

      do {
        changed = false;

        for (int y = 0; y < vault.length; y++) {
          var row = vault[y];

          for (int x = 0; x < row.length; x++) {
            var c = row[x];

            if (c != DOT_CHAR) {
              continue;
            }

            var position = new Point2D(x, y);

            if (keyPosSet.contains(position) || originSet.contains(position)) {
              continue;
            }

            var neighbors = listNeighbors(position, vault);

            if (neighbors.size() < 2) {
              row[x] = WALL_CHAR;
              changed = true;
            }
          }
        }
      } while (changed);

      for (int i = 0; i < keyPosArr.length; i++) {
        var pos = keyPosArr[i];
        vault[pos.y()][pos.x()] = (char) (i + 'a');
      }

      var removedDoors = new HashSet<Integer>();
      for (int i = 0; i < doorPosArr.length; i++) {
        var pos = doorPosArr[i];

        if (pos == null) {
          continue;
        }

        if (vault[pos.y()][pos.x()] != WALL_CHAR) {
          vault[pos.y()][pos.x()] = (char) (i + 'A');
        } else {
          removedDoors.add(i);
          doorPosArr[i] = null;
        }
      }

      return removedDoors;
    }

    private static int[] buildDependencies(char[][] vault,
                                           Point2D[] entrance,
                                           int[] keyPartition,
                                           int keyCount,
                                           Point2D[] keyArr,
                                           int[][] adjacencyList) {
      @SuppressWarnings("unchecked")
      var dependencies = (Set<Integer>[]) new Set[keyCount];

      for (int i = 0; i < keyCount; i++) {
        var neighbors = adjacencyList[i];

        if (neighbors.length > 2) {
          continue;
        }

        // 2 neighbors and that none of them are this key's own door
        if (neighbors.length == 2 && neighbors[0] != (char) (i + MAX_COUNT) && neighbors[1] != (char) (i + MAX_COUNT)) {
          continue;
        }

        // check if the starting position can access this key
        var kPos = keyArr[i];
        var kEntrance = entrance[keyPartition[i]];

        var pathToKey = doWithVault(
            vault,
            Map.of(kPos, DOT_CHAR),
            v -> GraphUtil.aStar(kEntrance, kPos, pt -> listNeighbors(pt, v))
        );

        if (pathToKey != null) {
          continue;
        }

        var hasKey = false;
        for (int out : neighbors) {
          if (out > 25) {
            hasKey = true;
            break;
          }
        }

        if (!hasKey) {
          int d = neighbors[0];
          if (neighbors.length == 2 && neighbors[0] == (char) (i + MAX_COUNT)) {
            d = neighbors[1];
          }

          dependencies[i] = new HashSet<>(Set.of(d));
        }
      }

      System.out.println(Arrays.toString(dependencies));

      boolean changed;

      do {
        changed = false;

        for (Set<Integer> dependency : dependencies) {
          if (dependency == null) {
            continue;
          }

          var newDeps = new HashSet<Integer>();

          for (Integer dep : dependency) {
            if (dependencies[dep] != null) {
              newDeps.addAll(dependencies[dep]);
            }
          }

          changed |= dependency.addAll(newDeps);
        }
      } while (changed);

      System.out.println(Arrays.toString(dependencies));

      for (int i = 0; i < dependencies.length; i++) {
        if (dependencies[i] == null) {
          continue;
        }

        var deps = dependencies[i]
            .stream()
            .map(idx -> 1 << idx)
            .collect(Collectors.collectingAndThen(
                Collectors.reducing(
                    0,
                    (left, right) -> left | right
                ),
                m -> decodeMask(m, keyCount)
            ));

        System.out.println((char) (i + 'a') + " depends on: " + deps);
      }

      var dependenciesArr = new int[keyCount];
      for (int i = 0; i < dependencies.length; i++) {
        if (dependencies[i] == null) {
          continue;
        }

        for (Integer dep : dependencies[i]) {
          dependenciesArr[i] |= (1 << dep);
        }
      }

      return dependenciesArr;
    }
  }
}
