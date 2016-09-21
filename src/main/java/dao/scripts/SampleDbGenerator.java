package dao.scripts;

import com.google.inject.Guice;
import com.google.inject.Injector;
import config.BarServerModule;
import dao.CategoryDao;
import dao.IngredientDao;
import dao.RecipeDao;
import model.Category;
import model.Ingredient;
import model.Recipe;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.*;

public class SampleDbGenerator {

    public static void main(String[] ignored) throws FileNotFoundException, SQLException {

        Injector injector = Guice.createInjector(new BarServerModule());
        CategoryDao categoryDao = injector.getInstance(CategoryDao.class);
        IngredientDao ingredientDao = injector.getInstance(IngredientDao.class);
        RecipeDao recipeDao = injector.getInstance(RecipeDao.class);

        Scanner scanner = new Scanner(new File("src/main/resources/ingredients.csv"));

        while (scanner.hasNext()) {
            String[] ingredientData = scanner.nextLine().split(",");
            ingredientDao.addIngredient(ingredientData[0], Integer.parseInt(ingredientData[1]));
        }

        scanner.close();

        scanner = new Scanner(new File("src/main/resources/categories.csv"));

        while (scanner.hasNext()) {
            categoryDao.addCategory(scanner.nextLine());
        }

        for (Ingredient ingredient : ingredientDao.getAllIngredients()) {
            System.out.println(
                    ingredient.getName() + ", " +
                            ingredient.getId() + ", " +
                            ingredient.getCostForQuantity(1) + ", " +
                            ingredient.inStock()
            );
        }

        for (Category category : categoryDao.getAllCategories()) {
            System.out.println(category.getName() + ", " + category.getId());
        }

        List<Ingredient> ingredients = ingredientDao.getAllIngredients();
        List<Category> categories = categoryDao.getAllCategories();

        Set<Long> tempCategories = new HashSet<>();
        Map<Long, Double> tempProportions = new HashMap<>();
        Random rand = new Random();

        for (int i = 0; i < 20; i++) {
            tempCategories.clear();
            int categoryBound = rand.nextInt(categories.size());
            for (int j = 0; j < categoryBound; j++) {
                tempCategories.add(categories.get(new Random().nextInt(categories.size())).getId());
            }
            tempProportions.clear();
            int proportionsBound = rand.nextInt(7);
            for (int j = 0; j < proportionsBound; j++) {
                tempProportions.put(ingredients.get(rand.nextInt(ingredients.size())).getId(), (double) rand.nextInt(60) / 10);
            }
            recipeDao.addRecipe("Recipe " + i, tempCategories, tempProportions, "Description " + i);
        }

        for (Recipe recipe : recipeDao.getAllRecipes()) {
            System.out.println(recipe.getName() + ", " + recipe.getId());
        }

    }

}
