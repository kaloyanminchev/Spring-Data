package demos.springdata.advanced.repositories;

import demos.springdata.advanced.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findAllByNameStartsWith(String startsWith);

    Ingredient findByName(String ingredientName);

    @Modifying
    @Transactional
    @Query("UPDATE Ingredient AS i SET i.price = i.price * 1.10")
    int updateIngredientsByPrice();
}
