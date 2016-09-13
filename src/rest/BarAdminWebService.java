package rest;

import model.Ingredient;

import java.util.List;

/**
 * TODO: Change method signatures depending on which library we use to make these visible to the client.
 *
 * TODO: Everything else.
 */
public class BarAdminWebService extends BarWebService {

    public void setIngredientInStock(Integer ingredientId) {

    }

    public void setIngredientOutOfStock(Integer ingredientId) {

    }

    public void setIngredientPrice(Integer ingredientId, Double price) {

    }

    public void addIngredient(Double price, String name, Boolean inStock) {

    }

    public void removeIngredient(Integer ingredientId) {

    }

    public void removeRecipe(Integer recipeId) {

    }

    public void updateIngredientPrice(Integer ingredientId, Double price) {

    }

    public void addCategory(String name) {

    }

    public void removeCategory(Integer categoryId) {

    }

    public void removeSuggestedIngredient(Integer ingedientId) {

    }

    public void supplySuggestedIngredient(Integer ingedientId, Double pricePerUnit) {

    }

    public List<Ingredient> getAllSuggestedIngredients() {
        return null;
    }

    public List<Ingredient> getAllOutOfStockIngredients() {
        return null;
    }

}
