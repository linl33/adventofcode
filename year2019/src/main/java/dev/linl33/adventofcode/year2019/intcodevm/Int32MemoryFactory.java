//package dev.linl33.adventofcode.year2019.intcodevm;
//
//import java.io.BufferedReader;
//import java.util.Arrays;
//import java.util.regex.Pattern;
//
//public class Int32MemoryFactory implements IntcodeMemoryFactory<Integer, int, Int32Memory> {
//  private static final int SIZE = 1 << 24;
//  private static final Pattern memorySepPattern = Pattern.compile(",");
//
//  @Override
//  public Int32Memory initialize(BufferedReader reader) {
//    Integer[] inputArr = reader
//        .lines()
//        .flatMap(memorySepPattern::splitAsStream)
//        .map(Integer::parseInt)
//        .toArray(Integer[]::new);
//
//    return new Int32Memory(Arrays.copyOf(inputArr, SIZE));
//  }
//
//  @Override
//  public Int32Memory initialize(String memory) {
//    Integer[] inputArr = memorySepPattern
//        .splitAsStream(memory)
//        .map(Integer::parseInt)
//        .toArray(Integer[]::new);
//
//    return new Int32Memory(Arrays.copyOf(inputArr, SIZE));
//  }
//
//  @Override
//  public Int32Memory newInstance(Int32Memory source) {
//    return new Int32Memory(source);
//  }
//}
