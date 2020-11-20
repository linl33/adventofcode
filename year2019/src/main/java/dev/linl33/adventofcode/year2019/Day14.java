package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.solution.SolutionPart;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day14 extends AdventSolution2019<Integer, Integer> {
  public static void main(String[] args) {
//    new Day14().runAndPrintAll();

    new Day14().print(SolutionPart.PART_1, "day14test5");
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    var recipes = reader
        .lines()
        .map(Recipe::parse)
        .collect(Collectors.toUnmodifiableMap(
            Recipe::product,
            Recipe::ingredients
        ));

    var quantityMap = recipes
        .keySet()
        .stream()
        .collect(Collectors.toUnmodifiableMap(
            Item::name,
            Item::quantity
        ));

    var ingrMap = recipes
        .entrySet()
        .stream()
        .collect(Collectors.toUnmodifiableMap(
            kv -> kv.getKey().name(),
            Map.Entry::getValue
        ));

    var ingrDeque = new ArrayDeque<String>();
    ingrDeque.add("FUEL");

    var itemTally = new HashMap<String, Integer>();
    itemTally.put("FUEL", 1);

    var excessItem = new HashMap<String, Integer>();

    while (!ingrDeque.isEmpty()) {
      var currIngr = ingrDeque.pop();
      var unitQuantity = quantityMap.get(currIngr);
      var requestedQuantity = itemTally.get(currIngr);

      if (requestedQuantity == null) {
        continue;
      }

      var units = requestedQuantity / unitQuantity;
      var missing = requestedQuantity % unitQuantity;

      if (units == 0) {
        units = 1;
        missing = 0;
      }

      if (missing > 0) {
        units++;
      }

      var excess = units * unitQuantity - requestedQuantity;
      excessItem.compute(currIngr, (item, currExcess) -> currExcess == null ? excess : currExcess + excess);

      itemTally.remove(currIngr);

      var ingredients = ingrMap.get(currIngr);
      for (var ingr : ingredients) {
        var stock = excessItem.getOrDefault(ingr.name(), 0);
        var totalIngrQuantity = ingr.quantity() * units;
        int actualQuantity = Math.max(0, totalIngrQuantity - stock);

        excessItem.put(ingr.name(), Math.max(0, stock - totalIngrQuantity));
        if (actualQuantity == 0) {
          continue;
        }

        itemTally.compute(ingr.name(), (item, currQuantity) -> currQuantity == null ? actualQuantity : currQuantity + actualQuantity);

        if (!ingr.name().equals("ORE")) {
          ingrDeque.push(ingr.name());
        }
      }
    }

    return itemTally.get("ORE");
  }

  @Override
  public Integer part2(BufferedReader reader) {
    var recipes = reader
        .lines()
        .map(Recipe::parse)
        .collect(Collectors.toUnmodifiableMap(
            Recipe::product,
            Recipe::ingredients
        ));

    var recipeMap = recipes
        .keySet()
        .stream()
        .collect(Collectors.toUnmodifiableMap(
            Item::name,
            Function.identity()
        ));

    var cache = new HashSet<Integer>();
    var cache2 = new HashSet<String>();
    var cache3 = new HashMap<String, Integer>();

    var excessItem = new HashMap<String, Integer>();
    recipes.keySet().forEach(i -> excessItem.put(i.name(), 0));

    var oresLeft = 1000000000000L;
    var fuelCount = 0;

    while (oresLeft > 0) {
      var leftOver = excessItem.toString();

      var cachedValue = cache3.get(leftOver);
      if (cachedValue != null) {
        oresLeft -= cachedValue;
        fuelCount++;
        continue;
      }

      var ingrDeque = new ArrayDeque<Item>();
      ingrDeque.add(new Item("FUEL", 1));

      var itemTally = new HashMap<String, Integer>();
      itemTally.put("FUEL", 1);

      var itemProduced = new HashMap<String, Integer>();

      while (!ingrDeque.isEmpty()) {
        var nextProduct = ingrDeque.pop();
        var requestedQuantity = itemTally.get(nextProduct.name());

        if (requestedQuantity == null) {
          continue;
        }

        var crafts = requestedQuantity / nextProduct.quantity();
        var missing = requestedQuantity % nextProduct.quantity();

        if (crafts == 0) {
          crafts = 1;
          missing = 0;
        }

        if (missing > 0) {
          crafts++;
        }

        var excess = crafts * nextProduct.quantity() - requestedQuantity;
        excessItem.compute(nextProduct.name(), (item, currExcess) -> currExcess == null ? excess : currExcess + excess);

        itemTally.remove(nextProduct.name());

        int finalCrafts = crafts;
        itemProduced.compute(nextProduct.name(), (s, integer) -> (integer == null ? 0 : integer) + (finalCrafts * nextProduct.quantity()));

        var ingredients = recipes.get(nextProduct);
        for (var ingr : ingredients) {
          var stock = excessItem.getOrDefault(ingr.name(), 0);
          var totalIngrQuantity = ingr.quantity() * crafts;
          int actualQuantity = Math.max(0, totalIngrQuantity - stock);

          excessItem.put(ingr.name(), Math.max(0, stock - totalIngrQuantity));
          if (actualQuantity == 0) {
            continue;
          }

          itemTally.compute(ingr.name(), (item, currQuantity) -> currQuantity == null ? actualQuantity : currQuantity + actualQuantity);

          if (!ingr.name().equals("ORE")) {
            ingrDeque.push(recipeMap.get(ingr.name()));
          }
        }
      }

      var cost = itemTally.get("ORE");
      oresLeft -= cost;
      fuelCount++;

      cache3.put(leftOver, cost);

//      System.out.println(excessItem);
//      System.out.println(itemProduced);

//      if (!cache.add(cost)) {
//        cache2.add(cost + " -> " + leftOver);
//      }
    }

//    System.out.println("!!");
//
//    var byLeftOver = cache2
//        .stream()
//        .collect(Collectors.groupingBy(s -> s.split(" -> ")[1], Collectors.toSet()));
//    byLeftOver
//        .values()
//        .stream()
//        .filter(s -> s.size() > 1)
//        .findAny()
//        .ifPresent(__ -> System.out.println("FOUND"));
//
//    cache2
//        .stream()
//        .filter(s -> s.startsWith("2203242") || s.startsWith("2203948"))
//        .sorted()
//        .forEach(System.out::println);

    return fuelCount - (oresLeft == 0 ? 0 : 1);
  }

  private static record Item(@NotNull String name, int quantity) {
    @NotNull
    public static Item parse(String itemStr) {
      var parts = itemStr.split(" ");
      return new Item(parts[1], Integer.parseInt(parts[0]));
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Item item = (Item) o;

      return name.equals(item.name);
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }
  }

  private static record Recipe(List<Item> ingredients, Item product) {
    @NotNull
    public static Recipe parse(String recipeStr) {
      var parts = recipeStr.split(" => ");
      var ingredientsList = Arrays
          .stream(parts[0].split(", "))
          .map(Item::parse)
          .collect(Collectors.toUnmodifiableList());

      return new Recipe(ingredientsList, Item.parse(parts[1]));
    }
  }
}
