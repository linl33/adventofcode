package dev.linl33.adventofcode.year2020;

import org.apache.logging.log4j.LogManager;
import sun.misc.Unsafe;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.ToLongFunction;

public class Day14 extends AdventSolution2020<Long, Long> {
  private static final int MAX_FLOATING_BITS = 9;
  private static final int ADDR_BITS = 36;
  private static final Unsafe UNSAFE;
  private static final long baseAddr;

  static {
    try {
      var f = Unsafe.class.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      UNSAFE = (Unsafe) f.get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    var tmpBaseAddr = -1L;
    try {
      tmpBaseAddr = UNSAFE.allocateMemory((1L << ADDR_BITS) * Integer.BYTES);
    } catch (OutOfMemoryError outOfMemoryError) {
      LogManager.getLogger(Day14.class).warn("Unable to allocate memory", outOfMemoryError);
    }

    baseAddr = tmpBaseAddr;
  }

  public static void main(String[] args) {
    new Day14().runAndPrintAll();
  }

  @Override
  public Long part1(BufferedReader reader) {
    var memory = new long[100000];
    var oneMask = new AtomicLong(0);
    var zeroMask = new AtomicLong(0);

    return solve(
        reader,
        maskInstr -> {
          oneMask.setPlain(maskInstr.oneMask());
          zeroMask.setPlain(maskInstr.zeroMask());
        },
        writeInstr -> {
          var newVal = (writeInstr.value() & zeroMask.getPlain()) | (oneMask.getPlain());
          var delta = newVal - memory[writeInstr.addr()];

          memory[writeInstr.addr()] = newVal;

          return delta;
        }
    );
  }

  @Override
  public Long part2(BufferedReader reader) {
    // pick an implementation

    return solveByHashMap(reader);
//    return solveByUnsafeAllocateMemory(reader);
  }

  private static long solveByUnsafeAllocateMemory(BufferedReader reader) {
    // this method takes advantage of vm overcommit to write memory values
    // directly into the virtual memory
    // tested on Linux
    // may have to set /proc/sys/vm/overcommit_memory to 1

    if (baseAddr < 0L) {
      throw new IllegalStateException("Memory allocation failed");
    }

    var oneMask = new AtomicLong(0);
    var floatingMaskCache = new long[1 << MAX_FLOATING_BITS];
    var floatingMaskSize = new AtomicInteger(0);

    return solve(
        reader,
        maskInstr -> {
          oneMask.setPlain(maskInstr.oneMask());
          floatingMaskSize.setPlain(applyFloatingMask(maskInstr.floatingMask(), floatingMaskCache));
        },
        writeInstr -> {
          var value = writeInstr.value();
          var addrWithOrMask = writeInstr.addr() | oneMask.getPlain();
          var delta = 0L;

          var length = floatingMaskSize.getPlain();
          for (int i = 0; i < length; i++) {
            var addr = addrWithOrMask ^ floatingMaskCache[i];
            delta += value - UNSAFE.getInt(baseAddr + addr * Integer.BYTES);
            UNSAFE.putInt(baseAddr + addr * Integer.BYTES, value);
          }

          return delta;
        }
    );
  }

  private static long solveByHashMap(BufferedReader reader) {
    var memory = new HashMap<Long, Integer>();
    var oneMask = new AtomicLong(0);
    var floatingMaskCache = new long[1 << MAX_FLOATING_BITS];
    var floatingMaskSize = new AtomicInteger(0);

    return solve(
        reader,
        maskInstr -> {
          oneMask.setPlain(maskInstr.oneMask());
          floatingMaskSize.setPlain(applyFloatingMask(maskInstr.floatingMask(), floatingMaskCache));
        },
        writeInstr -> {
          var value = writeInstr.value();
          var addrWithOrMask = writeInstr.addr() | oneMask.getPlain();
          var delta = 0L;

          var length = floatingMaskSize.getPlain();
          for (int i = 0; i < length; i++) {
            var addr = addrWithOrMask ^ floatingMaskCache[i];
            delta += value - Objects.requireNonNullElse(memory.put(addr, value), 0);
          }

          return delta;
        }
    );
  }

  private static long solve(BufferedReader reader,
                            Consumer<SetMaskInstr> onSetMask,
                            ToLongFunction<WriteInstr> onWrite) {
    return reader
        .lines()
        .map(InitInstr::parse)
        .reduce(
            0L,
            (sum, instr) -> {
              if (instr instanceof SetMaskInstr maskInstr) {
                onSetMask.accept(maskInstr);
                return sum;
              }

              if (instr instanceof WriteInstr writeInstr) {
                return sum + onWrite.applyAsLong(writeInstr);
              }

              throw new IllegalStateException();
            },
            Long::sum
        );
  }

  private static int applyFloatingMask(long floatingMask, long[] maskOut) {
    var counter = 1; // start from 1 to let maskOut[0] be 0

    var length = Long.SIZE - Long.numberOfLeadingZeros(floatingMask);
    for (int i = Long.numberOfTrailingZeros(floatingMask); i < length; i++) {
      if (((floatingMask >> i) & 1L) != 1L) {
        continue;
      }

      for (int j = 0; j < counter; j++) {
        maskOut[counter + j] = maskOut[j] | (1L << i);
      }

      counter <<= 1;
    }

    return 1 << Long.bitCount(floatingMask);
  }

  private sealed interface InitInstr {
    static InitInstr parse(String instr) {
      var parts = instr.split(" = ");

      if (parts[0].equals("mask")) {
        return new SetMaskInstr(parts[1]);
      }

      return new WriteInstr(
          Integer.parseInt(parts[0], 4, parts[0].length() - 1, 10),
          Integer.parseInt(parts[1])
      );
    }
  }

  private static record SetMaskInstr(String mask,
                                     long oneMask,
                                     long zeroMask,
                                     long floatingMask) implements InitInstr {
    public SetMaskInstr(String mask) {
      this(mask, makeMask(mask, '1', 0L), makeMask(mask, '0', ~0L), makeMask(mask, 'X', 0L));
    }

    private static long makeMask(String mask, char bit, long identity) {
      var res = identity;

      var maskSize = mask.length();
      for (int i = 0; i < maskSize; i++) {
        if (mask.charAt(i) == bit) {
          res ^= 1L << (maskSize - i - 1);
        }
      }

      return res;
    }
  }

  private static record WriteInstr(int addr, int value) implements InitInstr {}
}
