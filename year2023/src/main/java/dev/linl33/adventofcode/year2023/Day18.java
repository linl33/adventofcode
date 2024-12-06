package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.jmh.JmhBenchmarkOption;
import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Day18 extends AdventSolution2023<Long, Long> implements ByteBufferAdventSolution<Long, Long> {
  private static final int NORTH = 0b00;
  private static final int EAST = 0b01;
  private static final int SOUTH = 0b10;
  private static final int WEST = 0b11;

  private static final int[] DIR_TO_INT = new int[] {
    0,
    0,
    EAST,
    0,
    SOUTH,
    0,
    0,
    0,
    0,
    0,
    NORTH,
    0,
    WEST,
    0,
    0,
    0,
  };

  private static final MethodHandle OF_ADDRESS_UNSAFE;
  private static final VarHandle INT_HANDLE;

  private static MemorySegment ofAddressUnsafe(long address) {
    return MemorySegment.ofAddress(address).reinterpret(Integer.BYTES);
  }

  public static VarHandle adaptSegmentVarHandle(VarHandle handle) {
    handle = MethodHandles.insertCoordinates(handle, 1, 0L);
    handle = MethodHandles.filterCoordinates(handle, 0, OF_ADDRESS_UNSAFE);
    return handle;
  }

  static {
    try {
      OF_ADDRESS_UNSAFE = MethodHandles.lookup().findStatic(Day18.class, "ofAddressUnsafe", MethodType.methodType(MemorySegment.class, long.class));
    } catch (NoSuchMethodException | IllegalAccessException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    INT_HANDLE = adaptSegmentVarHandle(ValueLayout.JAVA_INT_UNALIGNED.varHandle());
  }

  public static void main(String[] args) {
//    new Day18().runAndPrintAll();
//    new Day18().benchmark(JmhBenchmarkOption.PART_1, JmhBenchmarkOption.PERF_PROFILE);
//    new Day18().benchmark(JmhBenchmarkOption.PART_1, JmhBenchmarkOption.ASYNC_PROFILE);
    new Day18().benchmark(JmhBenchmarkOption.PART_1);
//    new Day18().benchmark(JmhBenchmarkOption.PART_1, JmhBenchmarkOption.STACK_PROFILE);
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);

    var curr = new Point2D(0, 0);
    var dug = new ArrayList<Point2D>();
    dug.add(curr);

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      var parts = line.split(" ", 3);
      var dir = parts[0].charAt(0);
      var mag = Integer.parseInt(parts[1]);

      if (dir == 'U') {
        curr = curr.translate(0, -mag);
        dug.add(curr);
      } else if (dir == 'D') {
        curr = curr.translate(0, mag);
        dug.add(curr);
      } else if (dir == 'R') {
        curr = curr.translate(mag, 0);
        dug.add(curr);
      } else if (dir == 'L') {
        curr = curr.translate(-mag, 0);
        dug.add(curr);
      }

//      System.out.println(STR."\{curr.x()} \{curr.y()}");
    }

    return findArea(dug);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);

    var curr = new Point2D(0, 0);
    var dug = new ArrayList<Point2D>();
    dug.add(curr);

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      var parts = line.split(" ", 3);

      var dir = switch (parts[2].charAt(7)) {
        case '0' -> 'R';
        case '1' -> 'D';
        case '2' -> 'L';
        case '3' -> 'U';
        default -> throw new IllegalStateException();
      };

      var mag = Integer.parseInt(parts[2], 2, 7, 16);

      if (dir == 'U') {
        curr = curr.translate(0, -mag);
        dug.add(curr);
      } else if (dir == 'D') {
        curr = curr.translate(0, mag);
        dug.add(curr);
      } else if (dir == 'R') {
        curr = curr.translate(mag, 0);
        dug.add(curr);
      } else if (dir == 'L') {
        curr = curr.translate(-mag, 0);
        dug.add(curr);
      }
    }

    return findArea(dug);
  }

  @Override
  public Long part1(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part1(identifier);
  }

//  @Override
//  public Long part2(@NotNull ResourceIdentifier identifier) throws Exception {
//    return ByteBufferAdventSolution.super.part2(identifier);
//  }

  @Override
  public Long part1(@NotNull ByteBuffer byteBuffer) throws Exception {
    var memSegment = MemorySegment.ofBuffer(byteBuffer);
    // TODO: try array transfer

    var currX = 0L;
    var borderArea = 0L;
    var interiorArea = 0L;

    var address = memSegment.address();
    final var limit = address + memSegment.byteSize();
    var offset = 0L;
//    while (offset < byteBuffer.limit()) {
//    while (offset < memSegment.byteSize()) {
    while (address < limit) {
//      var bytes = memSegment.get(ValueLayout.JAVA_INT_UNALIGNED, offset);
      final var bytes = (int) INT_HANDLE.get(address);
//      byteBuffer.position(offset);
//      var bytes = byteBuffer.getInt(offset);

      // offset 8 if 1 digit, offset 0 if 2 digit
      var spaceOffset = (~bytes & 0x10000000) >> (1 + 24);
      // extract + convert string to int
      var mag = (((bytes << spaceOffset) & 0x0f0f0000) * 0x0a01) >> 24;
      borderArea += mag;

      final var dir = DIR_TO_INT[bytes & 0xf];
      final var signedMag = mag * ((dir & 0b10) - 1);
      currX += ((~dir & 0b1) - 1) & signedMag;
      interiorArea += ((dir & 0b1) - 1) & (currX * signedMag);

//      offset += 4 + 10 + ((spaceOffset >> 3) ^ 1);
      address += 4 + 10 + ((spaceOffset >> 3) ^ 1);
    }

    // pick's theorem
    return Math.abs(interiorArea) + borderArea / 2 + 1;
  }

  @Override
  public Long part2(@NotNull ByteBuffer byteBuffer) throws Exception {
    return ByteBufferAdventSolution.super.part2(byteBuffer);
  }

  private static long findArea(List<Point2D> pts) {
    pts.add(pts.getFirst());

    var sum = 0L;
    for (int i = 1; i < pts.size() - 1; i++) {
      var pt = pts.get(i);
      var prev = pts.get(i - 1);
      var next = pts.get(i + 1);

      sum += (long) pt.x() * (next.y() - prev.y());
    }

    sum /= 2;

    var borderArea = 0L;
    for (int i = 0; i < pts.size() - 1; i++) {
      var pt = pts.get(i);
      var next = pts.get(i + 1);
      borderArea += Math.abs(pt.x() - next.x()) + Math.abs(pt.y() - next.y());
    }

    return sum + borderArea / 2 + 1;
  }
}
