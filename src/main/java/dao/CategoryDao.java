package dao;

import dao.rowmapper.RowMapper;
import model.Category;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static dao.SQLUtils.joinLines;

public class CategoryDao {

    private final Connection connection;
    private final RowMapper<Category> categoryRowMapper;

    public CategoryDao(Connection connection, RowMapper<Category> categoryRowMapper) {
        this.connection = connection;
        this.categoryRowMapper = categoryRowMapper;
    }

    public void addCategory(String name) {
        String sql = joinLines(
                "INSERT INTO Category (id, name)",
                "     VALUES (UUID_SHORT(), '" + name + "')"
        );
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public void removeCategory(Long categoryId) {
        String sql = joinLines(
                "DELETE FROM Category",
                "      WHERE id = " + categoryId + ";",
                "DELETE FROM RecipeCategory",
                "      WHERE id = " + categoryId
        );
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public Category getById(Long categoryId) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category",
                " WHERE id = " + categoryId
        );
        List<Category> categories;
        try {
            Statement statement = connection.createStatement();
            categories = categoryRowMapper.mapAll(statement.executeQuery(sql));
            if (categories.size() > 1) {
                throw new RuntimeException("Found more than one result for id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
        return categories.get(1);
    }

    public List<Category> getByIds(List<Long> categoryIds) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category",
                " WHERE id = " + categoryIds.get(0)
        );
        for (int i = 1; i < categoryIds.size(); i++) {
            sql += "\n    OR id = " + categoryIds.get(i);
        }
        List<Category> categories;
        try {
            Statement statement = connection.createStatement();
            categories = categoryRowMapper.mapAll(statement.executeQuery(sql));
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
        return categories;
    }

    public List<Category> getByRecipe(Long recipeId) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category c",
                "  JOIN RecipeCategory rc",
                "    ON rc.categoryid = c.id",
                " WHERE rc.recipeid = " + recipeId
        );
        List<Category> categories;
        try {
            Statement statement = connection.createStatement();
            categories = categoryRowMapper.mapAll(statement.executeQuery(sql));
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
        return categories;
    }

    public List<Category> getAllCategories() {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category"
        );
        List<Category> categories;
        try {
            Statement statement = connection.createStatement();
            categories = categoryRowMapper.mapAll(statement.executeQuery(sql));
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
        return categories;
    }

}
