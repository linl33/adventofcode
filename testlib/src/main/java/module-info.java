module dev.linl33.adventofcode.testlib {
  requires transitive dev.linl33.adventofcode.lib;
  requires transitive org.junit.jupiter.api;
  requires transitive org.junit.jupiter.params;

  exports dev.linl33.adventofcode.testlib;

  opens dev.linl33.adventofcode.testlib to org.junit.platform.commons;
}
