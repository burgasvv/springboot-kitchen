package com.burgas.springbootkitchen.service;

import com.burgas.springbootkitchen.entity.Recipe;
import com.burgas.springbootkitchen.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public Recipe findById(Long id) {
        return recipeRepository.findById(id).orElseThrow();
    }

    public List<Recipe> search(String search) {
        return recipeRepository.search(search);
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe update(Long id, Recipe recipe) {
        return recipeRepository.findById(id).map(r -> {
            r.setName(recipe.getName());
            r.setProducts(recipe.getProducts());
            return recipeRepository.save(r);
        }).orElseGet(() -> {
            recipe.setId(id);
            return recipeRepository.save(recipe);
        });
    }

    @Transactional
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }
}
