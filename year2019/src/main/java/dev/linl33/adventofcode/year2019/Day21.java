package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.year2019.intcodevm.IntcodeVM;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day21 extends AdventSolution2019<Integer, Integer> {
  public static void main(String[] args) {
    new Day21().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    var vm = new IntcodeVM(reader);

    /*
    Jump when there is a hole in A, B, or C
    And that D is solid

    NOT A T
    NOT B J
    OR T J  // J is true when either A or B is a hole
    NOT C T
    OR T J  // J is true when either A, B, or C is a hole
    AND D J
    WALK
     */

    writeInstruction(vm, new SpringScriptInstruction("NOT", "A", "T"));
    writeInstruction(vm, new SpringScriptInstruction("NOT", "B", "J"));
    writeInstruction(vm, new SpringScriptInstruction("OR", "T", "J"));
    writeInstruction(vm, new SpringScriptInstruction("NOT", "C", "T"));
    writeInstruction(vm, new SpringScriptInstruction("OR", "T", "J"));
    writeInstruction(vm, new SpringScriptInstruction("AND", "D", "J"));
    writeInstruction(vm, new SpringScriptInstruction("WALK"));

    return vm
        .executeNonBlocking(IntcodeVM.ExecMode.STATELESS)
        .getOutput()
        .getLast()
        .intValue();
  }

  @Override
  public Integer part2(BufferedReader reader) throws Exception {
    var vm = new IntcodeVM(reader);

    /*
    Jump when the following conditions are satisfied
    1. A, B, or C is a hole
    2. If E is a hole then H must be solid
    3. D is solid

    NOT A T
    NOT B J
    OR T J  // J is true when either A or B is a hole
    NOT C T
    OR T J  // J is true when either A, B, or C is a hole

    NOT E T
    AND H T // T is true when E is a hole and H is solid
    OR E T  // T is true when E is solid or T was set to true by the previous line
    AND T J // J is true when the above condition is satisfied or when J was set to tue

    AND D J
    RUN
     */

    writeInstruction(vm, new SpringScriptInstruction("NOT", "A", "T"));
    writeInstruction(vm, new SpringScriptInstruction("NOT", "B", "J"));
    writeInstruction(vm, new SpringScriptInstruction("OR", "T", "J"));
    writeInstruction(vm, new SpringScriptInstruction("NOT", "C", "T"));
    writeInstruction(vm, new SpringScriptInstruction("OR", "T", "J"));

    writeInstruction(vm, new SpringScriptInstruction("NOT", "E", "T"));
    writeInstruction(vm, new SpringScriptInstruction("AND", "H", "T"));
    writeInstruction(vm, new SpringScriptInstruction("OR", "E", "T"));
    writeInstruction(vm, new SpringScriptInstruction("AND", "T", "J"));

    writeInstruction(vm, new SpringScriptInstruction("AND", "D", "J"));
    writeInstruction(vm, new SpringScriptInstruction("RUN"));

    return vm
        .executeNonBlocking(IntcodeVM.ExecMode.STATELESS)
        .getOutput()
        .getLast()
        .intValue();
  }

  private void writeInstruction(@NotNull IntcodeVM vm, @NotNull SpringScriptInstruction instruction) {
    var str = Stream
        .of(instruction.op(), instruction.operandLeft(), instruction.operandRight())
        .filter(Objects::nonNull)
        .collect(Collectors.collectingAndThen(Collectors.joining(" ", "", "\n"), String::toCharArray));

    for (char c : str) {
      vm.getInput().add((long) c);
    }
  }

  private static record SpringScriptInstruction(@NotNull String op,
                                                @Nullable String operandLeft,
                                                @Nullable String operandRight) {
    public SpringScriptInstruction {
      Objects.requireNonNull(op);
      if ((operandLeft == null) != (operandRight == null)) {
        throw new IllegalArgumentException();
      }
    }

    public SpringScriptInstruction(@NotNull String op) {
      this(op, null, null);
    }
  }
}
