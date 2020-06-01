package com.jsonexdemo.repositories;

import com.jsonexdemo.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String productName);

//        @Query("SELECT p FROM Product AS p " +
//            "WHERE p.price IN(500, 1000) AND p.buyer = 'null' " +
//            "ORDER BY p.price ASC")
    List<Product> findAllByPriceBetweenAndBuyerIsNull(BigDecimal lower, BigDecimal higher);
}
