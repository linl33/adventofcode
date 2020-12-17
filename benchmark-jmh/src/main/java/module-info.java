module dev.linl33.adventofcode.jmh {
  requires dev.linl33.adventofcode.lib;

  requires org.apache.logging.log4j;

  requires jmh.core;
  requires static jmh.generator.annprocess;
  requires jdk.unsupported; // used by jmh

  exports dev.linl33.adventofcode.jmh;

  opens dev.linl33.adventofcode.jmh.benchmark.jmh_generated to jmh.core;
}
