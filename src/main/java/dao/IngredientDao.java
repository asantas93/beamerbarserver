package dao;

import dao.rowmapper.RowMapper;
import model.Ingredient;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dao.SQLUtils.joinLines;

public class IngredientDao {

    private final Connection connection;
    private final RowMapper<Ingredient> ingredientRowMapper;

    @Inject
    public IngredientDao(Connection connection, RowMapper<Ingredient> ingredientRowMapper) {
        this.connection = connection;
        this.ingredientRowMapper = ingredientRowMapper;
    }

    public void addIngredient(String name, Integer pricePerUnit) {
        String sql = joinLines(
                "INSERT INTO Ingredient (id, name, inStock, price)",
                "     VALUES (UUID_SHORT(), ?, TRUE, ?)"
        );
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setInt(2, pricePerUnit);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public void removeIngredient(Long ingredientId) {
        String sql = joinLines(
                "DELETE rc ",
                "  FROM RecipeCategory rc",
                "  JOIN Recipe r",
                "    ON r.id = rc.recipeId",
                "  JOIN IngredientQuantity iq",
                "    ON r.id = iq.recipeId",
                " WHERE iq.ingredientId = ?;",
                "DELETE r ",
                "  FROM Recipe r",
                "  JOIN IngredientQuantity iq",
                "    ON r.id = iq.recipeId",
                " WHERE iq.ingredientId = ?;",
                "DELETE ",
                "  FROM Ingredient",
                " WHERE id = ?;",
                "DELETE ",
                "  FROM IngredientQuantity",
                " WHERE ingredientId = ?"
        );
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, ingredientId);
            statement.setLong(2, ingredientId);
            statement.setLong(3, ingredientId);
            statement.setLong(4, ingredientId);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public void setIngredientPrice(Long ingredientId, Double pricePerUnit) {
        String sql = joinLines(
                "UPDATE Ingredient",
                "    SET price = " + pricePerUnit,
                "  WHERE id = " + ingredientId
        );
        try {
            connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public void setIngredientInStock(Long ingredientId, Boolean inStock) {
        String sql = joinLines(
                "UPDATE Ingredient",
                "    SET inStock = " + inStock,
                "  WHERE id = " + ingredientId
        );
        try {
            connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public List<Ingredient> getAllIngredients() {
        String sql = joinLines(
                "SELECT *",
                "  FROM Ingredient"
        );
        try {
            return ingredientRowMapper.mapAll(connection.createStatement().executeQuery(sql));            
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);            
        }
    }

    public List<Ingredient> getIngredientsWithStock(Boolean inStock) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Ingredient",
                " WHERE inStock = " + inStock
        );
        try {
            return ingredientRowMapper.mapAll(connection.createStatement().executeQuery(sql));
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public Ingredient getById(Long ingredientId) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Ingredient",
                " WHERE id = " + ingredientId
        );
        List<Ingredient> ingredients;
        try {
             ingredients = ingredientRowMapper.mapAll(
                    connection.createStatement().executeQuery(sql)
            );
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
        if (ingredients.size() > 1) {
            throw new RuntimeException("Found more than one result for id");
        }
        return ingredients.get(0);
    }

    public List<Ingredient> getByIds(List<Long> ingredientIds) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Ingredient",
                " WHERE id = " + ingredientIds.get(0)
        );
        for (int i = 1; i < ingredientIds.size(); i++) {
            sql += "\n    OR id = " + ingredientIds.get(i);
        }
        try {
            return ingredientRowMapper.mapAll(connection.createStatement().executeQuery(sql));
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public Map<Ingredient, Double> getQuantitiesForRecipe(Long recipeId) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Ingredient i",
                "  JOIN IngredientQuantity iq",
                "    ON i.id = iq.ingredientId",
                " WHERE iq.recipeId = " + recipeId
        );
        ResultSet rs;
        try {
            rs =  connection.createStatement().executeQuery(sql);
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
