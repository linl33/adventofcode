package dev.linl33.adventofcode.lib.util;

import dev.linl33.adventofcode.lib.util.internal.LoggingUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PrintUtil {
  public static final char FULL_BLOCK = '█';
  public static final char LIGHT_SHADE = '░';
  public static final char BLACK_CIRCLE = '●';

  private static final Logger LOGGER = LogManager.getLogger(PrintUtil.class);

  public static void enhancedPrint(Object toPrint) {
    if (toPrint == null) {
      return;
    }

    var toPrintClass = toPrint.getClass();

    if (toPrintClass.isArray()) {
      var arrCompType = toPrintClass.getComponentType();

      if (!arrCompType.isArray()) {
        printArray(toPrint);
      } else {
        var arr = (Object[]) toPrint;
        if (!arrCompType.getComponentType().isArray()) {
          // to print 2D array
          for (Object nested : arr) {
            printArray(nested);
          }
        } else {
          // to print 3D or higher array
          logResult(toPrint);
        }
      }
    } else if (toPrint instanceof Iterable<?> iterableToPrint) {
      for (var printItem : iterableToPrint) {
        enhancedPrint(printItem);
      }
    } else {
      logResult(toPrint);
    }
  }

  public static void printArray(Object array) {
    String strToPrint;

    if (array instanceof boolean[] arr) {
      strToPrint = Arrays.toString(arr);
    } else if (array instanceof byte[] arr) {
      strToPrint = Arrays.toString(arr);
    } else if (array instanceof char[] arr) {
      strToPrint = new String(arr);
    } else if (array instanceof double[] arr) {
      strToPrint = Arrays.toString(arr);
    } else if (array instanceof float[] arr) {
      strToPrint = Arrays.toString(arr);
    } else if (array instanceof int[] arr) {
      strToPrint = Arrays.toString(arr);
    } else if (array instanceof long[] arr) {
      strToPrint = Arrays.toString(arr);
    } else {
      strToPrint = Arrays.toString((Object[]) array);
    }

    logResult(strToPrint);
  }

  public static <K, V extends Comparable<V>> void printSorted(Map<K, V> map) {
    printSorted(map, null, null, null, null);
  }

  public static <K, V extends Comparable<V>> void printSorted(Map<K, V> map,
                                                              Long limit,
                                                              Predicate<Map.Entry<K, V>> takeWhile,
                                                              Predicate<Map.Entry<K, V>> filter,
                                                              Function<Stream<Map.Entry<K, V>>, Stream<?>> streamTransform) {
    var stream = map
        .entrySet()
        .stream()
        .sorted(Map.Entry.<K, V>comparingByValue().reversed());

    if (filter != null) {
      stream = stream.filter(filter);
    }

    if (limit != null) {
      stream = stream.limit(limit);
    }

    if (takeWhile != null) {
      stream = stream.takeWhile(takeWhile);
    }

    Stream<?> finalStream = stream;

    if (streamTransform != null) {
      finalStream = streamTransform.apply(stream);
    }

    finalStream.forEach(PrintUtil::logResult);
  }

  private static void logResult(Object toLog) {
    LOGGER.info(LoggingUtil.SOLUTION_RESULT_MARKER, toLog);
  }
}
