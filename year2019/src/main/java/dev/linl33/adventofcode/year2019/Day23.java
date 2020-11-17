package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.year2019.intcodevm.IntcodeUtil;
import dev.linl33.adventofcode.year2019.intcodevm.IntcodeVM;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day23 extends AdventSolution2019<Long, Long> {
  private static final int NAT_ADDR = 255;

  public static void main(String[] args) {
    new Day23().runAndPrintAll();
  }

  @Override
  public Long part1(BufferedReader reader) throws Exception {
    var vmList = initializeVms(reader);

    var emptyInputQueue = new boolean[vmList.size()];

    while (true) {
      for (int i = 0; i < vmList.size(); i++) {
        var vm = vmList.get(i);
        var inputQueue = vm.getInput();
        var outputQueue = vm.getOutput();

        if (inputQueue.isEmpty()) {
          inputQueue.add(-1L);
          emptyInputQueue[i] = true;
        }

        vm.executeNonBlocking(IntcodeVM.ExecMode.STATEFUL);

        while (!outputQueue.isEmpty()) {
          var addr = outputQueue.remove().intValue();
          var xVal = outputQueue.remove();
          var yVal = outputQueue.remove();

          if (addr == NAT_ADDR) {
            return yVal;
          }

          deliveryPacket(addr, xVal, yVal, vmList, emptyInputQueue);
        }
      }
    }
  }

  @Override
  public Long part2(BufferedReader reader) throws Exception {
    var vmList = initializeVms(reader);

    var emptyInputQueue = new boolean[vmList.size()];
    var idle = true;

    var natX = -1L;
    var natY = -1L;

    var lastDeliveredYVal = 0L;

    while (true) {
      idle = true;

      for (int i = 0; i < vmList.size(); i++) {
        var vm = vmList.get(i);
        var inputQueue = vm.getInput();
        var outputQueue = vm.getOutput();

        if (inputQueue.isEmpty()) {
          inputQueue.add(-1L);
          emptyInputQueue[i] = true;
        } else {
          idle = false;
        }

        vm.executeNonBlocking(IntcodeVM.ExecMode.STATEFUL);

        while (!outputQueue.isEmpty()) {
          var addr = outputQueue.remove().intValue();
          var xVal = outputQueue.remove();
          var yVal = outputQueue.remove();

          if (addr == NAT_ADDR) {
            natX = xVal;
            natY = yVal;
          } else {
            deliveryPacket(addr, xVal, yVal, vmList, emptyInputQueue);
          }
        }
      }

      if (idle) {
        if (natY == lastDeliveredYVal) {
          return natY;
        }

        deliveryPacket(0, natX, natY, vmList, emptyInputQueue);
        lastDeliveredYVal = natY;
      }
    }
  }

  private static List<IntcodeVM> initializeVms(BufferedReader reader) {
    var vmMemory = IntcodeUtil.buildMemory(reader);

    return LongStream
        .range(0, 50)
        .mapToObj(i -> new LinkedBlockingDeque<>(List.of(i)))
        .map(inputQueue -> new IntcodeVM(
            Arrays.copyOf(vmMemory, vmMemory.length),
            inputQueue,
            new LinkedBlockingDeque<>()
        ))
        .peek(vm -> vm.executeNonBlocking(IntcodeVM.ExecMode.STATEFUL))
        .collect(Collectors.toUnmodifiableList());
  }

  private static void deliveryPacket(int addr,
                                     long x,
                                     long y,
                                     List<IntcodeVM> vmList,
                                     boolean[] emptyInputQueue) {
    var inputQueue = vmList.get(addr).getInput();

    if (emptyInputQueue[addr]) {
      inputQueue.clear();
      emptyInputQueue[addr] = false;
    }

    inputQueue.add(x);
    inputQueue.add(y);
  }
}
