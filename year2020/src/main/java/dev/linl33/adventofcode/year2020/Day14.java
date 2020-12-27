package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import org.apache.logging.log4j.LogManager;
import sun.misc.Unsafe;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.ToLongFunction;
import java.util.stream.Stream;

public class Day14 extends AdventSolution2020<Long, Long> implements
    ByteBufferAdventSolution<Long, Long>, NullBufferedReaderSolution<Long, Long> {
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
  public Long part1(ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part1(identifier);
  }

    @Override
  public Long part2(ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part2(identifier);
  }

  @Override
  public Long part1(ByteBuffer byteBuffer) throws Exception {
    var memory = new long[100000];
    var oneMask = new AtomicLong(0);
    var zeroMask = new AtomicLong(0);

    return solve(
        byteBuffer,
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
  public Long part2(ByteBuffer byteBuffer) throws Exception {
    // pick an implementation

    return solveByHashMap(byteBuffer);
//    return solveByUnsafeAllocateMemory(byteBuffer);
  }

  private static long solveByUnsafeAllocateMemory(ByteBuffer buffer) {
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
        buffer,
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
            delta += value - UNSAFE.getInt(baseAddr + (addrWithOrMask ^ floatingMaskCache[i]) * Integer.BYTES);
            UNSAFE.putInt(baseAddr + (addrWithOrMask ^ floatingMaskCache[i]) * Integer.BYTES, value);
          }

          return delta;
        }
    );
  }

  private static long solveByHashMap(ByteBuffer buffer) {
    var memory = new HashMap<Long, Integer>();
    var oneMask = new AtomicLong(0);
    var floatingMaskCache = new long[1 << MAX_FLOATING_BITS];
    var floatingMaskSize = new AtomicInteger(0);

    return solve(
        buffer,
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

  private static long solve(ByteBuffer byteBuffer,
                            Consumer<SetMaskInstr> onSetMask,
                            ToLongFunction<WriteInstr> onWrite) {
    var sBuilder = Stream.<InitInstr>builder();
    while (byteBuffer.hasRemaining()) {
      // skip first byte -- always 'm'
      byteBuffer.position(byteBuffer.position() + 1);

      if (byteBuffer.get() == 'a') {
        sBuilder.add(new SetMaskInstr(byteBuffer.slice(byteBuffer.position() + 5, ADDR_BITS)));
        // skip to the next line
        byteBuffer.position(byteBuffer.position() + 5 + ADDR_BITS + 1);
      } else {
        byteBuffer.position(byteBuffer.position() + 2);

        byte b;
        var addr = 0;
        while ((b = byteBuffer.get()) != ']') {
          addr = (addr * 10) + (b - '0');
        }

        byteBuffer.position(byteBuffer.position() + 3);
        var val = 0;
        while ((b = byteBuffer.get()) != '\n') {
          val = (val * 10) + (b - '0');
        }

        sBuilder.add(new WriteInstr(addr, val));
      }
    }

    return sBuilder.build().reduce(
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
      for (int j = 0; j < counter * (floatingMask >> i & 1); j++) {
        maskOut[counter + j] = maskOut[j] | (1L << i);
      }

      counter <<= (floatingMask >> i) & 1;
    }

    return 1 << Long.bitCount(floatingMask);
  }

  private sealed interface InitInstr {}

  private static record SetMaskInstr(long oneMask,
                                     long zeroMask,
                                     long floatingMask) implements InitInstr {
    public SetMaskInstr(ByteBuffer mask) {
      this(makeMask(mask, '1', 0L), makeMask(mask, '0', ~0L), makeMask(mask, 'X', 0L));
    }

    private static long makeMask(ByteBuffer byteBuffer, char bit, long identity) {
      var res = identity;

      for (int i = 0; i < ADDR_BITS; i++) {
        res ^= (byteBuffer.get() != bit) ? 0 : (1L << (ADDR_BITS - i - 1));
      }

      byteBuffer.rewind();

      return res;
    }
  }

  private static record WriteInstr(int addr, int value) implements InitInstr {}
}
