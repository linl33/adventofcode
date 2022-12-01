package dev.linl33.adventofcode.year2019.intcodevm;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.util.*;
import java.util.concurrent.*;

public class IntcodeVM {
  public enum ExecMode {
    STATELESS, STATEFUL
  }

  public enum Register {
    PC, PC_RESUME, RELATIVE_OFFSET
  }

  private enum State {
    BLOCKING, NULL, SUSPEND, RESUME, HALT
  }

  private static final int TIMEOUT = 3;
  private static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;

  private final long[] memory;
  private final EnumMap<Register, Integer> registers;
  private Integer verb;
  private Integer noun;
  private final BlockingDeque<Long> input;
  private final BlockingDeque<Long> output;
  private State state;

  public long[] getMemory() {
    return memory;
  }

  private EnumMap<Register, Integer> getRegisters() {
    return registers;
  }

  private Optional<Integer> getVerb() {
    return Optional.ofNullable(verb);
  }

  private void setVerb(Integer verb) {
    this.verb = verb;
  }

  private Optional<Integer> getNoun() {
    return Optional.ofNullable(noun);
  }

  private void setNoun(Integer noun) {
    this.noun = noun;
  }

  public BlockingDeque<Long> getInput() {
    return input;
  }

  public BlockingDeque<Long> getOutput() {
    return output;
  }

  public boolean hasSuspended() {
    return state == State.SUSPEND;
  }

  public boolean hasHalted() {
    return state == State.HALT;
  }

  public IntcodeVM(long[] memory, BlockingDeque<Long> input, BlockingDeque<Long> output) {
    this(memory, null, input, output);
  }

  public IntcodeVM(long[] memory) {
    this(memory, null, null);
  }

  public IntcodeVM(BufferedReader reader) {
    this(IntcodeUtil.buildMemory(reader));
  }

  private IntcodeVM(long[] memory,
                    @Nullable EnumMap<Register, Integer> registers,
                    @Nullable BlockingDeque<Long> input,
                    @Nullable BlockingDeque<Long> output) {
    this.memory = memory;
    this.registers = Objects.requireNonNullElseGet(registers, () -> new EnumMap<>(Register.class));
    this.verb = null;
    this.noun = null;
    this.input = Objects.requireNonNullElseGet(input, LinkedBlockingDeque::new);
    this.output = Objects.requireNonNullElseGet(output, LinkedBlockingDeque::new);
    this.state = State.NULL;

    initializeRegistersIfNeeded();
  }

  public long execute(int verb, int noun, ExecMode mode) {
    setVerb(verb);
    setNoun(noun);

    return executeInternal(mode, false)[0];
  }

  public long executeNonBlocking(int verb, int noun, ExecMode mode) {
    setVerb(verb);
    setNoun(noun);

    return executeInternal(mode, true)[0];
  }

  public IntcodeVM execute(long input, ExecMode mode) {
    getInput().add(input);
    executeInternal(mode, false);

    return this;
  }

  public IntcodeVM execute(ExecMode mode) {
    executeInternal(mode, false);

    return this;
  }

  public IntcodeVM executeNonBlocking(ExecMode mode) {
    executeInternal(mode, true);

    return this;
  }

  public CompletableFuture<IntcodeVM> executeAsync(ExecMode mode, Executor executor) {
    return CompletableFuture.supplyAsync(() -> execute(mode), executor);
  }

  public IntcodeVM fork() {
    var memory = Arrays.copyOf(getMemory(), getMemory().length);
    var registers = new EnumMap<>(getRegisters());

    return new IntcodeVM(memory, registers, getInput(), getOutput());
  }

  public IntcodeVM forkNewIO() {
    // TODO: clean up these 2 forks

    var memory = Arrays.copyOf(getMemory(), getMemory().length);
    var registers = new EnumMap<>(getRegisters());

    var fork = new IntcodeVM(memory, registers, null, null);
    fork.state = state;
    return fork;
  }

  public String printMemory() {
    return Arrays.toString(getMemory());
  }

  public String printRegisters() {
    return getRegisters().toString();
  }

  private void initializeRegistersIfNeeded() {
    getRegisters().putIfAbsent(Register.PC, 0);
    getRegisters().putIfAbsent(Register.PC_RESUME, 0);
    getRegisters().putIfAbsent(Register.RELATIVE_OFFSET, 0);
  }

  private long[] executeInternal(ExecMode mode, boolean nonBlocking) {
    if (state == State.HALT) {
      state = State.NULL;
    }

    if (state == State.SUSPEND) {
      state = State.RESUME;
      registers.put(Register.PC, registers.get(Register.PC_RESUME));
    }

    if (!nonBlocking) {
      state = State.BLOCKING;
    }

    long[] memory;
    if (mode == ExecMode.STATELESS) {
      memory = new long[getMemory().length];
      System.arraycopy(getMemory(), 0, memory, 0, getMemory().length);
    } else {
      memory = getMemory();
    }

    Map<Register, Integer> registers;
    if (mode == ExecMode.STATELESS) {
      registers = new EnumMap<>(getRegisters());
    } else {
      registers = getRegisters();
    }
    if (state == State.BLOCKING || state == State.NULL) {
      registers.put(Register.PC, 0);
    }

    getVerb().ifPresent(v -> memory[1] = v);
    getNoun().ifPresent(n -> memory[2] = n);

    var pc = new RegisterAccessor(registers, Register.PC);
    var pcResume = new RegisterAccessor(registers, Register.PC_RESUME);
    var relativeOffset = new RegisterAccessor(registers, Register.RELATIVE_OFFSET);
    while (pc.get() > -1) {
      var mainMemoryAccessor = new MainMemoryAccessor(memory, pc.get());
      var decodedInstr = DecodedInstruction.decode(mainMemoryAccessor);
      var accessors = decodeModes(mainMemoryAccessor, decodedInstr, registers);

      // TODO: consider instanceof pattern matching for the switch
      //       it should allow me to specialize the accessors
      //       and avoid addressing the accessors by index

      switch (decodedInstr.op) {
        case Instruction.ADD -> {
          accessors.get(2).set(accessors.get(0).get() + accessors.get(1).get());
          pc.add(decodedInstr.width());
        }

        case Instruction.MUL -> {
          accessors.get(2).set(accessors.get(0).get() * accessors.get(1).get());
          pc.add(decodedInstr.width());
        }

        // TODO: generalize input/output handling with input/output callback
        //       primary use case is day23, to supply default value when input is empty

        case Instruction.INPUT -> {
          if (nonBlocking) {
            var inputVal = getInput().pollFirst();
            if (inputVal == null) {
              pcResume.set(pc);
              pc.set(-2);
            } else {
              accessors.get(0).set(inputVal);
              pc.add(decodedInstr.width());
            }
          } else {
            try {
              var inputNext = getInput().pollFirst(TIMEOUT, TIMEOUT_UNIT);
              if (inputNext == null) {
                System.out.println("WARNING: INPUT WAIT TIMEOUT");
              }

              accessors.get(0).set(inputNext);
              pc.add(decodedInstr.width());
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          }
        }

        case Instruction.OUTPUT -> {
          if (nonBlocking) {
            var added = getOutput().offerLast(accessors.get(0).get());
            if (added) {
              pc.add(decodedInstr.width());
            } else {
              pcResume.set(pc);
              pc.set(-2);
            }
          } else {
            try {
              var added = getOutput().offerLast(accessors.get(0).get(), TIMEOUT, TIMEOUT_UNIT);
              if (!added) {
                System.out.println("WARNING: OUTPUT WAIT TIMEOUT");
              }

              pc.add(decodedInstr.width());
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          }
        }

        case Instruction.JNZ -> {
          if (accessors.get(0).get() != 0L) {
            pc.set(accessors.get(1).get().intValue());
          } else {
            pc.add(decodedInstr.width());
          }
        }

        case Instruction.JZ -> {
          if (accessors.get(0).get() == 0L) {
            pc.set(accessors.get(1).get().intValue());
          } else {
            pc.add(decodedInstr.width());
          }
        }

        case Instruction.LT -> {
          accessors.get(2).set(accessors.get(0).get() < accessors.get(1).get() ? 1L : 0L);
          pc.add(decodedInstr.width());
        }

        case Instruction.EQ -> {
          accessors.get(2).set(accessors.get(0).get().equals(accessors.get(1).get()) ? 1L : 0L);
          pc.add(decodedInstr.width());
        }

        case Instruction.SRO -> {
          relativeOffset.add(accessors.get(0).get().intValue());
          pc.add(decodedInstr.width());
        }

        case Instruction.HALT -> pc.set(-1);

        default -> throw new IllegalInstructionException(decodedInstr.op, pc.get());
      }
    }

    state = switch (pc.get()) {
      case -1 -> State.HALT;
      case -2 -> State.SUSPEND;
      default -> throw new IllegalStateException();
    };

    return memory;
  }

  private static List<? extends MemoryAccessor<Long>> decodeModes(MainMemoryAccessor memoryAccessor,
                                                                  DecodedInstruction instruction,
                                                                  Map<Register, Integer> registers) {
    if (instruction.width() < 2) {
      return List.of();
    }

    var remainingParams = instruction.width() - 1;
    var modes = memoryAccessor.get() / 100;
    var addr = memoryAccessor.getAddr();
    var paramsArr = new MainMemoryAccessor[remainingParams];
    do {
      var mode = modes % 10;
      modes /= 10;

      var accessor = MainMemoryAccessor.derive(memoryAccessor, ++addr);
      if (mode == 0) {
        accessor = MainMemoryAccessor.derive(accessor, accessor.get().intValue());
      } else if (mode == 2) {
        accessor = MainMemoryAccessor.derive(accessor, accessor.get().intValue() + registers.get(Register.RELATIVE_OFFSET));
      }

      paramsArr[paramsArr.length - remainingParams] = accessor;
    } while (--remainingParams > 0);

    return List.of(paramsArr);
  }

  private static record DecodedInstruction(int op, int width) {
    public static DecodedInstruction decode(MainMemoryAccessor memoryAccessor) {
      var op = (int) (memoryAccessor.get() % 100);

      var width = 1;
      if (op >= 1 && op <= 9) {
        width++;

        if (op == 1 || op == 2 || (op >= 5 && op <= 8)) {
          width++;

          if (op == 1 || op == 2 || op == 7 || op == 8) {
            width++;
          }
        }
      }

      return new DecodedInstruction(op, width);
    }
  }
}
