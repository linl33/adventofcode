package dev.linl33.adventofcode.lib;

import java.util.Map;

public record GraphPath<T>(Map<T, T> path, int length) {}
