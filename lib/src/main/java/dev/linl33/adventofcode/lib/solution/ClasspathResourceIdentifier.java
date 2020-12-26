package dev.linl33.adventofcode.lib.solution;

public record ClasspathResourceIdentifier(String name) implements ResourceIdentifier {
  @Override
  public String toString() {
    return name;
  }
}
