package config;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import dao.rowmapper.CategoryRowMapper;
import dao.rowmapper.IngredientRowMapper;
import dao.rowmapper.RecipeRowMapper;
import dao.rowmapper.RowMapper;
import model.Category;
import model.Ingredient;
import model.Recipe;

public class BarServerModule extends AbstractModule {

    @Override
    protected void configure() {
        SQLCallInterceptor sqlCallInterceptor = new SQLCallInterceptor();
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(SQLCall.class), sqlCallInterceptor);

        bind(new TypeLiteral<RowMapper<Category>>(){}).to(CategoryRowMapper.class);
        bind(new TypeLiteral<RowMapper<Ingredient>>(){}).to(IngredientRowMapper.class);
        bind(new TypeLiteral<RowMapper<Recipe>>(){}).to(RecipeRowMapper.class);
    }

}
