package dao.rowmapper;

import model.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientRowMapper extends RowMapper<Ingredient> {

    @Override
    public Ingredient mapRow(ResultSet rs) throws SQLException {
        return new Ingredient(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getBoolean("instock"),
                rs.getDouble("price")
        );
    }

}
