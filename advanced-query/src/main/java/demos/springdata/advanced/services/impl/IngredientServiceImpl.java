package demos.springdata.advanced.services.impl;

import demos.springdata.advanced.entities.Ingredient;
import demos.springdata.advanced.repositories.IngredientRepository;
import demos.springdata.advanced.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<Ingredient> findAllByNameStartsWith(String startsWith) {
        return this.ingredientRepository.findAllByNameStartsWith(startsWith);
    }

    @Override
    public Ingredient getIngredientByName(String name) {
        return this.ingredientRepository.findByName(name);
    }

    @Override
    public int updateIngredientsPrice() {
        return this.ingredientRepository.updateIngredientsByPrice();
    }
}
