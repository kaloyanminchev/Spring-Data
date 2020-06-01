package com.example.xmlexdemo.services;

import com.example.xmlexdemo.models.dtos.seeddtos.SupplierSeedDto;
import com.example.xmlexdemo.models.entities.Supplier;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface SupplierService {

    void seedSuppliers() throws JAXBException, FileNotFoundException;

    void save(SupplierSeedDto supplierSeedDto);

    Supplier getRandomSupplier();

    void visualizeSuppliersNotImporter() throws JAXBException;
}
