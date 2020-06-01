package demos.springdata.advanced.services;

import demos.springdata.advanced.entities.Ingredient;
import demos.springdata.advanced.entities.Label;
import demos.springdata.advanced.entities.Shampoo;
import demos.springdata.advanced.entities.Size;

import java.math.BigDecimal;
import java.util.List;

public interface ShampooService {
    List<Shampoo> getAllShampoosBySize(Size size);

    List<Shampoo> getAllShampoosBySizeOrLabel(Size size, Label label);

    List<Shampoo> getShampoosWithPriceHigherThan(BigDecimal price);

    List<Shampoo> findShampoosWithIngredients(Iterable<Ingredient> ingredients);

    List<Shampoo> getShampoosByCountOfIngredientsLessThan(int count);
}
