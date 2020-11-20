//package dev.linl33.adventofcode.year2019.intcodevm;
//
//import java.util.Arrays;
//
//public class Int32Memory implements IntcodeMemory<Integer, int> {
//  private final Integer[] memory;
//
//  public Integer[] getMemory() {
//    return memory;
//  }
//
//  Int32Memory(Integer[] memory) {
//    this.memory = memory;
//  }
//
//  Int32Memory(Int32Memory source) {
//    this(Arrays.copyOf(source.getMemory(), source.getMemory().length));
//  }
//
//  @Override
//  public Integer read(int addr) {
//    return memory[addr];
//  }
//
//  @Override
//  public Integer write(Integer newVal, int addr) {
//    var prevVal = memory[addr];
//    memory[addr] = newVal;
//
//    return prevVal;
//  }
//
//  @Override
//  public String print() {
//    return Arrays.toString(memory);
//  }
//}
