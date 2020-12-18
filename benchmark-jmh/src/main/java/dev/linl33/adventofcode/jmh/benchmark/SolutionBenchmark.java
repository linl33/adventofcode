package dev.linl33.adventofcode.jmh.benchmark;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class SolutionBenchmark {
  private AdventSolution<?, ?> solution;
  private String part1Resource;
  private String part2Resource;

  @Param("")
  String solutionClass;

  @Setup
  public void setupSolution() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    solution = (AdventSolution<?, ?>) Class.forName(solutionClass).getConstructor().newInstance();
    part1Resource = solution.getPart1Resource();
    part2Resource = solution.getPart2Resource();
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void benchmarkPart1(Blackhole blackhole) {
    blackhole.consume(solution.run(SolutionPart.PART_1, part1Resource));
  }

  @Benchmark
  @BenchmarkMode(Mode.AverageTime)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  public void benchmarkPart2(Blackhole blackhole) {
    blackhole.consume(solution.run(SolutionPart.PART_2, part2Resource));
  }
}
