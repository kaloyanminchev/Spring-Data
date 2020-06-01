package demos.springdata.advanced.services;

import demos.springdata.advanced.entities.Ingredient;

import java.util.List;

public interface IngredientService {
    List<Ingredient> findAllByNameStartsWith(String startsWith);

    Ingredient getIngredientByName(String name);

    int updateIngredientsPrice();
}
