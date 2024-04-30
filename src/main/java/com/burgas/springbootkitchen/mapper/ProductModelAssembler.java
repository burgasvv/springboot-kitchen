package com.burgas.springbootkitchen.mapper;

import com.burgas.springbootkitchen.controller.ProductController;
import com.burgas.springbootkitchen.entity.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {

    @Override
    public @NotNull EntityModel<Product> toModel(@NotNull Product entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(ProductController.class).find(entity.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).productRecipes(entity.getId())).withRel("product-recipes")
        );
    }
}
