package dao.rowmapper;

import model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRowMapper extends RowMapper<Category> {

    @Override
    public Category mapRow(ResultSet rs) throws SQLException {
        return new Category(
                rs.getLong("id"),
                rs.getString("name")
        );
    }

}
