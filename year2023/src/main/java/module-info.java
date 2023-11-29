module dev.linl33.adventofcode.year2023 {
  requires transitive dev.linl33.adventofcode.lib;
  requires dev.linl33.adventofcode.jmh;

  requires org.apache.logging.log4j;
  requires static org.apache.logging.log4j.core;
  requires static org.jetbrains.annotations;

  requires jdk.incubator.vector;

  exports dev.linl33.adventofcode.year2023 to dev.linl33.adventofcode.year2023.test;

  opens dev.linl33.adventofcode.year2023 to dev.linl33.adventofcode.lib, dev.linl33.adventofcode.jmh;
}
