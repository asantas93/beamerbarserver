package dao;

import dao.rowmapper.RowMapper;
import model.Ingredient;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dao.SQLUtils.joinLines;

public class IngredientDao {

    private final Connection connection;
    private final RowMapper<Ingredient> ingredientRowMapper;

    public IngredientDao(Connection connection, RowMapper<Ingredient> ingredientRowMapper) {
        this.connection = connection;
        this.ingredientRowMapper = ingredientRowMapper;
    }

    public void addIngredient(String name, Double pricePerUnit) {

    }

    public void removeIngredient(Long ingredientId) {

    }

    public void setIngredientPrice(Long ingredientId, Double pricePerUnit) {

    }

    public void setIngredientOutOfStock(Long ingredientId) {

    }

    public void setIngredientInStock(Long ingredientId) {

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

    public Ingredient getById(Long ingedientId) {
        return null;
    }

    public List<Ingredient> getByIds(List<Long> ingedientIds) {
        return null;
    }

    public Map<Ingredient, Double> getQuantitiesForRecipe(Long recipeId) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Ingredient i",
                "  JOIN IngredientQuantity iq",
                "    ON i.id = iq.ingredientId",
                " WHERE iq.recipeId = " + recipeId
        );
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            Map<Ingredient, Double> quantities = new HashMap<>();
            while (rs.next()) {
                quantities.put(
                        ingredientRowMapper.mapRow(rs),
                        rs.getDouble("quantity")
                );
            }
            return quantities;
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

}
