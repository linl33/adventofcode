package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.year2019.intcodevm.IntcodeUtil;
import dev.linl33.adventofcode.year2019.intcodevm.IntcodeVM;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class Day7 extends AdventSolution2019<Long, Long> {
  public static void main(String[] args) {
    new Day7().runAndPrintAll();
  }

  @Override
  public Long part1(BufferedReader reader) {
    var vm = new IntcodeVM(reader);
    var signals = new HashSet<Long>();

    for (int a = 0; a <= 4; a++) {
      for (int b = 0; b <= 4; b++) {
        for (int c = 0; c <= 4; c++) {
          for (int d = 0; d <= 4; d++) {
            for (int e = 0; e <= 4; e++) {
              if (new HashSet<>(Arrays.asList(a, b, c, d, e)).size() != 5) {
                continue;
              }

              vm.getInput().add((long) a);
              vm.getInput().add(0L);
              vm.execute(IntcodeVM.ExecMode.STATELESS);

              vm.getInput().add((long) b);
              vm.getInput().add(vm.getOutput().getLast());
              vm.execute(IntcodeVM.ExecMode.STATELESS);

              vm.getInput().add((long) c);
              vm.getInput().add(vm.getOutput().getLast());
              vm.execute(IntcodeVM.ExecMode.STATELESS);

              vm.getInput().add((long) d);
              vm.getInput().add(vm.getOutput().getLast());
              vm.execute(IntcodeVM.ExecMode.STATELESS);

              vm.getInput().add((long) e);
              vm.getInput().add(vm.getOutput().getLast());
              vm.execute(IntcodeVM.ExecMode.STATELESS);

              signals.add(vm.getOutput().getLast());
            }
          }
        }
      }
    }

    return Collections.max(signals);
  }

  @Override
  public Long part2(BufferedReader reader) {
    var memory = IntcodeUtil.buildMemory(reader);
    var signals = new HashSet<Long>();

    var qA = new LinkedBlockingDeque<Long>();
    var qB = new LinkedBlockingDeque<Long>();
    var qC = new LinkedBlockingDeque<Long>();
    var qD = new LinkedBlockingDeque<Long>();
    var qE = new LinkedBlockingDeque<Long>();

    var vmA = new IntcodeVM(memory, qE, qA);
    var vmB = new IntcodeVM(memory, qA, qB);
    var vmC = new IntcodeVM(memory, qB, qC);
    var vmD = new IntcodeVM(memory, qC, qD);
    var vmE = new IntcodeVM(memory, qD, qE);

    var ex = Executors.newWorkStealingPool(5);

    for (int a = 5; a <= 9; a++) {
      for (int b = 5; b <= 9; b++) {
        for (int c = 5; c <= 9; c++) {
          for (int d = 5; d <= 9; d++) {
            for (int e = 5; e <= 9; e++) {
              if (new HashSet<>(Arrays.asList(a, b, c, d, e)).size() == 5) {
                qE.add((long) a);
                qE.add(0L);

                qA.add((long) b);
                qB.add((long) c);
                qC.add((long) d);
                qD.add((long) e);

                CompletableFuture.allOf(
                    vmA.executeAsync(IntcodeVM.ExecMode.STATELESS, ex),
                    vmB.executeAsync(IntcodeVM.ExecMode.STATELESS, ex),
                    vmC.executeAsync(IntcodeVM.ExecMode.STATELESS, ex),
                    vmD.executeAsync(IntcodeVM.ExecMode.STATELESS, ex),
                    vmE.executeAsync(IntcodeVM.ExecMode.STATELESS, ex)
                ).join();

                signals.add(vmE.getOutput().pollLast());
              }
            }
          }
        }
      }
    }

    return Collections.max(signals);
  }

  public Long part2SingleThread(BufferedReader reader) {
    var memory = IntcodeUtil.buildMemory(reader);
    var signals = new HashSet<Long>();

    var qA = new LinkedBlockingDeque<Long>(1);
    var qB = new LinkedBlockingDeque<Long>(1);
    var qC = new LinkedBlockingDeque<Long>(1);
    var qD = new LinkedBlockingDeque<Long>(1);
    var qE = new LinkedBlockingDeque<Long>(2);

    for (int a = 5; a <= 9; a++) {
      for (int b = 5; b <= 9; b++) {
        for (int c = 5; c <= 9; c++) {
          for (int d = 5; d <= 9; d++) {
            for (int e = 5; e <= 9; e++) {
              if (new HashSet<>(Arrays.asList(a, b, c, d, e)).size() == 5) {
                var vmA = new IntcodeVM(Arrays.copyOf(memory, memory.length), qE, qA);
                var vmB = new IntcodeVM(Arrays.copyOf(memory, memory.length), qA, qB);
                var vmC = new IntcodeVM(Arrays.copyOf(memory, memory.length), qB, qC);
                var vmD = new IntcodeVM(Arrays.copyOf(memory, memory.length), qC, qD);
                var vmE = new IntcodeVM(Arrays.copyOf(memory, memory.length), qD, qE);

                qE.add((long) a);
                qE.add(0L);

                qA.add((long) b);
                qB.add((long) c);
                qC.add((long) d);
                qD.add((long) e);

                do {
                  if (!vmA.hasHalted()) {
                    vmA.executeNonBlocking(IntcodeVM.ExecMode.STATEFUL);
                  }

                  if (!vmB.hasHalted()) {
                    vmB.executeNonBlocking(IntcodeVM.ExecMode.STATEFUL);
                  }

                  if (!vmC.hasHalted()) {
                    vmC.executeNonBlocking(IntcodeVM.ExecMode.STATEFUL);
                  }

                  if (!vmD.hasHalted()) {
                    vmD.executeNonBlocking(IntcodeVM.ExecMode.STATEFUL);
                  }

                  vmE.executeNonBlocking(IntcodeVM.ExecMode.STATEFUL);
                } while (!vmE.hasHalted());

                signals.add(vmE.getOutput().pollLast());
              }
            }
          }
        }
      }
    }

    return Collections.max(signals);
  }
}
