package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.graph.GraphPath;
import dev.linl33.adventofcode.lib.graph.intgraph.DataIntGraphNode;
import dev.linl33.adventofcode.lib.graph.intgraph.IntGraph;
import dev.linl33.adventofcode.lib.graph.intgraph.IntGraphBuilder;
import dev.linl33.adventofcode.lib.util.AdventUtil;
import dev.linl33.adventofcode.year2019.intcodevm.IntcodeVM;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day25 extends AdventSolution2019<Integer, Integer> {
  private static final Set<String> IGNORED_ITEMS = Set.of(
      "escape pod",
      "giant electromagnet",
      "infinite loop",
      "molten lava",
      "photons"
  );

  private static final String BREACH = "Hull Breach";
  private static final String CHECKPOINT = "Security Checkpoint";
  private static final String PRESSURE_PLATE = "Pressure-Sensitive Floor";

  public static void main(String[] args) {
    new Day25().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    var initVm = new IntcodeVM(reader);
    var graph = buildGraph(initVm);

    var itemCombinations = Arrays
        .stream(graph.getNodes())
        .map(DataIntGraphNode::getData)
        .filter(r -> !r.items().isEmpty())
        .filter(r -> !IGNORED_ITEMS.contains(r.items().get(0)))
        .collect(Collectors.collectingAndThen(
            Collectors.toUnmodifiableList(),
            AdventUtil::<Room>generateCombinations
        ));

    var entrance = graph
        .findNode(n -> n.getData().name().equals(BREACH))
        .orElseThrow();

    var checkpoint = graph
        .findNode(n -> n.getData().name().equals(CHECKPOINT))
        .orElseThrow();

    for (var currCombin : itemCombinations) {
      var vm = initVm.forkNewIO();

      var currRoom = entrance.getData();
      for (Room room : currCombin) {
        moveTo(vm, graph, currRoom, room);
        currRoom = room;

        executeCommand(vm, "take " + room.items().get(0), null, IntcodeVM.ExecMode.STATEFUL);
      }

      moveTo(vm, graph, currRoom, checkpoint.getData());

      var result = executeCommand(vm, "east", Day25::parseOutput, IntcodeVM.ExecMode.STATEFUL);
      if (result.description().contains("You may proceed.")) {
        getLogger().info("Combination = {}", () -> currCombin
            .stream()
            .map(Room::items)
            .flatMap(List::stream)
            .sorted()
            .collect(Collectors.joining(", "))
        );

        return Integer.parseInt(result.description(), 260, 269, 10);
      }
    }

    throw new IllegalArgumentException();
  }

  @Override
  public Integer part2(BufferedReader reader) throws Exception {
    throw new UnsupportedOperationException();
  }

  private static IntGraph<Room, DataIntGraphNode<Room>> buildGraph(IntcodeVM vm) {
    var remaining = new ArrayDeque<IntcodeVM>();
    remaining.add(vm);

    var parentDeque = new ArrayDeque<Room>();
    var exploredRooms = new HashSet<String>();
    var builder = new IntGraphBuilder<Room>().withDefaultIdGenerator();

    while (!remaining.isEmpty()) {
      var vmNext = remaining.remove();
      var room = executeCommand(vmNext, null, Day25::parseOutput, IntcodeVM.ExecMode.STATEFUL);

      builder.addNode(room);

      if (!parentDeque.isEmpty()) {
        var parent = parentDeque.remove();
        builder.addEdge(parent, room, 1);
        builder.addEdge(room, parent, 1);
      }

      if (!room.name().equals(CHECKPOINT) && exploredRooms.add(room.name())) {
        for (var d : room.doors()) {
          var fork = vmNext.forkNewIO();
          sendCommand(fork.getInput(), d);
          remaining.add(fork);
          parentDeque.add(room);
        }
      }
    }

    return builder.build();
  }

  private static Room parseOutput(Collection<Long> output) {
    var out = output
        .stream()
        .map(l -> (char) l.intValue() + "")
        .collect(Collectors.collectingAndThen(Collectors.joining(), s -> s.trim().split("\n")));

    var name = out[0].substring(3, out[0].length() - 3);
    var description = out[1];

    if (name.equals(PRESSURE_PLATE)) {
      if (out.length == 9) {
        description = out[6] + out[7] + out[8];
      } else {
        description = out[6];
      }

      return new Room(name, description, List.of(), List.of());
    }

    var doors = new ArrayList<String>(4);
    var items = new ArrayList<String>(1);

    int i = 0;
    String line;
    do {
      line = out[i++];
    } while (!line.equals("Doors here lead:"));

    while (!(line = out[i++]).isEmpty()) {
      doors.add(line.substring(2));
    }

    if (!out[i].equals("Command?")) {
      do {
        line = out[i++];
      } while (!line.equals("Items here:"));

      while (!(line = out[i++]).isEmpty()) {
        items.add(line.substring(2));
      }
    }

    return new Room(name, description, doors, items);
  }

  private static void sendCommand(Queue<Long> inQueue, String command) {
    command
        .chars()
        .mapToLong(Long::valueOf)
        .forEachOrdered(inQueue::add);

    inQueue.add((long) '\n');
  }

  @Contract("_, _, null, _ -> null; _, _, !null, _, -> !null")
  private static <T> T executeCommand(@NotNull IntcodeVM vm,
                                      @Nullable String command,
                                      @Nullable Function<? super Collection<Long>, T> outputParser,
                                      @NotNull IntcodeVM.ExecMode mode) {
    if (command != null) {
      sendCommand(vm.getInput(), command);
    }

    vm.executeNonBlocking(mode);

    var result = outputParser != null ? outputParser.apply(vm.getOutput()) : null;
    vm.getOutput().clear();

    return result;
  }

  private static void moveTo(@NotNull IntcodeVM vm,
                             @NotNull IntGraph<Room, DataIntGraphNode<Room>> graph,
                             @NotNull Room start,
                             @NotNull Room end) {
    var currNode = graph.getNode(start).orElseThrow();
    var endNode = graph.getNode(end).orElseThrow();

    var path = graph
        .findPathFull(currNode, endNode)
        .map(GraphPath::path)
        .orElseThrow();

    var currRoom = start;

    while (!currRoom.equals(end)) {
      currNode = graph.getNode(currRoom).orElseThrow();
      var nextRoom = path.get(currNode).getData();
      if (currRoom.doors().size() == 1) {
        executeCommand(vm, currRoom.doors().get(0), null, IntcodeVM.ExecMode.STATEFUL);

        currRoom = nextRoom;
      } else {
        for (var d : currRoom.doors()) {
          var roomBehindRoom = executeCommand(vm, d, Day25::parseOutput, IntcodeVM.ExecMode.STATELESS);

          if (roomBehindRoom.equals(nextRoom)) {
            executeCommand(vm, d, null, IntcodeVM.ExecMode.STATEFUL);
            currRoom = nextRoom;
            break;
          }
        }
      }
    }
  }

  private static record Room(@NotNull String name,
                             @NotNull String description,
                             @NotNull List<String> doors,
                             @NotNull List<String> items) {
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Room room = (Room) o;

      return name.equals(room.name);
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }
  }
}
