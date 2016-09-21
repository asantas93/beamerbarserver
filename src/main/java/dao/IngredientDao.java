package dao;

import dao.rowmapper.RowMapper;
import model.Ingredient;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                "  JOIN RecipeProportion rp",
                "    ON r.id = rp.recipeId",
                " WHERE rp.ingredientId = ?"
        );
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, ingredientId);
            statement.executeUpdate();
            sql = joinLines(
                    "DELETE r ",
                    "  FROM Recipe r",
                    "  JOIN RecipeProportion rp",
                    "    ON r.id = rp.recipeId",
                    " WHERE rp.ingredientId = ?"
            );
            statement = connection.prepareStatement(sql);
            statement.setLong(1, ingredientId);
            statement.executeUpdate();
            sql = joinLines(
                    "DELETE ",
                    "  FROM Ingredient",
                    " WHERE id = ?"
            );
            statement = connection.prepareStatement(sql);
            statement.setLong(1, ingredientId);
            statement.executeUpdate();
            sql = joinLines(
                    "DELETE ",
                    "  FROM RecipeProportion",
                    " WHERE ingredientId = ?"
            );
            statement = connection.prepareStatement(sql);
            statement.setLong(1, ingredientId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public void setIngredientPrice(Long ingredientId, Integer pricePerUnit) {
        String sql = joinLines(
                "UPDATE Ingredient",
                "    SET price = ?",
                "  WHERE id = ?"
        );
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, pricePerUnit);
            statement.setLong(2, ingredientId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public void setIngredientInStock(Long ingredientId, Boolean inStock) {
        String sql = joinLines(
                "UPDATE Ingredient",
                "    SET inStock = ?",
                "  WHERE id = ?"
        );
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBoolean(1, inStock);
            statement.setLong(2, ingredientId);
            statement.executeUpdate();
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
                " WHERE inStock = ?"
        );
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBoolean(1, inStock);
            return ingredientRowMapper.mapAll(statement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public Ingredient getById(Long ingredientId) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Ingredient",
                " WHERE id = ?"
        );
        List<Ingredient> ingredients;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, ingredientId);
            ingredients = ingredientRowMapper.mapAll(statement.executeQuery());
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
                " WHERE id = ?"
        );
        for (int i = 1; i < ingredientIds.size(); i++) {
            sql += "\n    OR id = ?";
        }
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < ingredientIds.size(); i++) {
                statement.setLong(i + 1, ingredientIds.get(i));
            }
            return ingredientRowMapper.mapAll(statement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public Map<Ingredient, Double> getQuantitiesForRecipe(Long recipeId) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Ingredient i",
                "  JOIN RecipeProportion rp",
                "    ON i.id = rp.ingredientId",
                " WHERE rp.recipeId = ?"
        );
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, recipeId);
            ResultSet rs =  statement.executeQuery();
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
