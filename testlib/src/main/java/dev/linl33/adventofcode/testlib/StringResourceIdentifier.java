package dev.linl33.adventofcode.testlib;

import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;

public record StringResourceIdentifier(String content) implements ResourceIdentifier {
  public StringResourceIdentifier(String content) {
    this.content = content.startsWith("string:") ? content.replaceFirst("string:", "") : content;
  }
  @Override
  public String toString() {
    return "string:" + content;
  }
}
