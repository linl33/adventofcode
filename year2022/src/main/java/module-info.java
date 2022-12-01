module dev.linl33.adventofcode.year2022 {
  requires transitive dev.linl33.adventofcode.lib;
  requires dev.linl33.adventofcode.jmh;

  requires org.apache.logging.log4j;
  requires static org.apache.logging.log4j.core;
  requires static org.jetbrains.annotations;

  exports dev.linl33.adventofcode.year2022 to dev.linl33.adventofcode.year2022.test;

  opens dev.linl33.adventofcode.year2022 to dev.linl33.adventofcode.lib, dev.linl33.adventofcode.jmh;
}
