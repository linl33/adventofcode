package dev.linl33.adventofcode.lib.graph;

import java.util.Map;

public record GraphPath<T>(Map<T, T> path, T start, T end, int length) {}
