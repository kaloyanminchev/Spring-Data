package com.jsonexdemo.repositories;

import com.jsonexdemo.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT u FROM User As u WHERE u.soldProducts.size >= :size")
    List<User> findAllBySoldProductsSizeGreaterThan(@Param("size") int number);
}
