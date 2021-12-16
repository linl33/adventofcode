package dev.linl33.adventofcode.year2021.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.testlib.StringResourceIdentifier;
import dev.linl33.adventofcode.year2021.Day15;
import dev.linl33.adventofcode.year2021.Day16;

import java.util.Map;

class Day16Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day16();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 847L,
        new StringResourceIdentifier("8A004A801A8002F478"), 16L,
        new StringResourceIdentifier("620080001611562C8802118E34"), 12L,
        new StringResourceIdentifier("C0015000016115A2E0802F182340"), 23L,
        new StringResourceIdentifier("A0016C880162017C3686B18A3D4780"), 31L
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 333794664059L,
        new StringResourceIdentifier("C200B40A82"), 3L,
        new StringResourceIdentifier("04005AC33890"), 54L,
        new StringResourceIdentifier("880086C3E88112"), 7L,
        new StringResourceIdentifier("CE00C43D881120"), 9L,
        new StringResourceIdentifier("D8005AC2A8F0"), 1L,
        new StringResourceIdentifier("F600BC2D8F"), 0L,
        new StringResourceIdentifier("9C005AC2F8F0"), 0L,
        new StringResourceIdentifier("9C0141080250320F1802104A08"), 1L
    );
  }
}
