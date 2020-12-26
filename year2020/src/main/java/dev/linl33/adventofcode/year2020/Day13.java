package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.solution.SolutionPart;
import dev.linl33.adventofcode.lib.util.AdventUtil;
import dev.linl33.adventofcode.lib.util.MathUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Day13 extends AdventSolution2020<Integer, Long> {
  private static final String BUS_SEP = ",";
  private static final String NO_BUS = "x";

  public static void main(String[] args) {
    new Day13().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    var busSchedule = BusSchedule.parse(reader);

    return Arrays
        .stream(busSchedule.buses)
        .filter(b -> b > 0)
        .mapToObj(busFreq -> Map.entry(
            busFreq,
            (busSchedule.arrivalTime / busFreq +
                (busSchedule.arrivalTime % busFreq == 0 ? 0 : 1)) * busFreq - busSchedule.arrivalTime
        ))
        .collect(Collectors.collectingAndThen(
            Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ),
            busTimeMap -> {
              var minBus = AdventUtil.argMin(busTimeMap);
              return busTimeMap.get(minBus) * minBus;
            }
        ));
  }

  @Override
  public Long part2(BufferedReader reader) throws Exception {
    return part2ByCrt(reader, true);
  }

  public long part2ByCrt(BufferedReader reader, boolean primeOnly) throws IOException {
    var buses = BusSchedule.parse(reader).buses;

    var busCount = (int) Arrays.stream(buses).filter(i -> i > 0).count();
    var modulo = new int[busCount];
    var remainder = new int[busCount];

    var idx = 0;
    for (int i = 0; i < buses.length; i++) {
      if (buses[i] == 0) {
        continue;
      }

      modulo[idx] = buses[i];
      remainder[idx] = (buses[i] - i) % buses[i];
      idx++;
    }

    return MathUtil.crt(modulo, remainder, primeOnly);
  }

  public long part2Iterative(BufferedReader reader) throws IOException {
    var buses = BusSchedule.parse(reader).buses;

    // TODO:
    throw new UnsupportedOperationException();
  }

  public long part2ByLinearSystem(BufferedReader reader) throws IOException {
    var buses = BusSchedule.parse(reader).buses;
    var busMap = new ArrayList<Map.Entry<Integer, Integer>>();

    for (int i = 0; i < buses.length; i++) {
      if (buses[i] == 0) {
        continue;
      }

      busMap.add(Map.entry(i, buses[i]));
    }

    for (int i = 1; i < busMap.size(); i++) {
      var currCoeff = busMap.get(i).getValue() + "";
      var prevCoeff = busMap.get(i - 1).getValue() + "";

      var currVar = (char) ('a' + i);
      var prevVar = (char) (currVar - 1);

      var constDiff = busMap.get(i).getKey() - busMap.get(i - 1).getKey() + "";

      System.out.println(currCoeff + currVar + " = " + prevCoeff + prevVar + " + " + constDiff);
    }

    // TODO:
    throw new UnsupportedOperationException();
  }

  private static record BusSchedule(int arrivalTime, int[] buses) {
    public static BusSchedule parse(BufferedReader reader) throws IOException {
      var aTime = Integer.parseInt(reader.readLine());
      var buses = Arrays
          .stream(reader.readLine().split(BUS_SEP))
          .map(b -> b.equals(NO_BUS) ? "0" : b)
          .mapToInt(Integer::parseInt)
          .toArray();

      return new BusSchedule(aTime, buses);
    }
  }
}
