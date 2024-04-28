package com.burgas.springbootkitchen.config;

import com.burgas.springbootkitchen.entity.Chef;
import com.burgas.springbootkitchen.entity.Product;
import com.burgas.springbootkitchen.entity.Recipe;
import com.burgas.springbootkitchen.repository.ChefRepository;
import com.burgas.springbootkitchen.repository.ProductRepository;
import com.burgas.springbootkitchen.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LoadDatabase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    public CommandLineRunner initDatabase(RecipeRepository recipeRepository,
                                          ProductRepository productRepository,
                                          ChefRepository chefRepository) {
        return _ -> {
            Product apples = new Product();
            apples.setName("Apples");
            Product oranges = new Product();
            oranges.setName("Oranges");
            Product onion = new Product();
            onion.setName("Onion");
            Product tomatoes = new Product();
            tomatoes.setName("Tomatoes");
            Product dough = new Product();
            dough.setName("Dough");
            Product chocolate = new Product();
            chocolate.setName("Chocolate");
            Product cucumbers = new Product();
            cucumbers.setName("Cucumbers");
            Product carrots = new Product();
            carrots.setName("Carrots");
            Product sugar = new Product();
            sugar.setName("Sugar");
            Product milk = new Product();
            milk.setName("Milk");

            Chef louie = new Chef();
            louie.setName("Louie Button");
            louie.setBiography("Chef's biography");

            Recipe fruitPie = new Recipe();
            fruitPie.setName("Fruit Pie");
            fruitPie.setDescription("Fruit Pie recipe description");
            fruitPie.setRating(9.2);
            fruitPie.setChef(louie);
            fruitPie.setProducts(
                    List.of(apples,oranges, dough, chocolate, milk, sugar)
            );

            Recipe vegetableSalad = new Recipe();
            vegetableSalad.setName("Vegetable Salad");
            vegetableSalad.setDescription("Vegetable salad description");
            vegetableSalad.setRating(8.0);
            vegetableSalad.setChef(louie);
            vegetableSalad.setProducts(
                    List.of(onion,tomatoes,cucumbers,carrots)
            );

            LOGGER.info("Preload: {}", productRepository.save(apples));
            LOGGER.info("Preload: {}", productRepository.save(oranges));
            LOGGER.info("Preload: {}", productRepository.save(onion));
            LOGGER.info("Preload: {}", productRepository.save(tomatoes));
            LOGGER.info("Preload: {}", productRepository.save(dough));
            LOGGER.info("Preload: {}", productRepository.save(cucumbers));
            LOGGER.info("Preload: {}", productRepository.save(chocolate));
            LOGGER.info("Preload: {}", productRepository.save(carrots));
            LOGGER.info("Preload: {}", productRepository.save(sugar));
            LOGGER.info("Preload: {}", productRepository.save(milk));

            LOGGER.info("Preload: {}", chefRepository.save(louie));
            LOGGER.info("Preload: {}", recipeRepository.save(fruitPie));
            LOGGER.info("Preload: {}", recipeRepository.save(vegetableSalad));
        };
    }
}
