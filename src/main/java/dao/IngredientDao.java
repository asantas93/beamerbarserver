package dao;

import config.SQLCall;
import dao.rowmapper.RowMapper;
import model.Ingredient;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dao.SQLUtils.joinLines;

public class IngredientDao {

    private final DataSource dataSource;
    private final RowMapper<Ingredient> ingredientRowMapper;

    @Inject
    public IngredientDao(DataSource dataSource, RowMapper<Ingredient> ingredientRowMapper) {
        this.dataSource = dataSource;
        this.ingredientRowMapper = ingredientRowMapper;
    }

    @SQLCall
    public void addIngredient(String name, Double pricePerUnit) throws SQLException {
        String sql = joinLines(
                "INSERT INTO Ingredient (id, name, inStock, price)",
                "     VALUES (UUID_SHORT(), '" + name + "', TRUE, " + pricePerUnit + ")"
        );
        dataSource.getConnection().createStatement().executeUpdate(sql);
    }

    @SQLCall
    public void removeIngredient(Long ingredientId) throws SQLException {
        String sql = joinLines(
                "DELETE rc ",
                "  FROM RecipeCategory rc",
                "  JOIN Recipe r",
                "    ON r.id = rc.recipeId",
                "  JOIN IngredientQuantity iq",
                "    ON r.id = iq.recipeId",
                " WHERE iq.ingredientId = " + ingredientId + ";",
                "DELETE r ",
                "  FROM Recipe r",
                "  JOIN IngredientQuantity iq",
                "    ON r.id = iq.recipeId",
                " WHERE iq.ingredientId = " + ingredientId + ";",
                "DELETE ",
                "  FROM Ingredient",
                " WHERE id = " + ingredientId + ";",
                "DELETE ",
                "  FROM IngredientQuantity",
                " WHERE ingredientId = " + ingredientId
        );
        dataSource.getConnection().createStatement().executeUpdate(sql);
    }

    @SQLCall
    public void setIngredientPrice(Long ingredientId, Double pricePerUnit) throws SQLException {
        String sql = joinLines(
                "UPDATE Ingredient",
                "    SET price = " + pricePerUnit,
                "  WHERE id = " + ingredientId
        );
        dataSource.getConnection().createStatement().executeUpdate(sql);
    }

    @SQLCall
    public void setIngredientInStock(Long ingredientId, Boolean inStock) throws SQLException {
        String sql = joinLines(
                "UPDATE Ingredient",
                "    SET inStock = " + inStock,
                "  WHERE id = " + ingredientId
        );
        dataSource.getConnection().createStatement().executeUpdate(sql);
    }

    @SQLCall
    public List<Ingredient> getAllIngredients() throws SQLException {
        String sql = joinLines(
                "SELECT *",
                "  FROM Ingredient"
        );
        return ingredientRowMapper.mapAll( dataSource.getConnection().createStatement().executeQuery(sql));
    }

    @SQLCall
    public List<Ingredient> getIngredientsWithStock(Boolean inStock) throws SQLException {
        String sql = joinLines(
                "SELECT *",
                "  FROM Ingredient",
                " WHERE inStock = " + inStock
        );
        return ingredientRowMapper.mapAll( dataSource.getConnection().createStatement().executeQuery(sql));
    }

    @SQLCall
    public Ingredient getById(Long ingredientId) throws SQLException {
        String sql = joinLines(
                "SELECT *",
                "  FROM Ingredient",
                " WHERE id = " + ingredientId
        );
        List<Ingredient> ingredients = ingredientRowMapper.mapAll( dataSource.getConnection().createStatement().executeQuery(sql));
        if (ingredients.size() > 1) {
            throw new RuntimeException("Found more than one result for id");
        }
        return ingredients.get(0);
    }

    @SQLCall
    public List<Ingredient> getByIds(List<Long> ingredientIds) throws SQLException {
        String sql = joinLines(
                "SELECT *",
                "  FROM Ingredient",
                " WHERE id = " + ingredientIds.get(0)
        );
        for (int i = 1; i < ingredientIds.size(); i++) {
            sql += "\n    OR id = " + ingredientIds.get(i);
        }
        return ingredientRowMapper.mapAll( dataSource.getConnection().createStatement().executeQuery(sql));
    }

    @SQLCall
    public Map<Ingredient, Double> getQuantitiesForRecipe(Long recipeId) throws SQLException {
        String sql = joinLines(
                "SELECT *",
                "  FROM Ingredient i",
                "  JOIN IngredientQuantity iq",
                "    ON i.id = iq.ingredientId",
                " WHERE iq.recipeId = " + recipeId
        );
        ResultSet rs =  dataSource.getConnection().createStatement().executeQuery(sql);
        Map<Ingredient, Double> quantities = new HashMap<>();
        while (rs.next()) {
            quantities.put(
                    ingredientRowMapper.mapRow(rs),
                    rs.getDouble("quantity")
            );
        }
        return quantities;
    }

}
