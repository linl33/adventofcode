package dev.linl33.adventofcode.year2020;

import java.io.BufferedReader;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day10 extends AdventSolution2020<Integer, Long> {
  public static void main(String[] args) {
    new Day10().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    var voltageDiff = new int[4];
    var prevVoltage = new AtomicInteger(0);

    reader
        .lines()
        .mapToInt(Integer::parseInt)
        .sorted()
        .forEach(adapter -> {
          voltageDiff[adapter - prevVoltage.getPlain()]++;
          prevVoltage.setPlain(adapter);
        });

    return voltageDiff[1] * (voltageDiff[3] + 1);
  }

  @Override
  public Long part2(BufferedReader reader) {
    var chain = reader
        .lines()
        .map(Integer::parseInt)
        .sorted()
        .collect(Collectors.toList());

    var adapterCount = chain.size();

    chain.add(0, 0);

    var partitionSize = 0;
    var prod = 1L;

    for (int i = 1; i < adapterCount; i++) {
      var prev = chain.get(i - 1);
      var next = chain.get(i + 1);

      if (next - prev <= 3) {
        prod <<= 1;
        partitionSize++;
      } else {
        partitionSize = 0;
      }

      if (partitionSize == 3) {
        // with the given input, when the partitionSize is 3
        // it is never valid to skip all 3 adapters
        // but it is always valid to skip any 2 adapters

        // divide by 8 to un-choose the last 3 adapters
        // then times 7 to choose combinations that use 1, 2, or 3 adapters
        prod = (prod >> 3) * ((1 << 3) - 1);
      }

      // with the given input, when the partitionSize is 2
      // it is always safe to exclude both adapters

      // the partitionSize is never greater than 3
    }

    return prod;
  }
}
