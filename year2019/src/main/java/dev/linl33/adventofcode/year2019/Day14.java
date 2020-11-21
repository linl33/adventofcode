package dev.linl33.adventofcode.year2019;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Day14 extends AdventSolution2019<Integer, Integer> {
  private static final long ORE_TOTAL = 1000000000000L;

  public static void main(String[] args) {
    new Day14().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    var recipeBook = RecipeBook.parse(reader);
    return (int) produceFuel(
        new long[recipeBook.recipeQuantity.length + 1],
        recipeBook.recipeQuantity,
        recipeBook.recipeIngredients,
        1
    );
  }

  @Override
  public Integer part2(BufferedReader reader) {
    var recipeBook = RecipeBook.parse(reader);
    var recipeQuantity = recipeBook.recipeQuantity;
    var recipeIngredients = recipeBook.recipeIngredients;
    var itemCount = recipeQuantity.length + 1;

    var unitCost = produceFuel(new long[itemCount], recipeQuantity, recipeIngredients, 1);

    var excess = new long[itemCount];
    var fuelCount = (int) (ORE_TOTAL / unitCost);
    var oresLeft = ORE_TOTAL - produceFuel(excess, recipeQuantity, recipeIngredients, fuelCount);

    var guess = fuelCount >> 2;

    while (oresLeft > 0 && guess > 1) {
      long guessCost;
      long[] excessCopy;
      do {
        excessCopy = Arrays.copyOf(excess, itemCount);
        guessCost = produceFuel(excessCopy, recipeQuantity, recipeIngredients, guess);
      } while (guessCost > oresLeft && (guess >>= 1) > 1);

      guessCost = produceFuel(excessCopy, recipeQuantity, recipeIngredients, guess);

      excess = excessCopy;
      oresLeft -= guessCost;
      fuelCount += guess;
    }

    while (oresLeft > 0) {
      oresLeft -= produceFuel(excess, recipeQuantity, recipeIngredients, 1);
      fuelCount++;
    }

    return fuelCount - (oresLeft == 0 ? 0 : 1);
  }

  private static long produceFuel(long[] excess, int[] recipeQuantity, int[][] recipeIngr, int fuelQuantity) {
    var requestTally = new long[excess.length];
    requestTally[0] = fuelQuantity;

    boolean finished;
    do {
      finished = true;

      for (int currItem = 0; currItem < requestTally.length - 1; currItem++) {
        if (requestTally[currItem] == 0) {
          continue;
        }

        finished = false;

        var unitQuantity = recipeQuantity[currItem];
        var requestedQuantity = requestTally[currItem];

        if (requestedQuantity == 0) {
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

        excess[currItem] += units * unitQuantity - requestedQuantity;
        requestTally[currItem] = 0;

        var ingredients = recipeIngr[currItem];
        for (int ingrId = 0; ingrId < ingredients.length; ingrId++) {
          int ingrQuantity = ingredients[ingrId];
          if (ingrQuantity < 1) {
            continue;
          }

          var stock = excess[ingrId];
          var totalIngrQuantity = ingrQuantity * units;
          var actualQuantity = Math.max(0, totalIngrQuantity - stock);

          excess[ingrId] = Math.max(0, stock - totalIngrQuantity);
          if (actualQuantity == 0) {
            continue;
          }

          requestTally[ingrId] += actualQuantity;
        }
      }
    } while (!finished);

    return requestTally[requestTally.length - 1];
  }

  private static record Item(@NotNull String name, int quantity) {
    @NotNull
    public static Item parse(String itemStr) {
      var parts = itemStr.split(" ");
      return new Item(parts[1], Integer.parseInt(parts[0]));
    }
  }

  private static record Recipe(@NotNull List<Item> ingredients, @NotNull Item product) {
    @NotNull
    public static Recipe parse(@NotNull String recipeStr) {
      var parts = recipeStr.split(" => ");
      return Arrays
          .stream(parts[0].split(", "))
          .map(Item::parse)
          .collect(Collectors.collectingAndThen(
              Collectors.toUnmodifiableList(),
              list -> new Recipe(list, Item.parse(parts[1]))
          ));
    }
  }

  private static record RecipeBook(int[] recipeQuantity, int[][] recipeIngredients) {
    private static final String FUEL = "FUEL";
    private static final String ORE = "ORE";

    @NotNull
    public static RecipeBook parse(@NotNull BufferedReader reader) {
      var recipes = reader
          .lines()
          .map(Recipe::parse)
          .collect(Collectors.toUnmodifiableList());
      var recipeCount = recipes.size();
      var itemCount = recipeCount + 1;

      var recipeQuantity = new int[recipeCount];
      var recipeIngr = new int[recipeCount][itemCount];

      var recipeId = new HashMap<String, Integer>(itemCount);
      recipeId.put(FUEL, 0);
      recipeId.put(ORE, recipeCount);

      for (Recipe recipe : recipes) {
        var id = recipeId.computeIfAbsent(recipe.product().name(), __ -> recipeId.size() - 1);
        recipeQuantity[id] = recipe.product().quantity();

        for (var ingr : recipe.ingredients()) {
          var ingrId = recipeId.computeIfAbsent(ingr.name(), __ -> recipeId.size() - 1);
          recipeIngr[id][ingrId] = ingr.quantity();
        }
      }

      return new RecipeBook(recipeQuantity, recipeIngr);
    }
  }
}
