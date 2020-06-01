package com.example.xmlexdemo.services;

import com.example.xmlexdemo.models.dtos.seeddtos.CarSeedDto;
import com.example.xmlexdemo.models.entities.Car;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface CarService {

    void seedCars() throws JAXBException, FileNotFoundException;

    void save(CarSeedDto carSeedDto);

    Car getRandomCar();

    void visualizeCarsByMake() throws JAXBException;

    void visualizeAllCarsWithTheirParts() throws JAXBException;
}
