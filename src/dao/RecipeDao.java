package dao;

import model.Recipe;

import java.util.List;
import java.util.Map;

public class RecipeDao {

    public void addRecipe(String name, List<Integer> categoryIds, Map<Integer, Double> proportions) {

    }

    public void removeRecipe(Integer recipeId) {

    }

    public List<Recipe> getAllRecipes() {
        return null;
    }

    public List<Recipe> getAllRecipesWithIngredient(Integer ingredientId) {
        return null;
    }

    public List<Recipe> getAllRecipesWithCategory(Integer categoryId) {
        return null;
    }

}
