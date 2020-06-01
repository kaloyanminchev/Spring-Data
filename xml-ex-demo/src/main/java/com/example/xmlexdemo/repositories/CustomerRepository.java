package com.example.xmlexdemo.repositories;

import com.example.xmlexdemo.models.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByName(String customerName);

    @Query("SELECT c FROM Customer AS c ORDER BY c.birthDate, c.youngDriver")
    List<Customer> findAllCustomersOrderedByBirthDateAndYoungDriver();

    @Query("SELECT c FROM Customer AS c WHERE c.sales.size >= 1")
    List<Customer> findAllCustomersBoughtAtLeastOneCar();
}
