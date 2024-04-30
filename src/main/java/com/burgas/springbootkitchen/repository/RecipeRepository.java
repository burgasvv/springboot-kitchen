package com.burgas.springbootkitchen.repository;

import com.burgas.springbootkitchen.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query(nativeQuery = true,
            value = """
            select r.* from recipe r
                        join chef c on c.id = r.chef_id
                        join recipe_products rp on r.id = rp.recipe_id
                        join product p on p.id = rp.product_id
                        where concat(r.name,' ',c.name,' ',p.name,' ') ilike concat('%',:search,'%')""")
    List<Recipe> search(@Param("search") String search);
}
