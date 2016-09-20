package config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import dao.rowmapper.CategoryRowMapper;
import dao.rowmapper.IngredientRowMapper;
import dao.rowmapper.RecipeRowMapper;
import dao.rowmapper.RowMapper;
import model.Category;
import model.Ingredient;
import model.Recipe;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class BarServerModule extends AbstractModule {

    @Override
    protected void configure() {
        SQLCallInterceptor sqlCallInterceptor = new SQLCallInterceptor();
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(SQLCall.class), sqlCallInterceptor);

        bind(new TypeLiteral<RowMapper<Category>>(){}).to(CategoryRowMapper.class);
        bind(new TypeLiteral<RowMapper<Ingredient>>(){}).to(IngredientRowMapper.class);
        bind(new TypeLiteral<RowMapper<Recipe>>(){}).to(RecipeRowMapper.class);
    }

    @Provides
    @Singleton
    DataSource provideDataSource() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, String> credentials = new HashMap<>();
        try (Scanner scanner = new Scanner(new File("dblogin"))) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (!line.trim().startsWith("#")) {
                    String[] split = line.split(":");
                    credentials.put(split[0], split[1]);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File 'dblogin' is missing. Can't log in.");
        }

        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUser(credentials.get("user"));
        dataSource.setPassword(credentials.get("password"));
        dataSource.setURL("jdbc:mysql://localhost/beamerbar");
        return dataSource;
    }

}
