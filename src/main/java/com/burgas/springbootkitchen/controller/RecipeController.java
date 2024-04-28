package com.burgas.springbootkitchen.controller;

import com.burgas.springbootkitchen.entity.Product;
import com.burgas.springbootkitchen.entity.Recipe;
import com.burgas.springbootkitchen.repository.RecipeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping
    public CollectionModel<EntityModel<Recipe>> all() {
        return CollectionModel.of(
                recipeRepository.findAll().stream().map(recipe ->
                    EntityModel.of(
                            recipe,
                            linkTo(methodOn(RecipeController.class).find(recipe.getId())).withSelfRel(),
                            linkTo(methodOn(RecipeController.class).recipeProducts(recipe.getId())).withRel("recipe-products")
                    )
                ).toList()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<Recipe> find(@PathVariable Long id) {
        return EntityModel.of(
                recipeRepository.findById(id).orElseThrow(),
                linkTo(methodOn(RecipeController.class).recipeProducts(id)).withRel("recipe-products")
        );
    }

    @GetMapping("/random-recipe")
    public EntityModel<Recipe> randomRecipe() {
        List<Recipe> recipes = recipeRepository.findAll();
        Recipe recipe = recipes.get(new Random().nextInt(0, recipes.size()));
        return find(recipe.getId());
    }

    @GetMapping("/{id}/recipe-products")
    public CollectionModel<EntityModel<Product>>recipeProducts(@PathVariable Long id){
        return CollectionModel.of(
                recipeRepository.findById(id).orElseThrow().getProducts().stream().map(product ->
                    EntityModel.of(
                            product,
                            linkTo(methodOn(ProductController.class).find(product.getId())).withSelfRel(),
                            linkTo(methodOn(ProductController.class).productRecipes(product.getId())).withRel("product-recipes")
                    )
                ).toList()
        );
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Recipe recipe) {
        EntityModel<Recipe> entityModel = EntityModel.of(
                recipeRepository.save(recipe),
                linkTo(methodOn(RecipeController.class).recipeProducts(recipe.getId())).withRel("recipe-products")
        );
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Recipe recipe) {
        Recipe recipeToFind = recipeRepository.findById(id).map(r -> {
            r.setName(recipe.getName());
            r.setProducts(recipe.getProducts());
            return recipeRepository.save(r);
        }).orElseGet(() -> {
            recipe.setId(id);
            return recipeRepository.save(recipe);
        });
        EntityModel<Recipe> entityModel = EntityModel.of(
                recipeToFind, linkTo(methodOn(RecipeController.class).recipeProducts(recipeToFind.getId())).withRel("recipe-products")
        );
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        recipeRepository.delete(recipeRepository.findById(id).orElseThrow());
        return ResponseEntity.noContent().build();
    }
}
