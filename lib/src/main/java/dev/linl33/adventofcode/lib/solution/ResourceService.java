package dev.linl33.adventofcode.lib.solution;

import java.io.BufferedReader;
import java.nio.channels.FileChannel;

public interface ResourceService {
  BufferedReader asBufferedReader(ResourceIdentifier identifier);

  FileChannel asFileChannel(ResourceIdentifier identifier);
}
