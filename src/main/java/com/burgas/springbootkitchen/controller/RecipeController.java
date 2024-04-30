package com.burgas.springbootkitchen.controller;

import com.burgas.springbootkitchen.entity.Product;
import com.burgas.springbootkitchen.entity.Recipe;
import com.burgas.springbootkitchen.mapper.ProductModelAssembler;
import com.burgas.springbootkitchen.mapper.RecipeModelAssembler;
import com.burgas.springbootkitchen.service.RecipeService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeModelAssembler recipeModelAssembler;
    private final ProductModelAssembler productModelAssembler;

    public RecipeController(RecipeService recipeService, RecipeModelAssembler recipeModelAssembler,
                            ProductModelAssembler productModelAssembler) {
        this.recipeService = recipeService;
        this.recipeModelAssembler = recipeModelAssembler;
        this.productModelAssembler = productModelAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Recipe>> all() {
        return CollectionModel.of(
                recipeService.findAll().stream().map(recipeModelAssembler::toModel).toList()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<Recipe> find(@PathVariable Long id) {
        return recipeModelAssembler.toModel(recipeService.findById(id));
    }

    @GetMapping("/random-recipe")
    public EntityModel<Recipe> randomRecipe() {
        List<Recipe> recipes = recipeService.findAll();
        Recipe recipe = recipes.get(new Random().nextInt(0, recipes.size()));
        return find(recipe.getId());
    }

    @GetMapping("/{id}/recipe-products")
    public CollectionModel<EntityModel<Product>>recipeProducts(@PathVariable Long id){
        return CollectionModel.of(
                recipeService.findById(id).getProducts().stream().map(productModelAssembler::toModel).toList()
        );
    }

    @GetMapping("/search")
    public CollectionModel<EntityModel<Recipe>> search(@RequestParam("search") String search) {
        return CollectionModel.of(
                recipeService.search(search).stream().distinct().map(recipeModelAssembler::toModel).toList()
        );
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Recipe recipe) {
        EntityModel<Recipe> entityModel = recipeModelAssembler.toModel(recipeService.save(recipe));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Recipe recipe) {
        Recipe recipeToFind = recipeService.update(id, recipe);
        EntityModel<Recipe> entityModel = recipeModelAssembler.toModel(recipeToFind);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        recipeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
