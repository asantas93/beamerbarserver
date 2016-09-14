package dao;

import dao.rowmapper.RowMapper;
import model.Category;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static dao.SQLUtils.joinLines;

public class CategoryDao {

    private final Connection connection;
    private final RowMapper<Category> categoryRowMapper;

    public CategoryDao(Connection connection, RowMapper<Category> categoryRowMapper) {
        this.connection = connection;
        this.categoryRowMapper = categoryRowMapper;
    }

    public void addCategory(String name) throws SQLException {
        String sql = joinLines(
                "INSERT INTO Category (id, name)",
                "     VALUES (UUID_SHORT(), '" + name + "')"
        );
        connection.createStatement().execute(sql);
    }

    public void removeCategory(Long categoryId) throws SQLException {
        String sql = joinLines(
                "DELETE ",
                "  FROM Category",
                " WHERE id = " + categoryId + ";",
                "DELETE ",
                "  FROM RecipeCategory",
                " WHERE categoryId = " + categoryId
        );
        connection.createStatement().execute(sql);
    }

    public Category getById(Long categoryId) throws SQLException {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category",
                " WHERE id = " + categoryId
        );
        List<Category> categories = categoryRowMapper.mapAll(connection.createStatement().executeQuery(sql));
        if (categories.size() > 1) {
            throw new RuntimeException("Found more than one result for id");
        }
        return categories.get(0);
    }

    public List<Category> getByIds(List<Long> categoryIds) throws SQLException {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category",
                " WHERE id = " + categoryIds.get(0)
        );
        for (int i = 1; i < categoryIds.size(); i++) {
            sql += "\n    OR id = " + categoryIds.get(i);
        }
        return categoryRowMapper.mapAll(connection.createStatement().executeQuery(sql));
    }

    public List<Category> getByRecipe(Long recipeId) throws SQLException {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category c",
                "  JOIN RecipeCategory rc",
                "    ON rc.categoryId = c.id",
                " WHERE rc.recipeId = " + recipeId
        );
        return categoryRowMapper.mapAll(connection.createStatement().executeQuery(sql));
    }

    public List<Category> getAllCategories() throws SQLException {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category"
        );
        return categoryRowMapper.mapAll(connection.createStatement().executeQuery(sql));
    }

}
