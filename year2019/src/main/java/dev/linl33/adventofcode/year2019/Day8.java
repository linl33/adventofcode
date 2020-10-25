package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import dev.linl33.adventofcode.lib.util.PrintUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Day8 extends AdventSolution2019<Integer, char[][]> {
  private static final int WIDTH = 25;
  private static final int HEIGHT = 6;

  public static void main(String[] args) {
    new Day8().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws IOException {
    return buildImageLayers(reader)
        .stream()
        .map(Day8::countPixel)
        .min(Comparator.comparing(m -> m.getOrDefault('0', 0)))
        .map(freq -> freq.get('1') * freq.get('2'))
        .orElseThrow();
  }

  @Override
  public char[][] part2(BufferedReader reader) throws IOException {
    var layers = buildImageLayers(reader);
    var finalImage = new char[HEIGHT][WIDTH];

    for (int line = 0; line < finalImage.length; line++) {
      nextPixel:
      for (int pixel = 0; pixel < finalImage[line].length; pixel++) {
        for (char[][] layer : layers) {
          if (finalImage[line][pixel] == '0' || finalImage[line][pixel] == '1') {
            continue nextPixel;
          }

          finalImage[line][pixel] = layer[line][pixel];
        }
      }
    }

    return finalImage;
  }

  @Override
  public Object part2PrintMapping(char[][] image) {
    for (char[] chars : image) {
      for (int i = 0; i < chars.length; i++) {
        if (chars[i] == '1') {
          chars[i] = PrintUtil.FULL_BLOCK;
        } else if (chars[i] == '0' || chars[i] == '2') {
          chars[i] = ' ';
        }
      }
    }

    return image;
  }

  private static List<char[][]> buildImageLayers(BufferedReader reader) throws IOException {
    var imageLayers = new ArrayList<char[][]>();

    while (true) {
      var lines = new char[HEIGHT][WIDTH];

      for (char[] line : lines) {
        int read = reader.read(line);
        if (read < WIDTH) {
          return imageLayers;
        }
      }

      imageLayers.add(lines);
    }
  }

  private static Map<Character, Integer> countPixel(char[][] imageLayer) {
    var counters = new HashMap<Character, Integer>();

    for (char[] line : imageLayer) {
      for (char pixel : line) {
        AdventUtil.incrementMap(counters, pixel);
      }
    }

    return counters;
  }
}
