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

Create the database and start using it. Mine will be named `beamerbar`. 

```
CREATE DATABASE beamerbar;
USE beamerbar;
```

Create all the tables we need for the queries in the DAOs to work.

```
CREATE TABLE category(id BIGINT, name VARCHAR(20));
CREATE TABLE recipe(id BIGINT, name VARCHAR(20));
CREATE TABLE ingredient(id BIGINT, name VARCHAR(20), price SMALLINT, instock BOOL);
CREATE TABLE recipecategory(recipeid BIGINT, categoryid BIGINT);
CREATE TABLE ingredientquantity(recipeid BIGINT, ingredientid BIGINT, quantity DOUBLE(4,2));
```

### Setting up server to connect to database

Run this command upon cloning.

```
git update-index --assume-unchanged src/main/resources/dblogin
```

Now this file is safe to edit without accidentally putting your login info on GitHub. Fill in the password field with whatever user you want to use the database as (default is root).