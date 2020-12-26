package dev.linl33.adventofcode.lib.solution;

public interface ResourceServiceHolder {
  ResourceService getResourceService();

  void setResourceService(ResourceService resourceService);
}
