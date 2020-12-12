module dev.linl33.adventofcode.year2019 {
  requires transitive dev.linl33.adventofcode.lib;

  requires org.apache.logging.log4j;
  requires static org.apache.logging.log4j.core;
  requires static org.jetbrains.annotations;

  exports dev.linl33.adventofcode.year2019 to dev.linl33.adventofcode.year2019.test;

  opens dev.linl33.adventofcode.year2019 to dev.linl33.adventofcode.lib;
}
