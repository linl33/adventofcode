module dev.linl33.adventofcode.lib {
  requires static org.jetbrains.annotations;
  requires org.apache.logging.log4j;

  requires jmh.core;
  requires static jmh.generator.annprocess;
  requires jdk.unsupported; // used by jmh

  exports dev.linl33.adventofcode.lib;
  exports dev.linl33.adventofcode.lib.function;
  exports dev.linl33.adventofcode.lib.graph;
  exports dev.linl33.adventofcode.lib.graph.intgraph;
  exports dev.linl33.adventofcode.lib.graph.mutable;
  exports dev.linl33.adventofcode.lib.grid;
  exports dev.linl33.adventofcode.lib.point;
  exports dev.linl33.adventofcode.lib.solution;
  exports dev.linl33.adventofcode.lib.util;

  opens dev.linl33.adventofcode.lib.solution.benchmark.jmh_generated to jmh.core;
}
