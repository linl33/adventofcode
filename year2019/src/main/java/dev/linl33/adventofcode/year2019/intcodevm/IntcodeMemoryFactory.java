package dev.linl33.adventofcode.year2019.intcodevm;

import java.io.BufferedReader;

public interface IntcodeMemoryFactory<TData, TAddr, T extends IntcodeMemory<TData, TAddr>> {
  T initialize(BufferedReader reader);
  T initialize(String memory);
  T newInstance(T source);
}
