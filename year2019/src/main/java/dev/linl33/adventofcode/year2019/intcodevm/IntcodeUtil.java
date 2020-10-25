package dev.linl33.adventofcode.year2019.intcodevm;

import java.io.BufferedReader;
import java.util.Arrays;

public class IntcodeUtil {
  public static long[] buildMemory(BufferedReader reader) {
    var arr = reader
        .lines()
        .limit(1)
        .flatMap(x -> Arrays.stream(x.split(",")))
        .mapToLong(Long::parseLong)
        .toArray();

    var outputArr = new long[arr.length * 25];
    System.arraycopy(arr, 0, outputArr, 0, arr.length);

    return outputArr;
  }
}
