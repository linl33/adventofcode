package dev.linl33.adventofcode.year2019.intcodevm;

public interface Instruction {
  int ADD = 1;
  int MUL = 2;
  int INPUT = 3;
  int OUTPUT = 4;
  int JNZ = 5;
  int JZ = 6;
  int LT = 7;
  int EQ = 8;
  int SRO = 9;
  int HALT = 99;
}
