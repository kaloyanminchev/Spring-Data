package com.example.xmlexdemo.controllers;

import com.example.xmlexdemo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

@Component
public class AppController implements CommandLineRunner {
    private final SupplierService supplierService;
    private final PartService partService;
    private final CarService carService;
    private final CustomerService customerService;
    private final SaleService saleService;

    @Autowired
    public AppController(SupplierService supplierService,
                         PartService partService,
                         CarService carService,
                         CustomerService customerService,
                         SaleService saleService) {
        this.supplierService = supplierService;
        this.partService = partService;
        this.carService = carService;
        this.customerService = customerService;
        this.saleService = saleService;
    }

    @Override
    public void run(String... args) throws Exception {
//        seedDatabase();

//        Query 1
//        this.customerService.visualizeOrderedCustomers();

//        Query 2
//        this.carService.visualizeCarsByMake();

//        Query 3
//        this.supplierService.visualizeSuppliersNotImporter();

//        Query 4
//        this.carService.visualizeAllCarsWithTheirParts();

//        Query 5
//        this.customerService.visualizeCustomerWithSales();

//        Query 6
        this.saleService.visualizeSalesWithDiscount();
    }

    private void seedDatabase() throws JAXBException, FileNotFoundException {
        this.supplierService.seedSuppliers();
        this.partService.seedParts();
        this.carService.seedCars();
        this.customerService.seedCustomers();
        this.saleService.seedSales();
    }
}
