package com.burgas.springbootkitchen.mapper;

import com.burgas.springbootkitchen.controller.ChefController;
import com.burgas.springbootkitchen.entity.Chef;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ChefModelAssembler implements RepresentationModelAssembler<Chef, EntityModel<Chef>> {

    @Override
    public @NotNull EntityModel<Chef> toModel(@NotNull Chef entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(ChefController.class).chefRecipes(entity.getId())).withRel("chef_recipes"),
                linkTo(methodOn(ChefController.class).chefPopularRecipes(entity.getId())).withRel("chef_popular_recipes")
        );
    }
}
