package dev.linl33.adventofcode.year2019.intcodevm;

public class TMainMemoryAccessor<TData, TAddr> implements MemoryAccessor<TData> {
  private final IntcodeMemory<TData, TAddr> memory;
  private final TAddr addr;

  public TMainMemoryAccessor(IntcodeMemory<TData, TAddr> memory, TAddr addr) {
    this.memory = memory;
    this.addr = addr;
  }

  @Override
  public TData get() {
    return null;
  }

  @Override
  public TData set(TData newVal) {
    return null;
  }

  @Override
  public TData add(TData delta) {
    return null;
  }
}
