package dao;

import dao.rowmapper.RowMapper;
import model.Recipe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import static dao.SQLUtils.joinLines;

public class RecipeDao {

    private final Connection connection;
    private final RowMapper<Recipe> recipeRowMapper;

    public RecipeDao(Connection connection, RowMapper<Recipe> recipeRowMapper) {
        this.connection = connection;
        this.recipeRowMapper = recipeRowMapper;
    }

    public void addRecipe(String name, List<Long> categoryIds, Map<Long, Double> proportions) throws SQLException {
        String sql = joinLines(
                "INSERT INTO Recipe (id, name)",
                "     VALUES (UUID_SHORT(), '" + name + "')"
        );
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        Long recipeId = rs.getLong("id");
        sql = "";
        for (Long categoryId : categoryIds) {
            sql += joinLines(
                    "INSERT INTO RecipeCategory (recipeId, categoryId)",
                    "     VALUES (" + recipeId + ", " + categoryId + ");"
            );
        }
        for (Map.Entry<Long, Double> proportion : proportions.entrySet()) {
            sql += joinLines(
                    "INSERT INTO IngredientQuantity (ingredientId, recipeId, quantity)",
                    "     VALUES (" + proportion.getKey() + ", " + recipeId + ", " + proportion.getValue() + ");"
            );
        }
        connection.createStatement().executeUpdate(sql);
    }

    public void removeRecipe(Long recipeId) throws SQLException {
        String sql = joinLines(
                "DELETE rc",
                "  FROM RecipeCategory rc",
                "  JOIN Recipe r",
                "    ON rc.recipeId = r.id",
                " WHERE r.id = " + recipeId + ";",
                "DELETE iq",
                "  FROM IngredientQuantity iq",
                "  JOIN Recipe r",
                "    ON r.id = iq.recipeId",
                " WHERE r.id = " + recipeId + ";",
                "DELETE ",
                "  FROM Recipe",
                " WHERE id = " + recipeId
        );
        connection.createStatement().execute(sql);
    }

    public List<Recipe> getAllRecipes() throws SQLException {
        String sql = joinLines(
                "SELECT *",
                "  FROM Recipe"
        );
        return recipeRowMapper.mapAll(connection.createStatement().executeQuery(sql));
    }

    public List<Recipe> getAllRecipesWithIngredient(Long ingredientId) throws SQLException {
        String sql = joinLines(
                "SELECT *",
                "  FROM Recipe r",
                "  JOIN IngredientQuantity iq",
                "    ON iq.recipeId = r.id",
                " WHERE iq.ingredientId = " + ingredientId
        );
        return  recipeRowMapper.mapAll(connection.createStatement().executeQuery(sql));
    }

    public List<Recipe> getAllRecipesWithCategory(Long categoryId) {
        return null;
    }

}
