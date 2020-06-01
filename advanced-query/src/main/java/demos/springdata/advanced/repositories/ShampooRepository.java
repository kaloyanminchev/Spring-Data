package demos.springdata.advanced.repositories;

import demos.springdata.advanced.entities.Ingredient;
import demos.springdata.advanced.entities.Label;
import demos.springdata.advanced.entities.Shampoo;
import demos.springdata.advanced.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ShampooRepository extends JpaRepository<Shampoo, Long> {
    List<Shampoo> findAllBySize(Size size);

    List<Shampoo> findAllBySizeOrLabelOrderByPriceAsc(Size size, Label label);

    List<Shampoo> findAllByPriceGreaterThanOrderByPriceDesc(BigDecimal price);

    @Query("SELECT s FROM Shampoo AS s JOIN s.ingredients i WHERE i in :ingredients")
    List<Shampoo> findWithIngredientsInList(@Param("ingredients") Iterable<Ingredient> ingredients);

    @Query("SELECT s FROM Shampoo AS s WHERE s.ingredients.size < :count")
    List<Shampoo> findByCountOfIngredientsLessThan(@Param("count") int count);
}
