package dao;

import model.Recipe;

import java.util.List;
import java.util.Map;

public class RecipeDao {

    public void addRecipe(String name, List<Long> categoryIds, Map<Long, Double> proportions) {

    }

    public void removeRecipe(Long recipeId) {

    }

    public List<Recipe> getAllRecipes() {
        return null;
    }

    public List<Recipe> getAllRecipesWithIngredient(Long ingredientId) {
        return null;
    }

    public List<Recipe> getAllRecipesWithCategory(Long categoryId) {
        return null;
    }

}
