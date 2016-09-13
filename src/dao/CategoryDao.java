package dao;

import model.Category;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static dao.SQLUtils.joinLines;

public class CategoryDao {

    private final Connection connection;

    public CategoryDao(Connection connection) {
        this.connection = connection;
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
            e.printStackTrace();
        }
    }

    public void removeCategory(Integer categoryId) {
        String sql = joinLines(
                "DELETE FROM Category",
                "      WHERE id=" + categoryId
        );
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Category getById(Integer categoryId) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category",
                " WHERE id=" + categoryId
        );
        ArrayList<Category> categories = new ArrayList<>(1);
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
            if (categories.size() > 1) {
                throw new RuntimeException("Found more than one result for id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories.get(1);
    }

    public List<Category> getByIds(List<Integer> categoryIds) {
        String sql = joinLines(
                "SELECT *",
                "  FROM Category",
                " WHERE id=" + categoryIds.get(0)
        );
        for (int i = 1; i < categoryIds.size(); i++) {
            sql += "\n    OR id=" + categoryIds.get(i);
        }
        ArrayList<Category> categories = new ArrayList<>(1);
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
            if (categories.size() > 1) {
                throw new RuntimeException("Found more than one result for id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

}
