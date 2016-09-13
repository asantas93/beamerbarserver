package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Recipe {

    private final Integer id;
    private final String name;
    private final Double price;
    private final Map<Ingredient, Double> proportions;
    private final List<Ingredient> outOfStockIngredients;
    private final List<Category> categories;
    private final Boolean allIngredientsInStock;

    public Recipe(Integer id, String name, List<Category> categories, Map<Ingredient, Double> proportions) {
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
    }

    public Integer getId() {
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

    @Override
    public boolean equals(Object other) {
        return other instanceof Recipe && ((Recipe) other).id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
