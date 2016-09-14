package dao.rowmapper;

import dao.CategoryDao;
import dao.IngredientDao;
import model.Recipe;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeRowMapper extends RowMapper<Recipe> {

    private final CategoryDao categoryDao;
    private final IngredientDao ingredientDao;

    public RecipeRowMapper(CategoryDao categoryDao, IngredientDao ingredientDao) {
        this.categoryDao = categoryDao;
        this.ingredientDao = ingredientDao;
    }

    @Override
    public Recipe mapRow(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        return new Recipe(
                id,
                rs.getString("name"),
                categoryDao.getByRecipe(id),
                ingredientDao.getQuantitiesForRecipe(id)
        );
    }

}
