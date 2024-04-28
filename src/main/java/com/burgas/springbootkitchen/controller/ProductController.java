package com.burgas.springbootkitchen.controller;

import com.burgas.springbootkitchen.entity.Product;
import com.burgas.springbootkitchen.entity.Recipe;
import com.burgas.springbootkitchen.repository.ProductRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public CollectionModel<EntityModel<Product>> all() {
        return CollectionModel.of(
                productRepository.findAll().stream().map(product ->
                    EntityModel.of(
                            product,
                            linkTo(methodOn(ProductController.class).find(product.getId())).withSelfRel(),
                            linkTo(methodOn(ProductController.class).productRecipes(product.getId())).withRel("product-recipes")
                    )
                ).toList()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<Product> find(@PathVariable Long id) {
        return EntityModel.of(
                productRepository.findById(id).orElseThrow(),
                linkTo(methodOn(ProductController.class).productRecipes(id)).withRel("product-recipes")
        );
    }

    @GetMapping("/{id}/product-recipes")
    public CollectionModel<EntityModel<Recipe>> productRecipes(@PathVariable Long id) {
        return CollectionModel.of(
                productRepository.findById(id).orElseThrow().getRecipes().stream().map(recipe ->
                    EntityModel.of(
                            recipe,
                            linkTo(methodOn(RecipeController.class).find(recipe.getId())).withSelfRel(),
                            linkTo(methodOn(RecipeController.class).recipeProducts(recipe.getId())).withRel("recipe-products")
                    )
                ).toList()
        );
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Product product) {
        EntityModel<Product> entityModel = EntityModel.of(
                productRepository.save(product),
                linkTo(methodOn(ProductController.class).productRecipes(product.getId())).withRel("product-recipes")
        );
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Product product) {
        Product productToFind = productRepository.findById(id).map(p -> {
            p.setName(product.getName());
            p.setRecipes(product.getRecipes());
            return productRepository.save(p);
        }).orElseGet(() -> {
            product.setId(id);
            return productRepository.save(product);
        });
        EntityModel<Product> entityModel = EntityModel.of(
                productToFind, linkTo(methodOn(ProductController.class).find(product.getId())).withRel("product-recipes")
        );
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productRepository.delete(productRepository.findById(id).orElseThrow());
        return ResponseEntity.noContent().build();
    }
}
