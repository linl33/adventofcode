package dev.linl33.adventofcode.jmh;

import dev.linl33.adventofcode.lib.benchmark.AdventSolutionBenchmark;
import dev.linl33.adventofcode.lib.benchmark.BenchmarkOption;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public interface JmhSolutionBenchmark<T1, T2> extends AdventSolutionBenchmark<T1, T2> {
  @Override
  default void benchmark(BenchmarkOption... options) {
    options = Objects.requireNonNullElse(options, new JmhBenchmarkOption[] {
        JmhBenchmarkOption.PART_1,
        JmhBenchmarkOption.PART_2
    });

    var opt = Arrays
        .stream(options)
        .mapMulti((Object o, Consumer<JmhBenchmarkOption> accu) -> {
          if (o instanceof JmhBenchmarkOption jmhOpt) {
            accu.accept(jmhOpt);
          }
        })
        .reduce(
            (ChainedOptionsBuilder) new OptionsBuilder(),
            (builder, jmhOpt) -> jmhOpt.applyOption(builder),
            (left, right) -> left
        )
        .param("solutionClass", getClass().getName())
//        .jvmArgsPrepend("-XX:+UseParallelGC")
//        .warmupIterations(2)
//        .measurementIterations(2)
        .forks(1)
        .build();

    try {
      new Runner(opt).run();
    } catch (RunnerException e) {
      throw new RuntimeException(e);
    }
  }
}
