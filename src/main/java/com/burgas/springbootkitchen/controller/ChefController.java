package com.burgas.springbootkitchen.controller;

import com.burgas.springbootkitchen.entity.Chef;
import com.burgas.springbootkitchen.entity.Recipe;
import com.burgas.springbootkitchen.mapper.ChefModelAssembler;
import com.burgas.springbootkitchen.mapper.RecipeModelAssembler;
import com.burgas.springbootkitchen.service.ChefService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chefs")
public class ChefController {

    private final ChefService chefService;
    private final ChefModelAssembler chefModelAssembler;
    private final RecipeModelAssembler recipeModelAssembler;

    public ChefController(ChefService chefService, ChefModelAssembler chefModelAssembler,
                          RecipeModelAssembler recipeModelAssembler) {
        this.chefService = chefService;
        this.chefModelAssembler = chefModelAssembler;
        this.recipeModelAssembler = recipeModelAssembler;
    }

    @GetMapping("/{id}")
    public EntityModel<Chef> find(@PathVariable Long id) {
        return chefModelAssembler.toModel(chefService.findById(id));
    }

    @GetMapping
    public CollectionModel<EntityModel<Chef>> all() {
        return CollectionModel.of(
                chefService.findAll().stream().map(chefModelAssembler::toModel).toList()
        );
    }

    @GetMapping("/{id}/chef-recipes")
    public CollectionModel<EntityModel<Recipe>> chefRecipes(@PathVariable Long id) {
        return CollectionModel.of(
                chefService.findById(id).getRecipes().stream().map(recipeModelAssembler::toModel).toList()
        );
    }

    @GetMapping("/{id}/chef-popular-recipes")
    public CollectionModel<EntityModel<Recipe>> chefPopularRecipes(@PathVariable Long id) {
        List<Recipe> recipes = chefService.findById(id).getRecipes();
        List<Recipe>popularRecipes = new ArrayList<>();
        recipes.stream().filter(recipe -> recipe.getRating() >= 9.0).forEach(popularRecipes::add);
        return CollectionModel.of(
                popularRecipes.stream().map(recipeModelAssembler::toModel).toList()
        );
    }

    @GetMapping("/search")
    public CollectionModel<EntityModel<Chef>> search(@RequestParam("search") String search) {
        return CollectionModel.of(
                chefService.search(search).stream().distinct().map(chefModelAssembler::toModel).toList()
        );
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Chef chef) {
        EntityModel<Chef> model = chefModelAssembler.toModel(chefService.save(chef));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Chef chef) {
        Chef chefToFind = chefService.update(id, chef);
        EntityModel<Chef> model = chefModelAssembler.toModel(chefToFind);
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        chefService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
