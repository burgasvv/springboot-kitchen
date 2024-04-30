package com.burgas.springbootkitchen.controller;

import com.burgas.springbootkitchen.entity.Product;
import com.burgas.springbootkitchen.entity.Recipe;
import com.burgas.springbootkitchen.mapper.ProductModelAssembler;
import com.burgas.springbootkitchen.mapper.RecipeModelAssembler;
import com.burgas.springbootkitchen.service.ProductService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductModelAssembler productModelAssembler;
    private final RecipeModelAssembler recipeModelAssembler;

    public ProductController(ProductService productService, ProductModelAssembler productModelAssembler,
                             RecipeModelAssembler recipeModelAssembler) {
        this.productService = productService;
        this.productModelAssembler = productModelAssembler;
        this.recipeModelAssembler = recipeModelAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Product>> all() {
        return CollectionModel.of(
                productService.findAll().stream().map(productModelAssembler::toModel).toList()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<Product> find(@PathVariable Long id) {
        return productModelAssembler.toModel(productService.findById(id));
    }

    @GetMapping("/{id}/product-recipes")
    public CollectionModel<EntityModel<Recipe>> productRecipes(@PathVariable Long id) {
        return CollectionModel.of(
                productService.findById(id).getRecipes().stream().map(recipeModelAssembler::toModel).toList()
        );
    }

    @GetMapping("/search")
    public CollectionModel<EntityModel<Product>> search(@RequestParam("search") String search) {
        return CollectionModel.of(
                productService.search(search).stream().distinct().map(productModelAssembler::toModel).toList()
        );
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Product product) {
        EntityModel<Product> entityModel = productModelAssembler.toModel(productService.save(product));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Product product) {
        Product productToFind = productService.update(id, product);
        EntityModel<Product> entityModel = productModelAssembler.toModel(productToFind);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
