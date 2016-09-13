package dao;

import model.Ingredient;

import java.util.List;

public class IngredientDao {

    public void addIngredient(String name, Double pricePerUnit) {

    }

    public void removeIngredient(Integer ingredientId) {

    }

    public void setIngredientPrice(Integer ingredientId, Double pricePerUnit) {

    }

    public void setIngredientOutOfStock(Integer ingredientId) {

    }

    public void setIngredientInStock(Integer ingredientId) {

    }

    public List<Ingredient> getAllIngredients() {
        return null;
    }

    public List<Ingredient> getOutOfStockIngredients() {
        return null;
    }

    public List<Ingredient> getInStockIngredients() {
        return null;
    }

    public Ingredient getById(Integer ingedientId) {
        return null;
    }

    public List<Ingredient> getByIds(List<Integer> ingedientIds) {
        return null;
    }

}
