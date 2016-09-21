package dao;

import dao.rowmapper.RowMapper;
import model.Recipe;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static dao.SQLUtils.joinLines;

public class RecipeDao {

    private final Connection connection;
    private final RowMapper<Recipe> recipeRowMapper;

    @Inject
    public RecipeDao(Connection connection, RowMapper<Recipe> recipeRowMapper) {
        this.connection = connection;
        this.recipeRowMapper = recipeRowMapper;
    }

    public void addRecipe(String name, Set<Long> categoryIds, Map<Long, Double> proportions, String directions) {
        String sql = joinLines(
                "INSERT INTO Recipe (id, name, directions)",
                "     VALUES (?, ?, ?)"
        );
        try {

            ResultSet rs = connection.createStatement().executeQuery("SELECT UUID_SHORT()");
            rs.next();
            Long recipeId = rs.getLong(1);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, recipeId);
            statement.setString(2, name);
            statement.setString(3, directions);
            statement.executeUpdate();

            for (Long categoryId : categoryIds) {
                sql = joinLines(
                        "INSERT INTO RecipeCategory (recipeId, categoryId)",
                        "     VALUES (?, ?)"
                );
                statement = connection.prepareStatement(sql);
                statement.setLong(1, recipeId);
                statement.setLong(2, categoryId);
                statement.executeUpdate();
            }
            for (Map.Entry<Long, Double> entry : proportions.entrySet()) {
                sql = joinLines(
                        "INSERT INTO IngredientQuantity (ingredientId, recipeId, quantity)",
                        "     VALUES (?, ?, ?)"
                );
                statement = connection.prepareStatement(sql);
                statement.setLong(1, entry.getKey());
                statement.setLong(2, recipeId);
                statement.setDouble(3, entry.getValue());
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public void removeRecipe(Long recipeId) {
        String sql = joinLines(
                "DELETE rc",
                "  FROM RecipeCategory rc",
                "  JOIN Recipe r",
                "    ON rc.recipeId = r.id",
                " WHERE r.id = ?;",
                "DELETE iq",
                "  FROM IngredientQuantity iq",
                "  JOIN Recipe r",
                "    ON r.id = iq.recipeId",
                " WHERE r.id = ?;",
                "DELETE ",
                "  FROM Recipe",
                " WHERE id = ?"
        );
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, recipeId);
            statement.setLong(2, recipeId);
            statement.setLong(3, recipeId);
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public List<Recipe> getAllRecipes() {
        String sql = joinLines(
                "SELECT *",
                "  FROM Recipe"
        );
        try {
            return recipeRowMapper.mapAll(connection.createStatement().executeQuery(sql));
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public List<Recipe> getAllRecipesWithIngredient(Long ingredientId) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Recipe r",
                "  JOIN IngredientQuantity iq",
                "    ON iq.recipeId = r.id",
                " WHERE iq.ingredientId = ?"
        );
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, ingredientId);
            return  recipeRowMapper.mapAll(statement.executeQuery(sql));
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public List<Recipe> getAllRecipesWithCategory(Long categoryId) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Recipe r",
                "  JOIN RecipeCategory rc",
                "    ON rc.recipeId = r.id",
                " WHERE rc.categoryId = ?"
        );
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, categoryId);
            return recipeRowMapper.mapAll(statement.executeQuery(sql));
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

}
