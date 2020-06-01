package com.example.xmlexdemo.services;

import javax.xml.bind.JAXBException;

public interface SaleService {
    void seedSales();

    void visualizeSalesWithDiscount() throws JAXBException;
}
