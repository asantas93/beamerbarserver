package rest;

import model.Category;
import model.Ingredient;
import model.Recipe;

import java.util.List;

/**
 * TODO: Change method signatures depending on which library we use to make these visible to the client.
 *
 * TODO: Everything else.
 */
public class BarWebService {

    public List<Recipe> getRecipesWithIngredient(Integer ingredientId) {
        return null;
    }

    public List<Recipe> getRecipesWithCategory(Integer categoryId) {
        return null;
    }

    public List<Category> getAllCategories() {
        return null;
    }

    public List<Ingredient> getAllIngredients() {
        return null;
    }

    public List<Recipe> getAllRecipes() {
        return null;
    }

    public void reportIngredientOutOfStock(Integer ingredientId) {

    }

    public void addRecipe(String name, List<Integer> ingredientIds, List<Integer> categoryIds) {

    }

    public void suggestNewIngredient(String name) {

    }

}
