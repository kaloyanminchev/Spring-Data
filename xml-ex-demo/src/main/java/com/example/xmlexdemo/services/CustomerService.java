package com.example.xmlexdemo.services;

import com.example.xmlexdemo.models.dtos.seeddtos.CustomerSeedDto;
import com.example.xmlexdemo.models.entities.Customer;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface CustomerService {

    void seedCustomers() throws JAXBException, FileNotFoundException;

    void save(CustomerSeedDto customerSeedDto);

    Customer getRandomCustomer();

    void visualizeOrderedCustomers() throws JAXBException;

    void visualizeCustomerWithSales() throws JAXBException;
}
