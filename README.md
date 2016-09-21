# Beamer Bar Server
Keep track of shared ingredients.


### Creating a new database in Ubuntu
*Do this only if you don't want to use an existing database.*

First, install MySQL if you don't have it.

```
sudo apt-get update
sudo apt-get install mysql-server
```

Now enter the MySQL shell with the root user.

```
mysql -u root -p
```

Create the database and start using it. Mine will be named `BeamerBar`. 

```
CREATE DATABASE BeamerBar;
USE BeamerBar;
```

Create all the tables we need for the queries in the DAOs to work.

```
CREATE TABLE Category(id BIGINT, name VARCHAR(40));
CREATE TABLE Recipe(id BIGINT, name VARCHAR(40), directions TEXT);
CREATE TABLE Ingredient(id BIGINT, name VARCHAR(40), price SMALLINT, inStock BOOL);
CREATE TABLE RecipeCategory(recipeId BIGINT, categoryId BIGINT);
CREATE TABLE IngredientQuantity(recipeId BIGINT, ingredientId BIGINT, quantity DOUBLE(4,2));
```

### Setting up server to connect to database

Run this command upon cloning.

```
git update-index --assume-unchanged src/main/resources/dblogin
```

Now this file is safe to edit without accidentally putting your login info on GitHub. Fill in the password field with whatever user you want to use the database as (default is root).