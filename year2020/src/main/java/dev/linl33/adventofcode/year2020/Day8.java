package dev.linl33.adventofcode.year2020;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Day8 extends AdventSolution2020<Integer, Integer> {
  public static void main(String[] args) {
    // TODO: run this with IntcodeVM

    new Day8().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    var input = reader
        .lines()
        .map(Instruction::parse)
        .toArray(Instruction[]::new);

    return new Day8Vm(input).run().accu;
  }

  @Override
  public Integer part2(BufferedReader reader) {
    var input = reader
        .lines()
        .map(Instruction::parse)
        .toArray(Instruction[]::new);

    var programSize = input.length;

    // if this program enters a previously seen state, it has to loop infinitely

    return IntStream
        .range(0, programSize)
        .filter(i -> input[i].op != Instruction.OP.ACC)
        .mapToObj(i -> {
          var inputCopy = Arrays.copyOf(input, programSize);
          inputCopy[i] = input[i].invert();

          return new Day8Vm(inputCopy);
        })
        .map(Day8Vm::run)
        .filter(ExecutionResult::terminated)
        .findAny()
        .map(ExecutionResult::accu)
        .orElseThrow();
  }

  private static record Day8Vm(Instruction[] program) {
    public ExecutionResult run() {
      var accu = 0;
      var pc = 0;

      Instruction instr;
      while (pc < program.length && (instr = program[pc]) != null) {
        program[pc] = null;

        if (instr.op == Instruction.OP.JMP) {
          pc += instr.arg;
          continue;
        }

        if (instr.op == Instruction.OP.ACC) {
          accu += instr.arg;
        }

        pc++;
      }

      return new ExecutionResult(pc >= program.length, accu);
    }
  }

  private static record ExecutionResult(boolean terminated, int accu) {}

  private static record Instruction(OP op, int arg) {
    public enum OP {
      ACC, JMP, NOP
    }

    public static Instruction parse(String line) {
      var parts = line.split(" ");
      var arg = Integer.parseInt(parts[1]);

      return switch (parts[0]) {
        case "acc" -> new Instruction(OP.ACC, arg);
        case "jmp" -> new Instruction(OP.JMP, arg);
        case "nop" -> new Instruction(OP.NOP, arg);
        default -> throw new IllegalArgumentException();
      };
    }

    public Instruction invert() {
      if (op == OP.ACC) {
        throw new IllegalArgumentException("Cannot invert ACC");
      }

      return new Instruction(op == OP.NOP ? OP.JMP : OP.NOP, arg);
    }
  }
}
