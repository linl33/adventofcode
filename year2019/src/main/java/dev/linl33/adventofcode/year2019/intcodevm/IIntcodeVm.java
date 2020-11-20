package dev.linl33.adventofcode.year2019.intcodevm;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface IIntcodeVm<TData> {
  enum ExecMode {
    // TODO: fold blocking/nonblocking into here
    // TODO: add debug
    STATELESS, STATEFUL
  }

  enum Register {
    PC, PC_RESUME, RELATIVE_OFFSET
  }

  enum State {
    BLOCKING, NULL, SUSPEND, RESUME, HALT
  }

  TData execute(TData verb, TData noun, ExecMode execMode);
  TData execute(TData input, ExecMode execMode);
  TData execute(ExecMode execMode);

  TData executeNonBlocking(TData verb, TData noun, ExecMode execMode);
  TData executeNonBlocking(TData input, ExecMode execMode);
  TData executeNonBlocking(ExecMode execMode);

  CompletableFuture<TData> executeAsync(ExecMode execMode, Executor executor);

  String printMemory();
  String printRegisters();
}
