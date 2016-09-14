package rest;

import model.Ingredient;

import java.util.List;

/**
 * TODO: Change method signatures depending on which library we use to make these visible to the client.
 *
 * TODO: Everything else.
 */
public class BarAdminWebService extends BarWebService {

    public void setIngredientInStock(Long ingredientId) {

    }

    public void setIngredientOutOfStock(Long ingredientId) {

    }

    public void setIngredientPrice(Long ingredientId, Double price) {

    }

    public void addIngredient(Double price, String name, Boolean inStock) {

    }

    public void removeIngredient(Long ingredientId) {

    }

    public void removeRecipe(Long recipeId) {

    }

    public void updateIngredientPrice(Long ingredientId, Double price) {

    }

    public void addCategory(String name) {

    }

    public void removeCategory(Long categoryId) {

    }

    public void removeSuggestedIngredient(Long ingedientId) {

    }

    public void supplySuggestedIngredient(Long ingedientId, Double pricePerUnit) {

    }

    public List<Ingredient> getAllSuggestedIngredients() {
        return null;
    }

    public List<Ingredient> getAllOutOfStockIngredients() {
        return null;
    }

}
