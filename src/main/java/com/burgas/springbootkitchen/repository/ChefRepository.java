package com.burgas.springbootkitchen.repository;

import com.burgas.springbootkitchen.entity.Chef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChefRepository extends JpaRepository<Chef, Long> {

    @Query(
            nativeQuery = true,
            value = """
                    select c.* from chef c
                    join recipe r on c.id = r.chef_id
                    where concat(c.name,' ',r.name,' ') ilike concat('%',?1,'%')"""
    )
    List<Chef> search(String search);
}
