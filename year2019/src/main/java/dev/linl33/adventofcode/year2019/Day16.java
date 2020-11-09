package dev.linl33.adventofcode.year2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Day16 extends AdventSolution2019<String, String> {
  public static void main(String[] args) {
    new Day16().runAndPrintAll();
  }

  @Override
  public String part1(BufferedReader reader) throws IOException {
    var inputArr = Arrays
        .stream(reader.readLine().split(""))
        .mapToInt(Integer::parseInt)
        .toArray();

    for (var round = 0; round < 100; round++) {
      inputArr = fft(inputArr);
    }

    var sb = new StringBuilder();
    for (var i = 0; i < 8; i++) {
      sb.append(inputArr[i]);
    }

    return sb.toString();
  }

  @Override
  public String part2(BufferedReader reader) throws IOException {
    var input = reader.readLine();
    var startIdx = Integer.parseInt(input.substring(0, 7));

    var inputArr = Arrays.stream(input.split("")).mapToInt(Integer::parseInt).toArray();
    inputArr = repeatIntArr(inputArr, 10000);

    // single pass is enough, these digits don't depend on other digits
    var relevantIdxSet = new HashSet<Integer>();
    for (var i = startIdx; i < startIdx + 8; i++) {
      relevantIdxSet.addAll(generateSparseMask(i, inputArr.length).keySet());
    }

    for (var round = 0; round < 100; round++) {
      System.out.println(round);

      var inputArrNext = new int[inputArr.length];
      var inputArrCopy = inputArr;

      relevantIdxSet
          .stream()
          .parallel()
          .forEach(digit -> inputArrNext[digit] = fftSingleDigit(inputArrCopy, digit));

      inputArr = inputArrNext;
    }

    var sb = new StringBuilder();
    for (var i = startIdx; i < startIdx + 8; i++) {
      sb.append(inputArr[i]);
    }

    return sb.toString();
  }

  private static int[] fft(int[] input) {
    var inputLength = input.length;
    var output = new int[inputLength];

    for (var digitIdx = 0; digitIdx < inputLength; digitIdx++) {
      output[digitIdx] = fftSingleDigit(input, digitIdx);
    }

    return output;
  }

  private static int fftSingleDigit(int[] input, int digitIdx) {
    int digitIdxPlusOne = digitIdx + 1;
    int jIncrement = digitIdxPlusOne * 3;

    var sum = singleDigitPart1(digitIdx, jIncrement, input) + singleDigitPart2(digitIdx, jIncrement, input);

    return Math.abs(sum) % 10;
  }

  private static int singleDigitPart1(int digitIdx, int jIncrement, int[] input) {
    var inputLength = input.length;
    int digitIdxPlusOne = digitIdx + 1;

    var sum = 0;
    for (var j = digitIdx; j < inputLength; j += jIncrement) {
      for (var k = 0; k < digitIdxPlusOne && j < inputLength; k++) {
        sum += input[j++];
      }
    }

    return sum;
  }

  private static  int singleDigitPart2(int digitIdx, int jIncrement, int[] input) {
    var inputLength = input.length;
    int digitIdxPlusOne = digitIdx + 1;
    int negativeOneOffSet = digitIdx + digitIdxPlusOne * 2;

    var sum = 0;
    for (var j = negativeOneOffSet; j < inputLength; j += jIncrement) {
      for (var k = 0; k < digitIdxPlusOne && j < inputLength; k++) {
        sum -= input[j++];
      }
    }

    return sum;
  }

  private static Map<Integer, Integer> generateSparseMask(int order, int inputLength) {
    var map = new HashMap<Integer, Integer>(inputLength / 2);

    for (var i = order; i < inputLength;) {
      for (var j = 0; j < order + 1 && i < inputLength; j++) {
        map.put(i++, 1);
      }

      i += (order + 1) * 3;
    }

    for (var i = order + (order + 1) * 2; i < inputLength;) {
      for (var j = 0; j < order + 1 && i < inputLength; j++) {
        map.put(i++, -1);
      }


      i += (order + 1) * 3;
    }

    return map;
  }

  private static int[] repeatIntArr(int[] input, int repeat) {
    var output = new int[input.length * repeat];

    for (var i = 0; i < output.length; i += input.length) {
      System.arraycopy(input, 0, output, i, input.length);
    }

    return output;
  }
}
