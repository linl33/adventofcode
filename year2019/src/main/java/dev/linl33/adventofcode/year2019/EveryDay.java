package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.solution.AdventSolution;

import java.lang.reflect.InvocationTargetException;

public class EveryDay {
  public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    for (int i = 1; i <= 30; i++) {
      try {
        var instance = Class
            .forName(getClassName(i))
            .getConstructor()
            .newInstance();

        if (instance instanceof AdventSolution<?, ?> solutionInstance) {
          solutionInstance.runAndPrintAll();
        }
      } catch (ClassNotFoundException e) {
        // ignore
      }
    }
  }

  private static String getClassName(int day) {
    return EveryDay.class.getPackageName() + ".Day" + day;
  }
}
