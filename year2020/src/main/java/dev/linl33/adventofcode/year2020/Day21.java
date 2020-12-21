package dev.linl33.adventofcode.year2020;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

public class Day21 extends AdventSolution2020<Integer, String> {
  private static final int ID_WIDTH = 6;
  private static final int MAX_ALLERGEN = 8;
  private static final int MAX_ALLERGEN_SHORT_ID = 26 * 26 + 26;
  private static final int MAX_INGR = 1 << 8;
  private static final int MAP_SIZE_MUL = 10;
  private static final String CONTAINS_STR = "(contains ";

  public static void main(String[] args) {
    new Day21().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    var ingrId = new HashMap<Long, Integer>(MAX_INGR * MAP_SIZE_MUL);
    var allergenId = new int[MAX_ALLERGEN_SHORT_ID];
    var allergenIdCounter = 1;

    var ingrName = new int[MAX_INGR];

    var ingrFreq = new int[MAX_INGR];
    var unsafeIngr = new int[MAX_ALLERGEN * ID_WIDTH];
    var knownAllergen = 0;

    var total = 0;
    var mask = new int[ID_WIDTH];

    var lines = reader.lines().toArray(String[]::new);
    for (int row = 0; row < lines.length; row++) {
      String line = lines[row];
      var lineArr = line.toCharArray();
      var allergenStart = line.lastIndexOf(CONTAINS_STR);

      var newAllergen = false;

      for (int i = lineArr.length - 1; i >= allergenStart + 9; i--) {
        if (lineArr[i] == ' ') {
          if (allergenId[allergenShortId(lineArr, i + 1)] == 0) {
            newAllergen = true;
            break;
          }
        }
      }

      Arrays.fill(mask, 0);

      var start = 0;
      for (int i = 0; i < allergenStart; i++) {
        if (lineArr[i] == ' ') {
          if (newAllergen) {
            var name = encodePosition(row, start, i);
            int iId = ingrId.computeIfAbsent(alphabetToLong(lineArr, start, i), __ -> {
              ingrName[ingrId.size()] = name;
              return ingrId.size();
            });
            setMask(mask, iId);
            ingrFreq[iId]++;
          } else {
            // the id here might be null
            // if the ingredient is irrelevant
            var iId = ingrId.get(alphabetToLong(lineArr, start, i));
            if (iId != null) {
              setMask(mask, iId);
              ingrFreq[iId]++;
            }
          }

          start = i + 1;
          total++;
        }
      }

      for (int i = lineArr.length - 1; i >= allergenStart + 9; i--) {
        if (lineArr[i] == ' ') {
          var shortId = allergenShortId(lineArr, i + 1);

          // the id is 1-indexed when stored in the array to distinguish between
          // the id 0 and uninitialized entry
          int aId = allergenId[shortId] - 1;
          if (aId == -1) {
            aId = allergenIdCounter++;
            allergenId[shortId] = aId--;

            System.arraycopy(mask, 0, unsafeIngr, aId * ID_WIDTH, ID_WIDTH);
            knownAllergen |= 1 << aId;
          } else {
            maskIntersect(unsafeIngr, mask, aId * ID_WIDTH);
          }
        }
      }
    }

    Arrays.fill(mask, 0);
    var allergenCount = Integer.bitCount(knownAllergen);
    for (int i = 0; i < allergenCount; i++) {
      maskUnion(mask, unsafeIngr, i * ID_WIDTH);
    }

    var ingrCount = ingrId.size();
    for (int i = 0; i < ingrCount; i++) {
      if (checkMask(mask, i)) {
        total -= ingrFreq[i];

        // the frequency is tracked starting from the row where
        // the ingredient is a part of the first occurrence of an allergen
        // but the ingredient might have appeared earlier
        // check here and correct the total
        var name = decodePosition(lines, ingrName[i]);
        for (int row = (ingrName[i] >> 20) - 1; row >= 0 ; row--) {
          if (lines[row].contains(name)) {
            total--;
          }
        }
      }
    }

    return total;
  }

  @Override
  public String part2(BufferedReader reader) {
    var ingrId = new HashMap<Long, Integer>(MAX_INGR * MAP_SIZE_MUL);
    var allergenId = new int[MAX_ALLERGEN_SHORT_ID];
    var allergenIdCounter = 1;

    var ingrName = new int[MAX_INGR];
    var allergenName = new int[MAX_ALLERGEN];

    var unsafeIngr = new int[MAX_ALLERGEN * ID_WIDTH];
    var knownAllergen = 0;

    var mask = new int[ID_WIDTH];

    var lines = reader.lines().toArray(String[]::new);
    for (int row = 0; row < lines.length; row++) {
      var line = lines[row];
      var lineArr = line.toCharArray();
      var allergenStart = line.lastIndexOf(CONTAINS_STR);

      var newAllergen = false;

      for (int i = lineArr.length - 1; i >= allergenStart + 9; i--) {
        if (lineArr[i] == ' ') {
          if (allergenId[allergenShortId(lineArr, i + 1)] == 0) {
            newAllergen = true;
            break;
          }
        }
      }

      Arrays.fill(mask, 0);

      var start = 0;
      for (int i = 0; i < allergenStart; i++) {
        if (lineArr[i] == ' ') {
          if (newAllergen) {
            var name = encodePosition(row, start, i);
            int iId = ingrId.computeIfAbsent(alphabetToLong(lineArr, start, i), __ -> {
              ingrName[ingrId.size()] = name;
              return ingrId.size();
            });
            setMask(mask, iId);
          } else {
            // the id here might be null
            // if the ingredient is irrelevant
            var iId = ingrId.get(alphabetToLong(lineArr, start, i));
            if (iId != null) {
              setMask(mask, iId);
            }
          }

          start = i + 1;
        }
      }

      start = lineArr.length - 1;
      for (int i = start; i >= allergenStart + 9; i--) {
        if (lineArr[i] == ' ') {
          var shortId = allergenShortId(lineArr, i + 1);

          // the id is 1-indexed when stored in the array to distinguish between
          // the id 0 and uninitialized entry
          int aId = allergenId[shortId] - 1;
          if (aId == -1) {
            aId = allergenIdCounter++;

            allergenId[shortId] = aId--;
            allergenName[aId] = encodePosition(row, i + 1, start);

            System.arraycopy(mask, 0, unsafeIngr, aId * ID_WIDTH, ID_WIDTH);
            knownAllergen |= 1 << aId;
          } else {
            maskIntersect(unsafeIngr, mask, aId * ID_WIDTH);
          }

          start = i - 1;
        }
      }
    }

    var allergenLowBound = Integer.numberOfTrailingZeros(knownAllergen);
    var allergenHighBound = Integer.SIZE - Integer.numberOfLeadingZeros(knownAllergen);

    var found = 0;
    var finalMapping = new TreeMap<String, String>();

    while (found < Integer.bitCount(knownAllergen)) {
      for (int i = allergenLowBound; i < allergenHighBound; i++) {
        if (((knownAllergen >> i) & 1) == 0) {
          continue;
        }

        var sum = 0;
        for (int j = 0; j < ID_WIDTH; j++) {
          sum += Integer.bitCount(unsafeIngr[i * ID_WIDTH + j]);
        }

        if (sum != 1) {
          continue;
        }

        var ingr = 0;
        for (int j = 0; j < ID_WIDTH; j++) {
          if (unsafeIngr[i * ID_WIDTH + j] != 0) {
            ingr = j * Integer.SIZE + Integer.numberOfTrailingZeros(unsafeIngr[i * ID_WIDTH + j]);
            break;
          }
        }

        for (int j = allergenLowBound; j < allergenHighBound; j++) {
          if (((knownAllergen >> j) & 1) == 0) {
            continue;
          }

          unsetMask(unsafeIngr, ingr, j * ID_WIDTH);
        }

        finalMapping.put(decodePosition(lines, allergenName[i]), decodePosition(lines, ingrName[ingr]));
        found++;
      }
    }

    return String.join(",", finalMapping.values());
  }

  private static long alphabetToLong(char[] chars, int start, int end) {
    var result = 0L;
    var mul = 1L;

    for (int i = start; i < end; i++) {
      result += (chars[i] - 'a' + 1) * mul;
      mul *= 26;
    }

    return result;
  }

  private static int alphabetToInt(char[] chars, int start, int end) {
    var result = 0;
    var mul = 1;

    for (int i = start; i < end; i++) {
      result += (chars[i] - 'a' + 1) * mul;
      mul *= 26;
    }

    return result;
  }

  private static int allergenShortId(char[] chars, int start) {
    // generate the allergen ID using the first 2 characters
    // there is enough entropy to avoid collision

    return alphabetToInt(chars, start, start + 2);
  }

  private static void setMask(int[] mask, int pos) {
    mask[pos / Integer.SIZE] |= 1 << (pos % Integer.SIZE);
  }

  private static void unsetMask(int[] mask, int pos, int offset) {
    mask[pos / Integer.SIZE + offset] &= ~(1 << (pos % Integer.SIZE));
  }

  private static boolean checkMask(int[] mask, int pos) {
    return ((mask[pos / Integer.SIZE] >> (pos % Integer.SIZE)) & 1) == 1;
  }

  private static void maskIntersect(int[] target, int[] mask, int offset) {
    for (int i = 0; i < mask.length; i++) {
      target[i + offset] &= mask[i];
    }
  }

  private static void maskUnion(int[] target, int[] mask, int offset) {
    for (int i = 0; i < target.length; i++) {
      target[i] |= mask[i + offset];
    }
  }

  private static int encodePosition(int lineNo, int start, int end) {
    return (lineNo << 20) | (start << 10) | end;
  }

  private static String decodePosition(String[] lines, int position) {
    var end = position & 0b1111111111;
    var start = (position >> 10) & 0b1111111111;
    var lineNo = position >> 20;

    return lines[lineNo].substring(start, end);
  }
}
