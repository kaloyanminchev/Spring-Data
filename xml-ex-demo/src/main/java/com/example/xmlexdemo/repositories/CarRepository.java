package com.example.xmlexdemo.repositories;

import com.example.xmlexdemo.models.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Car findByMakeAndModelAndTravelledDistance(String make, String model, Integer travelledDistance);

    List<Car> findAllByMakeOrderByModelAscTravelledDistanceDesc(String make);
}
