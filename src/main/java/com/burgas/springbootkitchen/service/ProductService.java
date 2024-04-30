package com.burgas.springbootkitchen.service;

import com.burgas.springbootkitchen.entity.Product;
import com.burgas.springbootkitchen.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    public List<Product> search(String search) {
        return productRepository.search(search);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product update(Long id, Product product) {
        return productRepository.findById(id).map(p -> {
            p.setName(product.getName());
            p.setRecipes(product.getRecipes());
            return productRepository.save(p);
        }).orElseGet(() -> {
            product.setId(id);
            return productRepository.save(product);
        });
    }

    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
