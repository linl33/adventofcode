package dev.linl33.adventofcode.lib.solution;

import java.io.BufferedReader;
import java.lang.foreign.Arena;
import java.lang.foreign.SegmentAllocator;
import java.nio.channels.FileChannel;

public interface ResourceService {
  BufferedReader asBufferedReader(ResourceIdentifier identifier);

  FileChannel asFileChannel(ResourceIdentifier identifier);

  default SegmentAllocator bufferAllocator() {
    return Arena.ofAuto();
  }
}
