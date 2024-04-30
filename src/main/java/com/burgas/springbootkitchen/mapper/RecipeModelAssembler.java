package com.burgas.springbootkitchen.mapper;

import com.burgas.springbootkitchen.controller.RecipeController;
import com.burgas.springbootkitchen.entity.Recipe;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RecipeModelAssembler implements RepresentationModelAssembler<Recipe, EntityModel<Recipe>> {

    @Override
    public @NotNull EntityModel<Recipe> toModel(@NotNull Recipe entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(RecipeController.class).find(entity.getId())).withSelfRel(),
                linkTo(methodOn(RecipeController.class).recipeProducts(entity.getId())).withRel("recipe-products")
        );
    }
}
