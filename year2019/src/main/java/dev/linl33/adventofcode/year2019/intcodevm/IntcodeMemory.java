package dev.linl33.adventofcode.year2019.intcodevm;

public interface IntcodeMemory<TData, TAddr> {
  TData read(TAddr addr);
  TData readFirstEntry();

  TData write(TData data, TAddr addr);
  TData writeVerb(TData verb);
  TData writeNoun(TData noun);

  String print();
}
