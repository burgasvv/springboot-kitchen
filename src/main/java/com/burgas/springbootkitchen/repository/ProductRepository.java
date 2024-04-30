package com.burgas.springbootkitchen.repository;

import com.burgas.springbootkitchen.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    select p.* from product p
                    join recipe_products rp on p.id = rp.product_id
                    join recipe r on r.id = rp.recipe_id
                    where concat(p.name,' ',r.name,' ') ilike concat('%',?1,'%')"""
    )
    List<Product> search(String search);
}
