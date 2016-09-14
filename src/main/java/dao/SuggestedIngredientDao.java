package dao;

import config.SQLCall;
import model.Ingredient;

import java.sql.SQLException;
import java.util.List;

public class SuggestedIngredientDao {

    @SQLCall
    public void suggestIngredient(String name) throws SQLException {

    }

    @SQLCall
    public void removeSuggestedIngredient(Long ingedientId) throws SQLException {

    }

    @SQLCall
    public void supplySuggestedIngredient(Long ingedientId, Double pricePerUnit) throws SQLException {

    }

    @SQLCall
    public List<Ingredient> getAllSuggestedIngredients() throws SQLException {
        return null;
    }

}
