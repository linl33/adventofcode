module dev.linl33.adventofcode.lib {
  requires static org.jetbrains.annotations;
  requires org.apache.logging.log4j;

  requires jdk.incubator.vector;

  exports dev.linl33.adventofcode.lib;
  exports dev.linl33.adventofcode.lib.benchmark;
  exports dev.linl33.adventofcode.lib.function;
  exports dev.linl33.adventofcode.lib.graph;
  exports dev.linl33.adventofcode.lib.graph.intgraph;
  exports dev.linl33.adventofcode.lib.graph.mutable;
  exports dev.linl33.adventofcode.lib.grid;
  exports dev.linl33.adventofcode.lib.point;
  exports dev.linl33.adventofcode.lib.solution;
  exports dev.linl33.adventofcode.lib.util;
}
