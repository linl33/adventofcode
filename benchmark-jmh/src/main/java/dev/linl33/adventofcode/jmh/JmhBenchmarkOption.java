package dev.linl33.adventofcode.jmh;

import dev.linl33.adventofcode.jmh.benchmark.SolutionBenchmark;
import dev.linl33.adventofcode.lib.benchmark.BenchmarkOption;
import org.openjdk.jmh.profile.*;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;

public enum JmhBenchmarkOption implements BenchmarkOption {
  PART_1 {
    @Override
    public ChainedOptionsBuilder applyOption(ChainedOptionsBuilder builder) {
      return builder.include(SolutionBenchmark.class.getSimpleName() + ".benchmarkPart1");
    }
  },
  PART_2 {
    @Override
    public ChainedOptionsBuilder applyOption(ChainedOptionsBuilder builder) {
      return builder.include(SolutionBenchmark.class.getSimpleName() + ".benchmarkPart2");
    }
  },
  PROFILE {
    @Override
    public ChainedOptionsBuilder applyOption(ChainedOptionsBuilder builder) {
      return builder.addProfiler(JavaFlightRecorderProfiler.class, "stackDepth=2048");
    }
  },
  GC_PROFILE {
    @Override
    public ChainedOptionsBuilder applyOption(ChainedOptionsBuilder builder) {
      return builder.addProfiler(GCProfiler.class, "churn=true");
    }
  },
  PERF_PROFILE {
    @Override
    public ChainedOptionsBuilder applyOption(ChainedOptionsBuilder builder) {
      return builder
          .addProfiler(LinuxPerfAsmProfiler.class, "skipInterpreter=true;saveLog=true;saveLogTo=logs;intelSyntax=true;hotThreshold=0.05;drawIntraJumps=true;drawInterJumps=true")
          .addProfiler(LinuxPerfNormProfiler.class);
    }
  },
  STACK_PROFILE {
    @Override
    public ChainedOptionsBuilder applyOption(ChainedOptionsBuilder builder) {
      return builder.addProfiler(StackProfiler.class, "detailLine=true;lines=24");
    }
  },
  ;

  public abstract ChainedOptionsBuilder applyOption(ChainedOptionsBuilder builder);
}
