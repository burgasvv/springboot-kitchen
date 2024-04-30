package com.burgas.springbootkitchen.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Recipe {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Check(constraints = "rating >= 0 and rating <= 10")
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    private Chef chef;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product>products;
}
