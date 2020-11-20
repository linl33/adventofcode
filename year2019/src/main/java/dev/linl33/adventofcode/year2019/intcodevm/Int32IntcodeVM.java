package dev.linl33.adventofcode.year2019.intcodevm;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class Int32IntcodeVM implements IIntcodeVm<Integer> {
  @Override
  public Integer execute(Integer verb, Integer noun, ExecMode execMode) {
    return null;
  }

  @Override
  public Integer execute(Integer input, ExecMode execMode) {
    return null;
  }

  @Override
  public Integer execute(ExecMode execMode) {
    return null;
  }

  @Override
  public Integer executeNonBlocking(Integer verb, Integer noun, ExecMode execMode) {
    return null;
  }

  @Override
  public Integer executeNonBlocking(Integer input, ExecMode execMode) {
    return null;
  }

  @Override
  public Integer executeNonBlocking(ExecMode execMode) {
    return null;
  }

  @Override
  public CompletableFuture<Integer> executeAsync(ExecMode execMode, Executor executor) {
    return null;
  }

  @Override
  public String printMemory() {
    return null;
  }

  @Override
  public String printRegisters() {
    return null;
  }
}
