package dev.linl33.adventofcode.jmh;

import dev.linl33.adventofcode.jmh.benchmark.SolutionBenchmark;
import dev.linl33.adventofcode.lib.benchmark.BenchmarkOption;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.profile.JavaFlightRecorderProfiler;
import org.openjdk.jmh.profile.LinuxPerfAsmProfiler;
import org.openjdk.jmh.profile.LinuxPerfNormProfiler;
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
      return builder.addProfiler(GCProfiler.class);
    }
  },
  PERF_PROFILE {
    @Override
    public ChainedOptionsBuilder applyOption(ChainedOptionsBuilder builder) {
      return builder
          .addProfiler(LinuxPerfAsmProfiler.class, "skipInterpreter=true;saveLog=true;saveLogTo=logs;intelSyntax=true")
          .addProfiler(LinuxPerfNormProfiler.class);
    }
  };

  public abstract ChainedOptionsBuilder applyOption(ChainedOptionsBuilder builder);
}
