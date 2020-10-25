package dev.linl33.adventofcode.year2018;

import dev.linl33.adventofcode.lib.util.AdventUtil;

import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day4 extends AdventSolution2018<Integer, Integer> {
  public static void main(String[] args) {
    new Day4().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    var recordByGuard = recordByGuard(reader);

    var maxGuard = recordByGuard
        .entrySet()
        .stream()
        .collect(Collectors.collectingAndThen(
            Collectors.toMap(
                Map.Entry::getKey,
                kv -> kv
                    .getValue()
                    .stream()
                    .map(GuardRecord::asleep)
                    .mapToInt(List::size)
                    .sum()
            ),
            AdventUtil::argMax
        ));

    var mostFreqMinute = recordByGuard
        .get(maxGuard)
        .stream()
        .map(GuardRecord::asleep)
        .flatMap(List::stream)
        .collect(Collectors.collectingAndThen(
            Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
            ),
            AdventUtil::argMax
        ));

    return Integer.parseInt(maxGuard.substring(1)) * mostFreqMinute;
  }

  @Override
  public Integer part2(BufferedReader reader) {
    var recordByGuard = recordByGuard(reader);

    var guardFreqMap = new HashMap<String, Map<Integer, Integer>>();
    for (Map.Entry<String, List<GuardRecord>> entry : recordByGuard.entrySet()) {
      for (GuardRecord guardRecord : entry.getValue()) {
        for (Integer minute : guardRecord.asleep()) {
          guardFreqMap
              .computeIfAbsent(entry.getKey(), __ -> new HashMap<>())
              .compute(minute, AdventUtil::increment);
        }
      }
    }

    var maxFreq = 0;
    var maxFreqGuard = "";
    var maxFreqMinute = 0;
    for (Map.Entry<String, Map<Integer, Integer>> entry : guardFreqMap.entrySet()) {
      var localMaxFreq = 0;
      var localMaxFreqMinute = 0;
      for (Map.Entry<Integer, Integer> localEntry : entry.getValue().entrySet()) {
        if (localEntry.getValue() > localMaxFreq) {
          localMaxFreq = localEntry.getValue();
          localMaxFreqMinute = localEntry.getKey();
        }
      }

      if (localMaxFreq > maxFreq) {
        maxFreq = localMaxFreq;
        maxFreqGuard = entry.getKey();
        maxFreqMinute = localMaxFreqMinute;
      }
    }

    return Integer.parseInt(maxFreqGuard.substring(1)) * maxFreqMinute;
  }

  private static Map<String, List<GuardRecord>> recordByGuard(BufferedReader reader) {
    var lines = reader
        .lines()
        .map(LogEntry::newLogEntry)
        .sorted(Comparator.comparing(LogEntry::timestamp))
        .collect(Collectors.toList());

    var recordList = new ArrayList<GuardRecord>();

    GuardRecord latestRecord = null;
    int sleepStart = 0;
    for (var line : lines) {
      if (line.entry.startsWith("Guard")) {
        latestRecord = new GuardRecord(line.entry.split(" ")[1]);
        recordList.add(latestRecord);
      } else {
        if (line.entry.startsWith("falls")) {
          sleepStart = line.timestamp.getMinute();
        } else {
          for (var i = sleepStart; i < line.timestamp.getMinute(); i++) {
            latestRecord.asleep().add(i);
          }
        }
      }
    }

    return recordList
        .stream()
        .collect(Collectors.groupingBy(GuardRecord::guardId));
  }

  private static record LogEntry(LocalDateTime timestamp, String entry) {
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
        .appendLiteral('-')
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendLiteral('-')
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .appendLiteral(' ')
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendLiteral(':')
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .toFormatter();

    public static LogEntry newLogEntry(String rawEntry) {
      var timestamp = LocalDateTime.parse(rawEntry.substring(1, 17), FORMATTER);
      var entry = rawEntry.substring(19);

      return new LogEntry(timestamp, entry);
    }
  }

  private static record GuardRecord(String guardId, List<Integer> asleep) {
    public GuardRecord(String guardId) {
      this(guardId, new ArrayList<>(60));
    }
  }
}
