package dao;

import dao.rowmapper.RowMapper;
import model.Category;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static dao.SQLUtils.joinLines;

public class CategoryDao {

    private final DataSource dataSource;
    private final RowMapper<Category> categoryRowMapper;

    @Inject
    public CategoryDao(DataSource dataSource, RowMapper<Category> categoryRowMapper) {
        this.dataSource = dataSource;
        this.categoryRowMapper = categoryRowMapper;
    }

    public void addCategory(String name) {
        String sql = joinLines(
                "INSERT INTO Category (id, name)",
                "     VALUES (UUID_SHORT(), ?)"
        );
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(sql);
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public void removeCategory(Long categoryId) {
        String sql = joinLines(
                "DELETE ",
                "  FROM Category",
                " WHERE id = ?"
        );
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, categoryId);
            statement.executeUpdate();
            sql = joinLines(
                    "DELETE ",
                    "  FROM RecipeCategory",
                    " WHERE categoryId = ?"
            );
            statement = connection.prepareStatement(sql);
            statement.setLong(1, categoryId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public Category getById(Long categoryId) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category",
                " WHERE id = ?"
        );
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(sql);
            statement.setLong(1, categoryId);
            List<Category> categories = categoryRowMapper.mapAll(statement.executeQuery());
            if (categories.size() > 1) {
                throw new RuntimeException("Found more than one result for id");
            }
            return categories.get(0);
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public List<Category> getByIds(List<Long> categoryIds) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category",
                " WHERE id = ?"
        );
        for (int i = 1; i < categoryIds.size(); i++) {
            sql += "\n    OR id = ?";
        }
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(sql);
            for (int i = 0; i < categoryIds.size(); i++) {
                statement.setLong(i + 1, categoryIds.get(i));
            }
            return categoryRowMapper.mapAll(statement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public List<Category> getByRecipe(Long recipeId) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category c",
                "  JOIN RecipeCategory rc",
                "    ON rc.categoryId = c.id",
                " WHERE rc.recipeId = ?"
        );
        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(sql);
            statement.setLong(1, recipeId);
            return categoryRowMapper.mapAll(statement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

    public List<Category> getAllCategories() {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category"
        );
        try {
            return categoryRowMapper.mapAll(dataSource.getConnection().createStatement().executeQuery(sql));
        } catch (SQLException e) {
            throw new RuntimeException(sql, e);
        }
    }

}
