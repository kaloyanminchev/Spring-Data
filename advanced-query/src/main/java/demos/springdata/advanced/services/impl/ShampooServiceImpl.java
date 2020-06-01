package demos.springdata.advanced.services.impl;

import demos.springdata.advanced.entities.Ingredient;
import demos.springdata.advanced.entities.Label;
import demos.springdata.advanced.entities.Shampoo;
import demos.springdata.advanced.entities.Size;
import demos.springdata.advanced.repositories.ShampooRepository;
import demos.springdata.advanced.services.ShampooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShampooServiceImpl implements ShampooService {
    private final ShampooRepository shampooRepository;

    @Autowired
    public ShampooServiceImpl(ShampooRepository shampooRepository) {
        this.shampooRepository = shampooRepository;
    }

    @Override
    public List<Shampoo> getAllShampoosBySize(Size size) {
        return this.shampooRepository.findAllBySize(size);
    }

    @Override
    public List<Shampoo> getAllShampoosBySizeOrLabel(Size size, Label label) {
        return this.shampooRepository.findAllBySizeOrLabelOrderByPriceAsc(size, label);
    }

    @Override
    public List<Shampoo> getShampoosWithPriceHigherThan(BigDecimal price) {
        return this.shampooRepository.findAllByPriceGreaterThanOrderByPriceDesc(price);
    }

    @Override
    public List<Shampoo> findShampoosWithIngredients(Iterable<Ingredient> ingredients) {
        return this.shampooRepository.findWithIngredientsInList(ingredients);
    }

    @Override
    public List<Shampoo> getShampoosByCountOfIngredientsLessThan(int count) {
        return this.shampooRepository.findByCountOfIngredientsLessThan(count);
    }
}
