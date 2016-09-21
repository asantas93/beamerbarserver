package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Recipe {

    private final Long id;
    private final String name;
    private final Double price;
    private final Map<Ingredient, Double> proportions;
    private final List<Ingredient> outOfStockIngredients;
    private final List<Category> categories;
    private final Boolean allIngredientsInStock;
    private final String directions;

    public Recipe(
            Long id,
            String name,
            List<Category> categories,
            Map<Ingredient,
                    Double> proportions,
            String directions
    ) {
        this.id = id;
        this.name = name;
        this.proportions = proportions;
        this.categories = categories;
        this.outOfStockIngredients = new ArrayList<>();
        double cumulativePrice = 0;
        for (Map.Entry<Ingredient, Double> proportion : proportions.entrySet()) {
            Ingredient ingredient = proportion.getKey();
            if (!ingredient.inStock()) {
                outOfStockIngredients.add(ingredient);
            }
            cumulativePrice += ingredient.getCostForQuantity(proportion.getValue());
        }
        this.price = cumulativePrice;
        this.allIngredientsInStock = outOfStockIngredients.isEmpty();
        this.directions = directions;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Map<Ingredient, Double> getProportions() {
        return proportions;
    }

    public List<Ingredient> getOutOfStockIngredients() {
        return outOfStockIngredients;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Boolean allIngredientsInStock() {
        return allIngredientsInStock;
    }

    public String getDirections() {
        return directions;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Recipe && ((Recipe) other).id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
