package com.burgas.springbootkitchen.controller;

import com.burgas.springbootkitchen.entity.Chef;
import com.burgas.springbootkitchen.entity.Recipe;
import com.burgas.springbootkitchen.repository.ChefRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/chefs")
public class ChefController {

    private final ChefRepository chefRepository;

    public ChefController(ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }

    @GetMapping("/{id}")
    public EntityModel<Chef> find(@PathVariable Long id) {
        return EntityModel.of(
                chefRepository.findById(id).orElseThrow(),
                linkTo(methodOn(ChefController.class).chefRecipes(id)).withRel("chef_recipes"),
                linkTo(methodOn(ChefController.class).chefPopularRecipes(id)).withRel("chef_popular_recipes")
        );
    }

    @GetMapping
    public CollectionModel<EntityModel<Chef>> all() {
        return CollectionModel.of(
                chefRepository.findAll().stream().map(chef ->
                    EntityModel.of(
                            chef,
                            linkTo(methodOn(ChefController.class).find(chef.getId())).withSelfRel(),
                            linkTo(methodOn(ChefController.class).chefRecipes(chef.getId())).withRel("chef_recipes"),
                            linkTo(methodOn(ChefController.class).chefPopularRecipes(chef.getId())).withRel("chef_popular_recipes")
                    )
                ).toList()
        );
    }

    @GetMapping("/{id}/chef-recipes")
    public CollectionModel<EntityModel<Recipe>> chefRecipes(@PathVariable Long id) {
        return CollectionModel.of(
                chefRepository.findById(id).orElseThrow().getRecipes().stream().map(recipe ->
                    EntityModel.of(
                            recipe,
                            linkTo(methodOn(RecipeController.class).find(recipe.getId())).withSelfRel(),
                            linkTo(methodOn(RecipeController.class).recipeProducts(recipe.getId())).withRel("recipe-products")
                    )
                ).toList()
        );
    }

    @GetMapping("/{id}/chef-popular-recipes")
    public CollectionModel<EntityModel<Recipe>> chefPopularRecipes(@PathVariable Long id) {
        List<Recipe> recipes = chefRepository.findById(id).orElseThrow().getRecipes();
        List<Recipe>popularRecipes = new ArrayList<>();
        recipes.stream().filter(recipe -> recipe.getRating() >= 9.0).forEach(popularRecipes::add);
        return CollectionModel.of(
                popularRecipes.stream().map(recipe ->
                        EntityModel.of(
                                recipe,
                                linkTo(methodOn(RecipeController.class).find(recipe.getId())).withSelfRel(),
                                linkTo(methodOn(RecipeController.class).recipeProducts(recipe.getId())).withRel("recipe-products")
                        )
                ).toList()
        );
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Chef chef) {
        EntityModel<Chef> model = EntityModel.of(
                chefRepository.save(chef),
                linkTo(methodOn(ChefController.class).chefRecipes(chef.getId())).withRel("chef_recipes"),
                linkTo(methodOn(ChefController.class).chefPopularRecipes(chef.getId())).withRel("chef_popular_recipes")
        );
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Chef chef) {
        Chef chefToFind = chefRepository.findById(id).map(ch -> {
            ch.setName(chef.getName());
            ch.setBiography(chef.getBiography());
            ch.setRecipes(chef.getRecipes());
            return chefRepository.save(ch);
        }).orElseGet(() -> {
            chef.setId(id);
            return chefRepository.save(chef);
        });
        EntityModel<Chef> model = EntityModel.of(
                chefRepository.save(chefToFind),
                linkTo(methodOn(ChefController.class).chefRecipes(chefToFind.getId())).withRel("chef_recipes"),
                linkTo(methodOn(ChefController.class).chefPopularRecipes(chefToFind.getId())).withRel("chef_popular_recipes")
        );
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        chefRepository.delete(chefRepository.findById(id).orElseThrow());
        return ResponseEntity.noContent().build();
    }
}
