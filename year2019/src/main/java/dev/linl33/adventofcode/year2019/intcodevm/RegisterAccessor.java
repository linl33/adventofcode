package dev.linl33.adventofcode.year2019.intcodevm;

import java.util.Map;

public class RegisterAccessor implements MemoryAccessor<Integer> {
  private final Map<IntcodeVM.Register, Integer> registers;
  private final IntcodeVM.Register register;

  private Map<IntcodeVM.Register, Integer> getRegisters() {
    return registers;
  }

  public IntcodeVM.Register getRegister() {
    return register;
  }

  public RegisterAccessor(Map<IntcodeVM.Register, Integer> registers, IntcodeVM.Register register) {
    this.registers = registers;
    this.register = register;
  }

  @Override
  public Integer get() {
    return registers.get(register);
  }

  @Override
  public Integer set(Integer newVal) {
    return registers.put(register, newVal);
  }

  @Override
  public Integer add(Integer delta) {
    return set(get() + delta);
  }
}
