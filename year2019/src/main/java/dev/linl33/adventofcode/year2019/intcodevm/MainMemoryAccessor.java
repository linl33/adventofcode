package dev.linl33.adventofcode.year2019.intcodevm;

public class MainMemoryAccessor implements MemoryAccessor<Long> {
  private final long[] memory;
  private final int addr;

  private long[] getMemory() {
    return memory;
  }

  public int getAddr() {
    return addr;
  }

  public MainMemoryAccessor(long[] memory, int addr) {
    this.memory = memory;
    this.addr = addr;
  }

  public static MainMemoryAccessor derive(MainMemoryAccessor accessor, int newAddr) {
    return new MainMemoryAccessor(accessor.getMemory(), newAddr);
  }

  @Override
  public Long get() {
    return memory[addr];
  }

  @Override
  public Long set(Long newVal) {
    var prevVal = get();
    memory[addr] = newVal;

    return prevVal;
  }

  @Override
  public Long add(Long delta) {
    return set(get() + delta);
  }
}
