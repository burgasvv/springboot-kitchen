package com.burgas.springbootkitchen.service;

import com.burgas.springbootkitchen.entity.Chef;
import com.burgas.springbootkitchen.repository.ChefRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChefService {

    private final ChefRepository chefRepository;

    public ChefService(ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }

    public List<Chef> findAll() {
        return chefRepository.findAll();
    }

    public Chef findById(Long id) {
        return chefRepository.findById(id).orElseThrow();
    }

    public List<Chef> search(String search) {
        return chefRepository.search(search);
    }

    public Chef save(Chef chef) {
        return chefRepository.save(chef);
    }

    public Chef update(Long id, Chef chef) {
        return chefRepository.findById(id).map(ch -> {
            ch.setName(chef.getName());
            ch.setBiography(chef.getBiography());
            ch.setRecipes(chef.getRecipes());
            return chefRepository.save(ch);
        }).orElseGet(() -> {
            chef.setId(id);
            return chefRepository.save(chef);
        });
    }

    @Transactional
    public void deleteById(Long id) {
        chefRepository.deleteById(id);
    }
}
