package dev.linl33.adventofcode.year2019.intcodevm;

public class IllegalInstructionException extends RuntimeException {
  public IllegalInstructionException(long op, int position) {
    super("Illegal instruction {" + op + "} at position {" + position + "}");
  }
}
