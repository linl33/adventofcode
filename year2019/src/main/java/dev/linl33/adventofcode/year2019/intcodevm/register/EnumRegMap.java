package dev.linl33.adventofcode.year2019.intcodevm.register;

import dev.linl33.adventofcode.year2019.intcodevm.IIntcodeVm;
import dev.linl33.adventofcode.year2019.intcodevm.MemoryAccessor;

import java.util.EnumMap;

public class EnumRegMap<V extends MemoryAccessor<T>, T> extends EnumMap<IIntcodeVm.Register, V> {
  public EnumRegMap() {
    super(IIntcodeVm.Register.class);
  }
}
